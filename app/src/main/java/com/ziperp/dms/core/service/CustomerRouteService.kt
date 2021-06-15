package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.customer.customerroute.model.AddRouteResponse
import com.ziperp.dms.main.customer.customerroute.model.CustomerRouteResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomerRouteService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getRouteList(
        @Body request: BodyRequest
    ): Single<CustomerRouteResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    suspend fun addRoute(
        @Body request: BodyRequest
    ): AddRouteResponse

    @POST(Constants.API.PATH_QUERY_DATA)
    fun addRoute2(
        @Body request: BodyRequest
    ): Single<AddRouteResponse>
}

