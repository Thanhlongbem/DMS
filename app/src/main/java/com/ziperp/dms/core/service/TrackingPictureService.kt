package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.trackingreports.picture.model.TrackingPictureDetailResponse
import com.ziperp.dms.main.trackingreports.picture.model.TrackingPictureListResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface TrackingPictureService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getTrackingPictureList(
        @Body request: BodyRequest
    ): Single<TrackingPictureListResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getTrackingPictureDetail(
        @Body request: BodyRequest
    ): Single<TrackingPictureDetailResponse>
}