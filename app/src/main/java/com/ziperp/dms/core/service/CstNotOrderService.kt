package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.trackingreports.customernotorder.model.CustomerNotOrderResponse
import com.ziperp.dms.main.trackingreports.customernotorder.model.NewCustomerResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface CstNotOrderService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getNewCustomer(
        @Body request: BodyRequest
    ): Single<NewCustomerResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getCustomerNotOrder(
        @Body request: BodyRequest
    ): Single<CustomerNotOrderResponse>
}