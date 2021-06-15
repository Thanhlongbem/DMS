package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.trackingreports.salesresult.model.SalesDetailResponse
import com.ziperp.dms.main.trackingreports.salesresult.model.SalesSummationResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface SalesResultService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getSummationResult(
        @Body request: BodyRequest
    ): Single<SalesSummationResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getDetailResult(
        @Body request: BodyRequest
    ): Single<SalesDetailResponse>
}