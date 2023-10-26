package com.sawyer.component.task

import android.content.Context
import android.os.Looper
import android.os.SystemClock
import com.sawyer.common.utils.Logger
import com.sawyer.component.startup.AndroidStartup
import com.sawyer.component.startup.Startup

class Task3: AndroidStartup<Unit>() {

    companion object{
        private var depends = mutableListOf<Class<Startup<Any>>>()
        init {
            depends.add(Task1::class.java as Class<Startup<Any>>)
        }
    }

    override fun create(context: Context) {
        val t = if (Looper.myLooper() == Looper.getMainLooper()) "主线程:" else "子线程:"
        Logger.d(msg = "$t Task3：学习设计模式")
        SystemClock.sleep(2000)
        Logger.d(msg = "$t Task3：掌握设计模式")
    }

    override fun dependencies(): MutableList<Class<Startup<Any>>> {
        return depends
    }

    override fun callCreateOnMainThread() = false

    override fun waitOnMainThread() = false
}