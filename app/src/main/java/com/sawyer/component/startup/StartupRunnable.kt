package com.sawyer.component.startup

import android.content.Context
import android.os.Process
import com.sawyer.common.utils.Logger
import com.sawyer.component.manage.StartupCacheManager
import com.sawyer.component.manage.StartupManager
import com.sawyer.component.task.Task5

//异步策略
class StartupRunnable(
    val context: Context,
    val startup: Startup<Any>,
    private val startupManager: StartupManager
) : Runnable {

    override fun run() {
        Process.setThreadPriority(startup.getThreadPriority())
        startup.toWait()
        val startupResult = startup.create(context)
        StartupCacheManager.saveInitializedComponent(startup.javaClass, Result(startupResult))
        startupManager.notifyChildren(startup)
    }
}