package com.sawyer.component

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.alibaba.android.arouter.facade.annotation.Route
import com.sawyer.common.base.BaseActivity
import com.sawyer.common.utils.Constants
import com.sawyer.common.utils.setupWithNavController
import com.sawyer.component.databinding.ActivityMainBinding
import com.sawyer.project.R.id.projectContentFragment

@Route(path = Constants.PATH_MAIN)
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private var currentNavController: LiveData<NavController>? = null

    override fun getLayoutId() = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {
       if( savedInstanceState == null) setupBottomNavigationBar()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    //Navigation绑定BottomNavigationView
    private fun setupBottomNavigationBar(){
        val navGraphIds = listOf(R.navigation.navi_home,
                R.navigation.navi_project, R.navigation.navi_personal)

        val controller = mBinding.navView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        controller.observe(this, Observer {
            it.addOnDestinationChangedListener { _, destination, _ ->
                run {
                    val id = destination.id
                    mBinding.navView.visibility =
                        if (id == projectContentFragment) View.GONE else View.VISIBLE
                }
            }
        })

        currentNavController = controller
    }

    override fun onSupportNavigateUp() = currentNavController?.value?.navigateUp() ?: false
}