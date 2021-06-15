package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.NewSalesPointService
import com.ziperp.dms.main.trackingreports.newpoint.model.NewSalesPointResponse
import io.reactivex.Single

class NewSalesPointRepository(private val service: NewSalesPointService) {
    fun getSalesPoint(request: BodyRequest): Single<NewSalesPointResponse> {
        return service.getSalesPoint(request)
    }
}