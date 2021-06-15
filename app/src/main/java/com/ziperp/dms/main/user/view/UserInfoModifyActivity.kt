package com.ziperp.dms.main.user.view

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import com.ziperp.dms.Injection
import com.ziperp.dms.NotificationType
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseCUDActivity
import com.ziperp.dms.common.model.EditView
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.form.model.KeyValue
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.convertDate
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.main.login.model.UserInfo
import com.ziperp.dms.main.user.model.UpdateUserInfoRequest
import com.ziperp.dms.showNotificationBanner
import com.ziperp.dms.utils.AppUtil
import kotlinx.android.synthetic.main.activity_user_info_mofify.*

class UserInfoModifyActivity: BaseCUDActivity<UserInfo>() {

    private val viewModel by lazy { getViewModel { Injection.provideUserInfoViewModel()} }

    override fun setLayoutId(): Int = R.layout.activity_user_info_mofify

    override fun initView() {
        setToolbar(R.string.str_edit_profile.getString(), true)
        userBirthday.setShowDatePicker()

        viewModel.cudRequestStatus.observe(this, Observer { response ->
            when(response.status) {
                Status.LOADING -> showLoading(true)
                Status.SUCCESS -> {
                    showLoading(false)
                    if (response.data?.data?.get(0)?.status == "OK") {
                        setResult(RESULT_OK)
                        finish()
                        showNotificationBanner(NotificationType.SUCCESS, R.string.str_success.getString())
                    } else {
                        showNotificationBanner(NotificationType.ERROR, response.data?.data?.get(0)?.errMsg?: "Something went wrong!")
                    }

                }
                Status.ERROR -> {
                    showLoading(false)
                    showNotificationBanner(NotificationType.ERROR, "Could not create customer right now!")
                }
            }
        })
    }

    override fun createNewForm() {
        //Modify mode only
    }

    @SuppressLint("SetTextI18n")
    override fun fillCurrentInfo() {
        item.apply {

            AppUtil.loadImageFromURL(this@UserInfoModifyActivity, Constants.API_BASE_PATH + imgProfile, imgAvatar, defaultImg = R.drawable.avata_default)
            tv_name.text = staffNm
            tv_email.text = email
            tv_role.text = "$deptNm - $position"

            phone.setValue(phoneNumber)
            idNo.setValue(staffId)
            userBirthday.setValue(birthday.reformatDate())
            userGender.setValue(gender)
            listOption[0].second.controlValue = arrayListOf(
                KeyValue(
                    keyCode = "GenderCd",
                    valueCode = genderCd,
                    keyName = "Gender",
                    valueName = gender
                )
            )
            marriageStatus.setValue(marriedStatusNm)
            listOption[1].second.controlValue = arrayListOf(
                KeyValue(
                    keyCode = "MarriedStatus",
                    valueCode = marriedStatus.toString(),
                    keyName = "MarriedStatusNm",
                    valueName = marriedStatusNm
                )
            )
            blood_type.setValue(bloodTypeNm)
            listOption[2].second.controlValue = arrayListOf(
                KeyValue(
                    keyCode = "BloodType",
                    valueCode = bloodType,
                    keyName = "BloodTypeNm",
                    valueName = bloodTypeNm
                )
            )
            tv_address.setText(address)
        }
    }

    override fun setListOption(): ArrayList<Pair<EditView, ItemControlForm>> = arrayListOf(
        userGender to ItemControlForm(
            formId = Form.FORM_ID_USER_INFO,
            controlId = "cmbGender",
            controlName = "Gender",
            lookUpCode = "0074",
            lookUpType = Form.LookUpType.COMBO,
            findType = Form.FindType.CODE
        ),
        marriageStatus to ItemControlForm(
            formId = Form.FORM_ID_USER_INFO,
            controlId = "cmbMarriageSts",
            controlName = "Marriage Status",
            lookUpCode = "0039",
            lookUpType = Form.LookUpType.COMBO,
            findType = Form.FindType.CODE
        ),
        blood_type to ItemControlForm(
            formId = Form.FORM_ID_USER_INFO,
            controlId = "cmbBloodType",
            controlName = "Blood Type",
            lookUpCode = "0076",
            lookUpType = Form.LookUpType.COMBO,
            findType = Form.FindType.CODE
        )
    )

    override fun setListMandatoryView(): ArrayList<EditView> = arrayListOf(userGender)

    override fun requestUpdate() {
        val request = UpdateUserInfoRequest()
        request.apply {
            phoneNumber = phone.content
            birthday = userBirthday.content.convertDate()
            genderCd = listOption[0].second.getFilterValue()
            marriedStatus = listOption[1].second.getFilterValue()
            bloodType = listOption[2].second.getFilterValue()
            address = tv_address.text.toString()
        }
        viewModel.updateUserInfo(request)
    }
}