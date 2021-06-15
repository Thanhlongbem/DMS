package com.ziperp.dms.main.trackingreports.staff.model


import com.google.gson.annotations.SerializedName

data class TrackingStaffDetailRequest(
    @SerializedName("TimeTracking")
    var timeTracking: String = "",
    @SerializedName("StaffId")
    var staffId: String = "",
    @SerializedName("CallFrmMobileApp")
    var callFrmMobileApp: String = "",
    @SerializedName("PageNumber")
    var pageNumber: Int = 0,
    @SerializedName("RowspPage")
    var rowspPage: Int = 0
)