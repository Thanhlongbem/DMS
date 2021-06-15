package com.ziperp.dms.main.user.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.base.BaseCUDActivity
import com.ziperp.dms.base.BaseCUDActivity.Companion.EXTRA_CUD_MODE
import com.ziperp.dms.base.BaseCUDActivity.Companion.EXTRA_CUD_OBJECT
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.main.login.model.UserInfo
import com.ziperp.dms.utils.AppUtil
import kotlinx.android.synthetic.main.activity_user_profile.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.*


class UserInfoActivity: BaseActivity() {

    lateinit var userInfo: UserInfo
    private val viewModel by lazy { getViewModel { Injection.provideLoginViewModel()} }
    private val userInfoViewModel by lazy { getViewModel { Injection.provideUserInfoViewModel()} }

    override fun setLayoutId(): Int = R.layout.activity_user_profile

    @SuppressLint("SetTextI18n")
    override fun initView() {
        setToolbar(R.string.str_user_infomation.getString(), true)
        userInfo = DmsUserManager.userInfo
        updateInfo(userInfo)

        viewModel.userInfoLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        val userInfo = response.table[0]
                        DmsUserManager.updateUserInfo(userInfo)
                        this@UserInfoActivity.userInfo = userInfo
                        updateInfo(userInfo)
                    }
                }
                Status.LOADING -> { }
                Status.ERROR -> { }
            }
        })

        userInfoViewModel.updateAvatarStatus.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        viewModel.getUserInfo(userInfo.userId)
                    }
                }
                Status.LOADING -> { }
                Status.ERROR -> { }
            }
        })

        imgChangeAvatar.setOnClickListener {
            loadImageFromGallery()
        }
    }

    @AfterPermissionGranted(STORAGE_PERM)
    fun loadImageFromGallery() {
        if (hasStoragePermission()) {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setFixAspectRatio(true)
                .start(this)
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                this,
                "Allow access gallery to import photo",
                STORAGE_PERM,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun hasStoragePermission(): Boolean {
        return EasyPermissions.hasPermissions(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            viewModel.getUserInfo(userInfo.userId)
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val resultUri = CropImage.getActivityResult(data)?.uri
            resultUri?.let {
                AppUtil.getFilePathFromURI(this, resultUri)?.let {
                    userInfoViewModel.updateAvatar(it)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateInfo(user: UserInfo) {
        user.apply {

            AppUtil.loadImageFromURL(
                this@UserInfoActivity,
                Constants.API_BASE_PATH + imgProfile,
                imgAvatar,
                enableCache = false,
                defaultImg = R.drawable.avata_default
            )
            tvName.text = "$staffNo - $staffNm"
            tvMail.text = email
            tvPart.text = deptNm
            tvRole.text = ""
            tvPhone.text = phoneNumber
            tvCardVisit.text = tenantId
            tvBirthday.text = birthday.reformatDate()
            tvRelationship.text = marriedStatusNm
            tvGender.text = gender
            tvBloodType.text = bloodTypeNm
            tvAddress.text = address
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        menu?.findItem(R.id.action_edit)?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {
                val intent = Intent(this, UserInfoModifyActivity::class.java)
                intent.putExtra(EXTRA_CUD_MODE, BaseCUDActivity.UPDATE_MODE)
                intent.putExtra(EXTRA_CUD_OBJECT, userInfo)
                startActivityForResult(intent, 100)
            }
        }
        return true
    }

    companion object {
        const val STORAGE_PERM = 123
    }

}