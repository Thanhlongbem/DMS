package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.trackingreports.visitcustomer.model.TrackingVSDetailResponse
import com.ziperp.dms.main.trackingreports.visitcustomer.model.TrackingVSListResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface TrackingVSService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getTrackingVSList(
        @Body request: BodyRequest
    ): Single<TrackingVSListResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getTrackingVSDetail(
        @Body request: BodyRequest
    ): Single<TrackingVSDetailResponse>
}