package com.ziperp.dms.main.trackingreports.staff.model

data class StaffMarker (
    val staffID: String,
    val lat: Double,
    val lng: Double,
    val staffNm: String,
    val logPosTime: String,
    val checkInType: String
): Comparable<StaffMarker> {
    override fun compareTo(other: StaffMarker): Int {
        return compareValues(other.staffID, staffID)
    }
}