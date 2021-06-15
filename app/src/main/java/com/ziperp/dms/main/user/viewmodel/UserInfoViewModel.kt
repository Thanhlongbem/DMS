package com.ziperp.dms.main.user.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.common.model.CUDResponse
import com.ziperp.dms.core.repository.UserInfoRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.user.model.UpdateAvatarResponse
import com.ziperp.dms.main.user.model.UpdatePasswordRequest
import com.ziperp.dms.main.user.model.UpdateUserInfoRequest
import com.ziperp.dms.utils.LogUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class UserInfoViewModel(val repository: UserInfoRepository) : BaseViewModel() {
    val cudRequestStatus = MutableLiveData<ResponseData<CUDResponse>>()
    val updatePasswordStatus = MutableLiveData<ResponseData<CUDResponse>>()
    val updateAvatarStatus = MutableLiveData<ResponseData<UpdateAvatarResponse>>()

    fun updateUserInfo(request: UpdateUserInfoRequest) {
        val pJsonData = Gson().toJson(request)
        val bodyRequest = BodyRequestFactory.get(RequestType.USER_INFO, pJsonData, pPageMode = "U")

        cudRequestStatus.postValue(ResponseData.loading())
        repository
            .updateUserInfo(bodyRequest)
            .applyOn()
            .subscribe({
                cudRequestStatus.postValue(ResponseData.success(it))
            }, {
                LogUtils.d("Error ${it.message})")
                cudRequestStatus.postValue(
                    ResponseData.error(
                        it.message ?: "Something went wrong!",
                        null
                    )
                )
            }).disposedBy(compositeDisposable)

    }

    fun updatePassword(request: UpdatePasswordRequest) {
        val pJsonData = Gson().toJson(request)
        val bodyRequest = BodyRequestFactory.get(
            RequestType.USER_PASSWORD,
            pJsonData,
            pPageMode = "U"
        )

        updatePasswordStatus.postValue(ResponseData.loading())
        repository
            .updatePassword(bodyRequest)
            .applyOn()
            .subscribe({
                updatePasswordStatus.postValue(ResponseData.success(it))
            }, {
                updatePasswordStatus.postValue(
                    ResponseData.error(
                        it.message ?: "Something went wrong!", null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun updateAvatar(imgPath: String) {
        updateAvatarStatus.postValue(ResponseData.loading())

        val file = File(imgPath)
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val imageRequestBody = MultipartBody.Part.createFormData("file", file.name, requestFile)

        repository
            .updateAvatar(imageRequestBody)
            .applyOn()
            .subscribe({
                updateAvatarStatus.postValue(ResponseData.success(it))
            }, {
                updateAvatarStatus.postValue(ResponseData.error(it.message ?: "Something went wrong!", null))
            }).disposedBy(compositeDisposable)
    }

}