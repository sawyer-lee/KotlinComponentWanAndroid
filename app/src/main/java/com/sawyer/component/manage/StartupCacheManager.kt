package com.sawyer.component.manage

import com.sawyer.component.startup.Startup
import java.util.concurrent.ConcurrentHashMap
import com.sawyer.component.startup.Result
import kotlin.math.min

/**
 * 缓存每个任务的执行结果
 * Result<*>：因为*可能会==null，在ConcurrentHashMap中不能保存null
 */
object StartupCacheManager {

    //可并发执行的缓存类
    private val mInitializedComponent = ConcurrentHashMap<Class<Startup<*>>,Result<*>>()

    //保存某个数据
    fun saveInitializedComponent(clazz: Class<Startup<*>>,result: Result<*>){
        mInitializedComponent[clazz] = result
    }

    //获得某个数据
    fun obtainInitializedResult(clazz: Class<Startup<*>>): Result<*>{
        return mInitializedComponent[clazz] as Result<*>
    }

    //移除某个数据
    fun remove(clazz: Class<Startup<*>>){
        mInitializedComponent.remove(clazz)
    }

    //清空数据
    fun clear(){
        mInitializedComponent.clear()
    }

    //是否已经初始化过
    fun hadInitialized(clazz: Class<Startup<*>>): Boolean{
        return mInitializedComponent.containsKey(clazz)
    }

}