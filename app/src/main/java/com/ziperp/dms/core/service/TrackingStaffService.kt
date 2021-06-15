package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.trackingreports.staff.model.TrackingStaffDetailResponse
import com.ziperp.dms.main.trackingreports.staff.model.TrackingStaffListResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface TrackingStaffService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getTrackingStaffList(
        @Body request: BodyRequest
    ): Single<TrackingStaffListResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getTrackingStaffDetail(
        @Body request: BodyRequest
    ): Single<TrackingStaffDetailResponse>
}