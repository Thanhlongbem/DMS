package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.itemmaster.model.*
import com.ziperp.dms.main.salespricepromotion.model.SalesPriceInfoResponse
import com.ziperp.dms.main.salespricepromotion.model.SalesPriceResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface SalesPriceService {

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getSalesPrice(
        @Body request: BodyRequest
    ): Single<SalesPriceResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getSalesPriceInfo(
        @Body request: BodyRequest
    ): Single<SalesPriceInfoResponse>

}


