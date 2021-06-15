package com.ziperp.dms.core.service

import com.ziperp.dms.camera.CustomerImagesResponse
import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomerImageService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getImageList(
        @Body request: BodyRequest
    ): Single<CustomerImagesResponse>

}

