package com.sawyer.component.manage

import android.os.Handler
import android.os.Looper
import java.util.concurrent.*
import kotlin.math.max
import kotlin.math.min
object ExecutorManager {

    //获得CPU核心数
    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
    private val CORE_POOL_SIZE = max(2, min(CPU_COUNT - 1, 5))
    private val MAX_POOL_SIZE = CORE_POOL_SIZE
    private val KEEP_ALIVE_TIME = 5L

    val cpuExecutor = ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE,KEEP_ALIVE_TIME,
        TimeUnit.SECONDS, LinkedBlockingQueue(), Executors.defaultThreadFactory()).apply {
        allowCoreThreadTimeOut(true)
    }

    val ioExecutor = Executors.newCachedThreadPool()

    val mainExecutor = object : Executor{
        val handler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            handler.post(command)
        }
    }







}

