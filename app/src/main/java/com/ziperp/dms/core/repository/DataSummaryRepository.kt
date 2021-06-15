package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.DataSummaryService
import com.ziperp.dms.main.personalreport.datasummary.model.DataSummaryResponse
import io.reactivex.Single

class DataSummaryRepository(private val service: DataSummaryService) {
    fun getDataSummary(request: BodyRequest): Single<DataSummaryResponse> {
        return service.getDataSummary(request)
    }
}