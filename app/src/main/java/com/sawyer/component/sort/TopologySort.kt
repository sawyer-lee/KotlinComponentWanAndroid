package com.sawyer.component.sort

import com.sawyer.common.utils.Logger
import com.sawyer.component.startup.Startup
import com.sawyer.component.startup.StartupSortStore
import java.util.*
import kotlin.collections.HashMap

//拓扑排序
object TopologySort {

    fun sort(startupList: List<Startup<Any>>): StartupSortStore{
        //每个任务的入度数
        val inDegreeMap = HashMap<Class<Startup<Any>>, Int>()
        //0入度任务队列
        val zeroDeque = ArrayDeque<Class<Startup<Any>>>()
        //当前任务和其Class
        val startupMap = HashMap<Class<Startup<Any>>, Startup<Any>>()
        //每个任务所依赖度任务列表
        val startupChildrenMap = HashMap<Class<Startup<Any>>,MutableList<Class<Startup<Any>>>>()

        for (startup in startupList){
            startupMap[startup.javaClass] = startup
            //记录每个任务的入度数
            val dependenciesCount = startup.dependenciesCount()
            inDegreeMap[startup.javaClass] = dependenciesCount

            if(dependenciesCount == 0){//记录入度数为0的任务
                zeroDeque.offer(startup.javaClass)
            }else{//遍历此任务所依赖的任务列表
                for(parent in startup.dependencies()!!){
                    var children = startupChildrenMap[parent]
                    if (children == null){
                        children = mutableListOf()
                        startupChildrenMap[parent] = children
                    }
                    children.add(startup.javaClass)
                }
            }
        }

        val result = mutableListOf<Startup<Any>>()
        //记录在Main线程执行的任务
        val main = mutableListOf<Startup<Any>>()
        //记录在异步线程执行的任务
        val threads = mutableListOf<Startup<Any>>()

        //处理入度为0的任务
        while (zeroDeque.isNotEmpty()){
            val cls = zeroDeque.poll()
            val startup = startupMap[cls]
            startup?.let {
                if (startup.callCreateOnMainThread()){
                    main.add(startup)
                }else{
                    threads.add(startup)
                }
            }
            //删除此入度为0的任务
            if (startupChildrenMap.containsKey(cls)){
                val childStartup = startupChildrenMap[cls]
                for (childCls in childStartup!!){
                    val num = inDegreeMap[childCls]
                    inDegreeMap[childCls] = num?.minus(1)!!
                    if (num.minus(1) == 0){
                        zeroDeque.offer(childCls)
                    }
                }
            }
        }
        //排序好的任务，无论主线程还是子线程，都添加到一个集合
        result.addAll(threads)
        result.addAll(main)
        for (name in result){
            Logger.d(msg = name.javaClass.name)
        }
        return StartupSortStore(result,startupMap,startupChildrenMap)
    }

}