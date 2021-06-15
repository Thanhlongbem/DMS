package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.TKDiaryService
import com.ziperp.dms.main.personalreport.timekeeping.model.TKDiaryResponse
import io.reactivex.Single

class TKDiaryRepository(private val service: TKDiaryService) {
    fun getTimeKeepingDiary(request: BodyRequest): Single<TKDiaryResponse> {
        return service.getTimeKeepingDiary(request)
    }
}