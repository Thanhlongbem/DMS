package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.personalreport.visitdetail.model.NotVisitedResponse
import com.ziperp.dms.main.personalreport.visitdetail.model.VisitedDetailResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface VisitDetailService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getVisitedDetail(
        @Body request: BodyRequest
    ): Single<VisitedDetailResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getNotVisited(
        @Body request: BodyRequest
    ): Single<NotVisitedResponse>
}