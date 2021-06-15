package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.ReportSummationService
import com.ziperp.dms.main.trackingreports.reportsummation.model.ReportSummationResponse
import com.ziperp.dms.main.trackingreports.reportsummation.model.SummationDebtResponse
import com.ziperp.dms.main.trackingreports.reportsummation.model.SummationOrderResponse
import com.ziperp.dms.main.trackingreports.reportsummation.model.SummationStockResponse
import io.reactivex.Single

class ReportSummationRepository(private val service: ReportSummationService) {
    fun getReportSummation(request: BodyRequest): Single<ReportSummationResponse> {
        return service.getReportSummation(request)
    }

    fun getSummationDebt(request: BodyRequest): Single<SummationDebtResponse> {
        return service.getSummationDebt(request)
    }

    fun getSummationOrder(request: BodyRequest): Single<SummationOrderResponse> {
        return service.getSummationOrder(request)
    }

    fun getSummationStock(request: BodyRequest): Single<SummationStockResponse> {
        return service.getSummationStock(request)
    }

}