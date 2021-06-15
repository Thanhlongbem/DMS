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

interface VisitCustomerService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getVisitCustomer(
        @Body request: BodyRequest
    ): Single<VisitCustomerResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getVisitCustomerInfo(
        @Body request: BodyRequest
    ): Single<VisitCustomerInfoResponse>

    @POST(Constants.API.PATH_CRUD_FORM)
    fun saveVisitCustomerInfo(
        @Body request: BodyRequest
    ): Single<SaveVisitCustomerInfoResponse>


    @POST(Constants.API.PATH_QUERY_DATA)
    fun getVisitCustomerOrder(
        @Body request: BodyRequest
    ): Single<VisitCustomerOrderResponse>
}