package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.TrackingStaffService
import com.ziperp.dms.main.trackingreports.staff.model.TrackingStaffDetailResponse
import com.ziperp.dms.main.trackingreports.staff.model.TrackingStaffListResponse
import io.reactivex.Single

class TrackingStaffRepository(private val service: TrackingStaffService) {
    fun getTrackingStaffList(request: BodyRequest): Single<TrackingStaffListResponse> {
        return service.getTrackingStaffList(request)
    }

    fun getTrackingStaffDetail(request: BodyRequest): Single<TrackingStaffDetailResponse> {
        return service.getTrackingStaffDetail(request)
    }
}