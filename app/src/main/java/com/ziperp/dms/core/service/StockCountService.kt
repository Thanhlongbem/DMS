package com.ziperp.dms.core.service

import com.ziperp.dms.common.model.CUDResponse
import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.visitcustomer.model.StockCountListResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface StockCountService {

    @POST(Constants.API.PATH_CRUD_FORM)
    fun cudItemCount(
        @Body request: BodyRequest
    ): Single<CUDResponse>


    @POST(Constants.API.PATH_QUERY_DATA)
    fun getStockCountList(
        @Body request: BodyRequest
    ): Single<StockCountListResponse>

}