package com.sawyer.personal.ui

import androidx.paging.ExperimentalPagingApi
import com.sawyer.common.base.BaseFragment
import com.sawyer.personal.R
import com.sawyer.personal.databinding.FragmentPersonalBinding

@ExperimentalPagingApi
class FragmentPersonal : BaseFragment<FragmentPersonalBinding>(){
    override fun initData() { }

    override fun getLayoutId() = R.layout.fragment_personal
}