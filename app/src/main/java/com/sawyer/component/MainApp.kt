package com.sawyer.component

import android.app.Application
import android.content.Context
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import androidx.paging.ExperimentalPagingApi
import com.alibaba.android.arouter.facade.model.RouteMeta.build
import com.alibaba.android.arouter.launcher.ARouter
import com.kingja.loadsir.core.LoadSir
import com.sawyer.common.loadsir.EmptyCallback
import com.sawyer.common.loadsir.ErrorCallback
import com.sawyer.common.loadsir.LoadingCallback
import com.sawyer.component.manage.StartupManager
import com.sawyer.component.task.*
import com.sawyer.login.di.moduleLogin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@ExperimentalPagingApi
class MainApp : Application() {

    private val modules = arrayListOf(
        moduleLogin
    )

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onCreate() {
        super.onCreate()
        initARouter()
        initLoadSir()
        //Koin：kotlin依赖注入框架
        initKoin()

        //使用ContentProvider帮助我们自动的构建多任务
        /*StartupManager.Builder()
            .addStartup(Task3())
            .addStartup(Task5())
            .addStartup(Task2())
            .addStartup(Task1())
            .addStartup(Task4())
            .build(this)
            .start()
            //场景：处理主线程需要等待其他线程的任务执行
            .await()*/
    }

    private fun initARouter() {
        ARouter.init(this)
        if (BuildConfig.DEBUG){
            ARouter.openLog()
            ARouter.openDebug()
        }
    }

    private fun initLoadSir() {
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())
            .addCallback(EmptyCallback())
            .addCallback(ErrorCallback())
            .setDefaultCallback(LoadingCallback::class.java)
            .commit()
    }

    private fun initKoin(){
        startKoin {
            androidLogger()
            androidContext(this@MainApp)
            modules(modules)
        }
    }
}