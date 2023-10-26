package com.sawyer.component.task

import android.content.Context
import android.os.Looper
import android.os.SystemClock
import com.sawyer.common.utils.Logger
import com.sawyer.component.startup.AndroidStartup
import com.sawyer.component.startup.Startup

class Task4: AndroidStartup<Unit>() {

    companion object{
        private var depends = mutableListOf<Class<Startup<Any>>>()
        init {
            depends.add(Task2::class.java as Class<Startup<Any>>)
        }
    }

    override fun create(context: Context) {
        val t = if (Looper.myLooper() == Looper.getMainLooper()) "主线程:" else "子线程:"
        Logger.d(msg = "$t Task4：学习Http")
        SystemClock.sleep(1200)
        Logger.d(msg = "$t Task4：掌握Http")
    }

    override fun dependencies(): MutableList<Class<Startup<Any>>> {
        return depends
    }

    override fun callCreateOnMainThread() = false

    override fun waitOnMainThread() = false

}