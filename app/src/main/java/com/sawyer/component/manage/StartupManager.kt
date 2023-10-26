package com.sawyer.component.manage

import android.content.Context
import android.os.Looper
import com.sawyer.common.utils.Logger
import com.sawyer.component.sort.TopologySort
import com.sawyer.component.startup.*
import java.lang.RuntimeException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

class StartupManager(
    val context: Context,
    private val startupList: MutableList<Startup<Any>>,
    private val awaitLatch: CountDownLatch
) {
    lateinit var startupSortStore: StartupSortStore

    fun start(): StartupManager{
        if (Looper.myLooper() != Looper.getMainLooper()){
            throw RuntimeException("start函数需在主线程调用！")
        }
        startupSortStore = TopologySort.sort(startupList)
        for (startup in startupSortStore.result){
            //异步策略
            val runnable = StartupRunnable(context,startup,this)
            if (startup.callCreateOnMainThread()){
                runnable.run()
            }else{
                startup.executor()?.execute(runnable)
            }
        }
        return this
    }

    fun notifyChildren(startup: Startup<Any>){
        if (!startup.callCreateOnMainThread() && startup.waitOnMainThread()){
            awaitLatch.countDown()
        }
        //获得当前已经完成任务的所有子任务
        if (startupSortStore.startupChildrenMap.containsKey(startup.javaClass)){
            val childClsList = startupSortStore.startupChildrenMap[startup.javaClass]
            childClsList?.let {
                for (childCls in childClsList){
                    //通知子任务,父任务已完成
                    startupSortStore.startupMap[childCls]?.toNotify()
                }
            }
        }
    }

    companion object class Builder{

        private var startupList = mutableListOf<Startup<Any>>()
        //记录需要主线程等待完成的异步任务
        private val needAwaitCount = AtomicInteger()

        fun addStartup(startup: AndroidStartup<*>): Builder{
            startupList.add(startup)
            return this
        }

        fun addAllStartup(startups: List<Startup<Any>>): Builder{
            startupList.addAll(startups)
            return this
        }

        fun build(context: Context): StartupManager{
            //场景：处理主线程需要等待其他线程的任务执行
            for (startup in startupList){
                if (!startup.callCreateOnMainThread() && startup.waitOnMainThread()){
                    needAwaitCount.incrementAndGet()
                }
            }
            val awaitLatch = CountDownLatch(needAwaitCount.get())
            return StartupManager(context, startupList, awaitLatch)
        }
    }

    //场景：处理主线程需要等待其他线程的任务执行
    fun await(){
        try {
            awaitLatch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}