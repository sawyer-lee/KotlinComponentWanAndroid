package com.sawyer.component.startup

import android.os.Process
import com.sawyer.common.utils.Logger
import com.sawyer.component.manage.ExecutorManager
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor

abstract class  AndroidStartup<T> : Startup<Any> {

    //CountDownLatch：闭锁，用于多线程任务的同步执行
    private val mWaitCountDown = CountDownLatch(dependenciesCount())

    override fun dependenciesCount(): Int{
        return dependencies()?.size ?: 0
    }

    override fun dependencies(): MutableList<Class<Startup<Any>>>? {
        return null
    }

    override fun toWait() {
        mWaitCountDown.await()
    }

    override fun toNotify() {
        mWaitCountDown.countDown()
    }

    override fun executor(): Executor = ExecutorManager.ioExecutor

    override fun getThreadPriority() = Process.THREAD_PRIORITY_DEFAULT
}