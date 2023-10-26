package com.sawyer.component.task

import android.content.Context
import android.os.Looper
import android.os.SystemClock
import com.sawyer.common.utils.Logger
import com.sawyer.component.startup.AndroidStartup
import com.sawyer.component.startup.Startup

class Task1 : AndroidStartup<String>() {

    override fun create(context: Context): String {
        val t = if (Looper.myLooper() == Looper.getMainLooper()) "主线程:" else "子线程:"
        Logger.d(msg = "$t Task1：学习Java基础")
        SystemClock.sleep(1000)
        Logger.d(msg = "$t Task1：掌握Java基础")
        return "Task1返回数据"
    }

    override fun callCreateOnMainThread() = false

    override fun waitOnMainThread() = false

}