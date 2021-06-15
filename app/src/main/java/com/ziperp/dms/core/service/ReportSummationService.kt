package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.trackingreports.reportsummation.model.ReportSummationResponse
import com.ziperp.dms.main.trackingreports.reportsummation.model.SummationDebtResponse
import com.ziperp.dms.main.trackingreports.reportsummation.model.SummationOrderResponse
import com.ziperp.dms.main.trackingreports.reportsummation.model.SummationStockResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportSummationService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getReportSummation(
        @Body request: BodyRequest
    ): Single<ReportSummationResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getSummationDebt(
        @Body request: BodyRequest
    ): Single<SummationDebtResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getSummationOrder(
        @Body request: BodyRequest
    ): Single<SummationOrderResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getSummationStock(
        @Body request: BodyRequest
    ): Single<SummationStockResponse>

}