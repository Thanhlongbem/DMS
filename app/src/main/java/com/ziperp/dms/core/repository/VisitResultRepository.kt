package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.VisitResultService
import com.ziperp.dms.main.personalreport.visitresult.VisitResultResponse
import io.reactivex.Single

class VisitResultRepository(private val service: VisitResultService) {
    fun getVisitResultSummation(request: BodyRequest): Single<VisitResultResponse> {
        return service.getVisitResultSummation(request)
    }
}