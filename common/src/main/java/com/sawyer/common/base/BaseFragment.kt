package com.sawyer.common.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.sawyer.common.R
import com.sawyer.common.databinding.BaseFragmentLayoutBinding
import com.sawyer.common.widget.LoadingDialog

abstract class BaseFragment<DB : ViewDataBinding>: Fragment() {

    private lateinit var mBinding: DB
    private lateinit var mBaseContainBinding: BaseFragmentLayoutBinding

    private lateinit var mContext: Context
    private lateinit var mLoadingDialog: LoadingDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        mBaseContainBinding = DataBindingUtil.inflate(inflater,
            R.layout.base_fragment_layout, container, false)
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(),
            container, false)
        mBaseContainBinding.baseContainer.addView(mBinding.root)
        return mBaseContainBinding.root
    }

    abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }

    abstract fun getLayoutId(): Int

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLoadingDialog = LoadingDialog(view.context, false)
        initData()
    }

    private fun showLoading() {
        mLoadingDialog.showDialog(mContext, false)
    }

    private fun dismissLoading() {
        mLoadingDialog.dismissDialog()
    }

    private var time: Long = 0
    private var oldMsg: String? = null

    //相同msg 只显示一个
    fun showToast(msg: String) {
        if (msg != oldMsg) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
            time = System.currentTimeMillis()
        } else {
            if (System.currentTimeMillis() - time > 2000) {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
                time = System.currentTimeMillis()
            }
        }
        oldMsg = msg
    }
}