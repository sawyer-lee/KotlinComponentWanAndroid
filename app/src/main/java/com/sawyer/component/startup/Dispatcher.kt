package com.sawyer.component.startup

import java.util.concurrent.Executor

interface Dispatcher {
    //是否在主线程执行
    fun callCreateOnMainThread(): Boolean
    //主线程是否需要等待该任务执行完成再开始执行
    fun waitOnMainThread(): Boolean
    //等待
    fun toWait()
    //有父任务执行完毕
    fun toNotify()
    //线程池执行任务
    fun executor(): Executor?
    //线程执行的优先级
    fun getThreadPriority(): Int
}