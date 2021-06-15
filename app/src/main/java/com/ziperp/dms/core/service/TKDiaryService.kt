package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.personalreport.timekeeping.model.TKDiaryResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface TKDiaryService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getTimeKeepingDiary(
        @Body request: BodyRequest
    ): Single<TKDiaryResponse>
}