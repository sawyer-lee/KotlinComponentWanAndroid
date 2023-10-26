package com.sawyer.component.task

import android.content.Context
import android.os.Looper
import android.os.SystemClock
import com.sawyer.common.utils.Logger
import com.sawyer.component.startup.AndroidStartup
import com.sawyer.component.startup.Startup

class Task5: AndroidStartup<Unit>() {

    companion object{
        private var depends = mutableListOf<Class<Startup<Any>>>()
        init {
            depends.add(Task3::class.java as Class<Startup<Any>>)
            depends.add(Task4::class.java as Class<Startup<Any>>)
        }
    }

    override fun create(context: Context) {
        val t = if (Looper.myLooper() == Looper.getMainLooper()) "主线程:" else "子线程:"
        Logger.d(msg = "$t Task5：学习OkHttp")
        SystemClock.sleep(1000)
        Logger.d(msg = "$t Task5：掌握OkHttp")
    }

    override fun dependencies(): MutableList<Class<Startup<Any>>> {
        return depends
    }

    override fun callCreateOnMainThread() = false

    override fun waitOnMainThread() = true
}