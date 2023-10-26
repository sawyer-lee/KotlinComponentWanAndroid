package com.sawyer.common.base

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sawyer.common.utils.StatusBar
import com.sawyer.common.widget.LoadingDialog

abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity() {

    lateinit var mBinding: DB
    private lateinit var mLoadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBar.fitSystemBar(this)
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        initData(savedInstanceState)
    }

    abstract fun getLayoutId(): Int

    abstract fun initData(savedInstanceState: Bundle?)

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }

    fun showLoading() {
        mLoadingDialog.showDialog(this, false)
    }

    fun dismissLoading() {
        mLoadingDialog.dismissDialog()
    }

    //设置toolbar名称
    protected fun setToolbarTitle(view: TextView, title: String) {
        view.text = title
    }

    //设置toolbar返回按键图片
    protected fun setToolbarBackIcon(view: ImageView, id: Int) {
        view.setBackgroundResource(id)
    }
}