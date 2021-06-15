package com.ziperp.dms.camera

import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.utils.AppUtil
import kotlinx.android.synthetic.main.fragment_image_viewer.*

class ImageFragment(val path: String, val isLocal: Boolean): BaseFragment() {
    override fun setLayoutId(): Int = R.layout.fragment_image_viewer

    override fun initView() {
        if(isLocal){
            AppUtil.loadImageFromLocal(context, path, image_view)
        } else {
            AppUtil.loadImageFromURL(context, Constants.API_BASE_PATH + path, image_view, true)
        }
    }
}