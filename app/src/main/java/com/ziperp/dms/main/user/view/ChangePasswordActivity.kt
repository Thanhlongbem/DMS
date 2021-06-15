package com.ziperp.dms.main.user.view

import androidx.lifecycle.Observer
import com.ziperp.dms.Injection
import com.ziperp.dms.NotificationType
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.main.user.model.UpdatePasswordRequest
import com.ziperp.dms.showNotificationBanner
import com.ziperp.dms.utils.AppUtil
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity: BaseActivity() {

    private val viewModel by lazy { getViewModel { Injection.provideUserInfoViewModel()} }

    override fun setLayoutId(): Int = R.layout.activity_change_password

    override fun initView() {
        setToolbar(R.string.str_update_password.getString(), true)
        val userInfo = DmsUserManager.userInfo
        userInfo.apply {
            AppUtil.loadImageFromURL(this@ChangePasswordActivity, Constants.API_BASE_PATH + imgProfile, imgAvatar, defaultImg = R.drawable.avata_default)
            tv_name.text = staffNm
            tv_email.text = email
            tv_role.text = "$deptNm - $position"
        }

        btn_update.setOnClickListener {
            var isReadyToCUD = true
            listOf(old_password, new_password, confirm_password).forEach { editView ->
                editView.isNeedToCheckValid = true
                if (!editView.checkValid()) {
                    isReadyToCUD = false
                }
            }
            if (isReadyToCUD) {
                viewModel.updatePassword(UpdatePasswordRequest(
                    old_password.content,
                    new_password.content,
                    confirm_password.content
                ))
            }
        }

        viewModel.updatePasswordStatus.observe(this, Observer { response ->
            when(response.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    if (response.data?.data?.get(0)?.status == "OK") {
                        setResult(RESULT_OK)
                        finish()
                        showNotificationBanner(NotificationType.SUCCESS, R.string.str_success.getString())
                    } else {
                        showNotificationBanner(
                            NotificationType.ERROR,
                            response.data?.data?.get(0)?.errMsg?.replace("_", " ")?.split(":")?.get(0)
                                ?: "Something went wrong!")
                    }

                }
                Status.ERROR -> showNotificationBanner(NotificationType.ERROR, "Could not create customer right now!")
            }
        })
    }
}