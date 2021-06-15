package com.ziperp.dms.core.repository

import com.ziperp.dms.common.model.CUDResponse
import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants.PREF_CURRENT_LANGUAGE
import com.ziperp.dms.core.service.UserInfoService
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.extensions.toRequestBody
import com.ziperp.dms.main.user.model.UpdateAvatarResponse
import com.ziperp.dms.utils.DMSPreference
import io.reactivex.Single
import okhttp3.MultipartBody

class UserInfoRepository(private val userInfoService: UserInfoService) {
    fun updateUserInfo(request: BodyRequest): Single<CUDResponse> {
        return userInfoService.updateUserInfo(request)
    }

    fun updatePassword(request: BodyRequest): Single<CUDResponse> {
        return userInfoService.updatePassword(request)
    }

    fun updateAvatar(image: MultipartBody.Part): Single<UpdateAvatarResponse> {
        DmsUserManager.userInfo.apply {
            return userInfoService.updateAvatar(
                staffId.toRequestBody(),
                userId.toRequestBody(),
                DMSPreference.getInt(PREF_CURRENT_LANGUAGE, 1).toString().toRequestBody(),
                "".toRequestBody(),
                "L".toRequestBody(),
                "$siteID/frmRStaff/$staffId".toRequestBody(),
                image
            )
        }
    }
}