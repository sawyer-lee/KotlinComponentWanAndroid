package com.sawyer.project.ui

import androidx.paging.ExperimentalPagingApi
import com.sawyer.common.base.BaseFragment
import com.sawyer.project.R
import com.sawyer.project.databinding.FragmentProjectBinding

@ExperimentalPagingApi
class ProjectFragment : BaseFragment<FragmentProjectBinding>(){
    override fun initData() { }

    override fun getLayoutId() = R.layout.fragment_project
}