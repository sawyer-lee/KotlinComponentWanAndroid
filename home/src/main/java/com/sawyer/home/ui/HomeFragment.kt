package com.sawyer.home.ui

import androidx.paging.ExperimentalPagingApi
import com.sawyer.common.base.BaseFragment
import com.sawyer.home.R
import com.sawyer.home.databinding.FragmentHomeBinding

/**
 * @date：2050
 * @author sawyer
 * @instruction：整个首页容器，包含‘每日一问’、‘首页’、‘广场’三个fragment
 */
@ExperimentalPagingApi
class HomeFragment : BaseFragment<FragmentHomeBinding>(){
    override fun initData() { }

    override fun getLayoutId() = R.layout.fragment_home


}