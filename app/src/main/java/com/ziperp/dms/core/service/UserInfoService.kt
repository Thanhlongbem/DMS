package com.ziperp.dms.core.service

import com.ziperp.dms.common.model.CUDResponse
import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.user.model.UpdateAvatarResponse
import com.ziperp.dms.main.visitcustomer.model.UploadFileResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserInfoService {
    @POST(Constants.API.PATH_CRUD_FORM)
    fun updateUserInfo(
        @Body request: BodyRequest
    ): Single<CUDResponse>

    @POST(Constants.API.PATH_CRUD_FORM)
    fun updatePassword(
        @Body request: BodyRequest
    ): Single<CUDResponse>

    @Multipart
    @POST(Constants.API.PATH_UPDATE_AVATAR)
    fun updateAvatar(
        @Part("pStaffId") staffId: RequestBody,
        @Part("pUserId") userId: RequestBody,
        @Part("pLangId") langId: RequestBody,
        @Part("pConnStr") connStr: RequestBody,
        @Part("pPageMode") pageMode: RequestBody,
        @Part("pFolder") folder: RequestBody,
        @Part image: MultipartBody.Part
    ): Single<UpdateAvatarResponse>
}