package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.TrackingVSService
import com.ziperp.dms.main.trackingreports.visitcustomer.model.TrackingVSDetailResponse
import com.ziperp.dms.main.trackingreports.visitcustomer.model.TrackingVSListResponse
import io.reactivex.Single

class TrackingVSRepository(private val service: TrackingVSService) {
    fun getTrackingVSList(request: BodyRequest): Single<TrackingVSListResponse> {
        return service.getTrackingVSList(request)
    }
    fun getTrackingVSDetail(request: BodyRequest): Single<TrackingVSDetailResponse> {
        return service.getTrackingVSDetail(request)
    }
}