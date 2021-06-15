package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.common.table.model.SaleReportResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface SaleReportService {

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getSaleReport(
        @Body request: BodyRequest
    ): Single<SaleReportResponse>
}
