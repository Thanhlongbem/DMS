package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.salespricepromotion.model.PromotionInfoResponse
import com.ziperp.dms.main.salespricepromotion.model.PromotionResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface PromotionService {

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getPromotions(
        @Body request: BodyRequest
    ): Single<PromotionResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getPromotionInfo(
        @Body request: BodyRequest
    ): Single<PromotionInfoResponse>

}


