package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.DailyPerformanceService
import com.ziperp.dms.main.trackingreports.dailyperformance.model.DailyPerformanceResponse
import io.reactivex.Single

class DailyPerformanceRepository(private val customerNoteService: DailyPerformanceService) {
    fun getDailyReport(request: BodyRequest): Single<DailyPerformanceResponse> {
        return customerNoteService.getDailyReport(request)
    }
}