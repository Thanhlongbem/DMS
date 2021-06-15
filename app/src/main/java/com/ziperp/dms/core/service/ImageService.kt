package com.ziperp.dms.core.service

import com.ziperp.dms.camera.CustomerImagesResponse
import com.ziperp.dms.camera.SaveToSQLFileRequest
import com.ziperp.dms.camera.SaveToSQLFileResponse
import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.visitcustomer.model.*
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageService {

    @Multipart
    @POST(Constants.API.PATH_UPLOAD_FILE)
    fun uploadFile(
        @Part image: MultipartBody.Part,
        @Part("ItemCd") itemCd: RequestBody
    ): Single<UploadFileResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getFileOfVisitCustomer(
        @Body request: BodyRequest
    ): Single<CustomerImagesResponse>

  @POST(Constants.API.PATH_SAVE_FILE_INFO)
    fun saveSQLFileInfo(
        @Body request: SaveToSQLFileRequest
    ): Single<SaveToSQLFileResponse>

}