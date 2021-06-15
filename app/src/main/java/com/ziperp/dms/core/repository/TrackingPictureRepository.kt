package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.TrackingPictureService
import com.ziperp.dms.main.trackingreports.picture.model.TrackingPictureDetailResponse
import com.ziperp.dms.main.trackingreports.picture.model.TrackingPictureListResponse
import io.reactivex.Single

class TrackingPictureRepository(private val service: TrackingPictureService) {
    fun getTrackingPictureList(request: BodyRequest): Single<TrackingPictureListResponse> {
        return service.getTrackingPictureList(request)
    }
    fun getTrackingPictureDetail(request: BodyRequest): Single<TrackingPictureDetailResponse> {
        return service.getTrackingPictureDetail(request)
    }
}