package com.sawyer.component

import android.content.Intent
import android.os.Bundle
import android.view.Window
import com.sawyer.common.base.BaseActivity
import com.sawyer.common.utils.StatusBar
import com.sawyer.component.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun getLayoutId(): Int = R.layout.activity_splash
}