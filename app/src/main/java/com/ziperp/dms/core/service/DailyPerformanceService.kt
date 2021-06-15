package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.trackingreports.dailyperformance.model.DailyPerformanceResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface DailyPerformanceService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getDailyReport(
        @Body request: BodyRequest
    ): Single<DailyPerformanceResponse>
}