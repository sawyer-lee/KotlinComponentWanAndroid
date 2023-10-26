package com.sawyer.component.provider

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.sawyer.component.startup.Startup

object StartupInitializer {

    private const val meta_value = "android.startup"

    @Throws(Exception::class)
    fun discoverAndInitialize(context: Context, providerName: String): List<Startup<Any>>{
        val startupMap = mutableMapOf<Class<Startup<Any>>,Startup<Any>>()
        //获得ContentProvider中定义的meta-data
        val provider = ComponentName(context, providerName)
        val providerInfo = context.packageManager.getProviderInfo(provider,
            PackageManager.GET_META_DATA)
        //获得AndroidManifest中定义的任务
        for (key in providerInfo.metaData.keySet()){
            val value = providerInfo.metaData.getString(key)
            if (value == meta_value){
                val clazz = Class.forName(key)
                if (Startup::class.java.isAssignableFrom(clazz)) {
                    doInitialize(clazz.newInstance() as Startup<Any>, startupMap)
                }
            }
        }
        return ArrayList(startupMap.values)
    }

    @Throws(Exception::class)
    fun doInitialize(
        startup: Startup<Any>,
        startupMap: MutableMap<Class<Startup<Any>>, Startup<Any>>
    ) {
        startupMap[startup.javaClass] = startup
        if (startup.dependenciesCount() != 0){
            //循环遍历，存放所有的任务
            for(parent in startup.dependencies()!!){
                doInitialize(parent.newInstance(), startupMap)
            }
        }
    }

}