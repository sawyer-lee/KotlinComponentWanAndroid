package com.sawyer.component.task

import android.content.Context
import android.os.Looper
import android.os.SystemClock
import com.sawyer.common.utils.Logger
import com.sawyer.component.startup.AndroidStartup
import com.sawyer.component.startup.Startup
import java.util.HashMap

class Task2: AndroidStartup<Unit>() {

    companion object{
        private var depends = mutableListOf<Class<Startup<Any>>>()
        init {
            depends.add(Task1::class.java as Class<Startup<Any>>)
        }
    }

    override fun create(context: Context) {
        val t = if (Looper.myLooper() == Looper.getMainLooper()) "主线程:" else "子线程:"
        Logger.d(msg = "$t Task2：学习Socket")
        SystemClock.sleep(1000)
        Logger.d(msg = "$t Task2：掌握Socket")
    }

    override fun callCreateOnMainThread() = true

    override fun waitOnMainThread() = false

    override fun dependencies(): MutableList<Class<Startup<Any>>> {
        return depends
    }

}