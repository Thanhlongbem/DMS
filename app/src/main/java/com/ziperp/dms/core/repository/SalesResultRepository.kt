package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.SalesResultService
import com.ziperp.dms.main.trackingreports.salesresult.model.SalesDetailResponse
import com.ziperp.dms.main.trackingreports.salesresult.model.SalesSummationResponse
import io.reactivex.Single

class SalesResultRepository(private val service: SalesResultService) {
    fun getSummationResult(request: BodyRequest): Single<SalesSummationResponse> {
        return service.getSummationResult(request)
    }

    fun getDetailResult(request: BodyRequest): Single<SalesDetailResponse> {
        return service.getDetailResult(request)
    }
}