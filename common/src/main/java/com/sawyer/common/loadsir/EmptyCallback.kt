package com.sawyer.common.loadsir

import android.content.Context
import android.view.View
import com.kingja.loadsir.callback.Callback
import com.sawyer.common.R

class EmptyCallback : Callback() {

    override fun onCreateView() = R.layout.base_layout_empty

    //当前Callback的点击事件，如果返回true则覆盖注册时的onReload()，
    //如果返回false则两者都执行，先执行onReloadEvent()
    override fun onReloadEvent(context: Context?, view: View?) = false
}