package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.trackingreports.salesonroute.model.SalesOnRouteResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface SalesOnRouteService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getSalesOnRoute(
        @Body request: BodyRequest
    ): Single<SalesOnRouteResponse>
}