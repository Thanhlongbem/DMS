package com.ziperp.dms.main.user.view

import android.content.Intent
import android.view.View
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.common.activity.SettingsActivity
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.main.login.model.UserInfo
import com.ziperp.dms.utils.AppUtil
import kotlinx.android.synthetic.main.activity_more.*

class MoreActivity : BaseActivity() {

    lateinit var userInfo: UserInfo

    override fun setLayoutId(): Int = R.layout.activity_more

    override fun initView() {
        setToolbar(R.string.str_more.getString(), true)
        userInfo = DmsUserManager.userInfo
        tv_name.text = userInfo.staffNm
        tv_username.text = userInfo.userId

        setOnClickListener(layoutDataNotSync, layoutUserInformation, layoutChangePass)
    }

    override fun onResume() {
        super.onResume()
        AppUtil.loadImageFromURL(
            this,
            Constants.API_BASE_PATH + userInfo.imgProfile,
            imgAvatar,
            enableCache = false,
            defaultImg = R.drawable.avata_default
        )
    }

    override fun onClick(view: View?) {
        when(view){
            layoutDataNotSync ->{
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            layoutUserInformation ->{
                startActivity(Intent(this, UserInfoActivity::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            layoutChangePass -> {
                startActivity(Intent(this, ChangePasswordActivity::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }
}