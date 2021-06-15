package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.timekeeping.model.SaveTimeKeepingResponse
import com.ziperp.dms.main.timekeeping.model.TimeKeepingListResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface TimeKeepingService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getTimeKeepingList(
        @Body request: BodyRequest
    ): Single<TimeKeepingListResponse>

    @POST(Constants.API.PATH_CRUD_FORM)
    fun saveTimeKeeping(
        @Body request: BodyRequest
    ): Single<SaveTimeKeepingResponse>
}