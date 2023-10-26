package com.sawyer.component.startup

import android.content.Context

interface Startup<T : Any> : Dispatcher{

    fun create(context: Context): T

    fun dependenciesCount(): Int

    //返回当前任务所依赖的任务
    fun dependencies(): MutableList<Class<Startup<Any>>>?

}