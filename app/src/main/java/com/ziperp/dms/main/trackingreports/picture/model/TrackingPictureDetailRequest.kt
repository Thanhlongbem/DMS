package com.ziperp.dms.main.trackingreports.picture.model


import com.google.gson.annotations.SerializedName

data class TrackingPictureDetailRequest(
    @SerializedName("StaffId")
    var staffId: String = "",
    @SerializedName("TimeTracking")
    var timeTracking: String = "",
    @SerializedName("CallFrmMobileApp")
    var callFrmMobileApp: String = "",
    @SerializedName("PageNumber")
    var pageNumber: Int = 0,
    @SerializedName("RowspPage")
    var rowspPage: Int = 0
)