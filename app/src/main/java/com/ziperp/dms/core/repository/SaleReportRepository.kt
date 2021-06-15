package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.SaleReportService
import com.ziperp.dms.common.table.model.SaleReportResponse
import io.reactivex.Single

class SaleReportRepository(private val saleReportService: SaleReportService) {

    fun getSaleReport(
        request: BodyRequest
    ): Single<SaleReportResponse> {
        return saleReportService.getSaleReport(request)
    }

}