package com.sawyer.common.loadsir

import com.kingja.loadsir.callback.Callback
import com.sawyer.common.R

class ErrorCallback : Callback() {
    override fun onCreateView() = R.layout.base_layout_error
}