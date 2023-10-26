package com.sawyer.common.utils

import android.util.Log

object TimeMonitorManager {
    private val mTimeTagMap: HashMap<String, Long> = HashMap()
    private var mStartTime = 0L

    //开始监听
    fun startMonitor(){
        if(mTimeTagMap.size > 0) mTimeTagMap.clear()
        mStartTime = System.currentTimeMillis()
    }

    //结束监听
    fun endMonitor(tag: String){
        if(mTimeTagMap[tag] != null) mTimeTagMap.remove(tag)
        val time = System.currentTimeMillis() - mStartTime
        mTimeTagMap[tag] = time
        showData()
    }

    private fun showData(){
        if (mTimeTagMap.size <= 0) return
        for (tag in mTimeTagMap.keys){
            val time = mTimeTagMap[tag]
            Log.d("lee", " $tag : $time")
        }
    }

}