package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.SalesOnRouteService
import com.ziperp.dms.main.trackingreports.salesonroute.model.SalesOnRouteResponse
import io.reactivex.Single

class SalesOnRouteRepository(private val service: SalesOnRouteService) {
    fun getSalesOnRoute(request: BodyRequest): Single<SalesOnRouteResponse> {
        return service.getSalesOnRoute(request)
    }
}