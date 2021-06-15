package com.ziperp.dms.core.service

import com.ziperp.dms.common.model.BatchTrackingResponse
import com.ziperp.dms.common.model.TrackingResponse
import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface TrackingService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun postTrackingLocation(
        @Body request: BodyRequest
    ): Single<TrackingResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun postBatchTrackingLocation(
        @Body request: BodyRequest
    ): Single<BatchTrackingResponse>
}