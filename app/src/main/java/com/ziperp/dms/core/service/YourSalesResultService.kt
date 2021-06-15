package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.personalreport.salesresult.model.CustomerResultResponse
import com.ziperp.dms.main.personalreport.salesresult.model.ItemResultResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface YourSalesResultService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getCustomerResult(
        @Body request: BodyRequest
    ): Single<CustomerResultResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getItemResult(
        @Body request: BodyRequest
    ): Single<ItemResultResponse>
}