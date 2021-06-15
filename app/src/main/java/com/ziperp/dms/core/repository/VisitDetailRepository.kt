package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.VisitDetailService
import com.ziperp.dms.main.personalreport.visitdetail.model.NotVisitedResponse
import com.ziperp.dms.main.personalreport.visitdetail.model.VisitedDetailResponse
import io.reactivex.Single

class VisitDetailRepository(private val service: VisitDetailService) {
    fun getVisitedDetail(request: BodyRequest): Single<VisitedDetailResponse> {
        return service.getVisitedDetail(request)
    }
    fun getNotVisited(request: BodyRequest): Single<NotVisitedResponse> {
        return service.getNotVisited(request)
    }
}