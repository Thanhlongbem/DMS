package com.ziperp.dms.main.trackingreports.staff.model


import com.google.gson.annotations.SerializedName

data class TrackingStaffRequest(
    @SerializedName("TrackSearchInfo")
    var trackSearchInfo: String = "",
    @SerializedName("StaffId")
    var staffId: String = "",
    @SerializedName("DeptCd")
    var deptCd: String = "",
    @SerializedName("PositionSts")
    var positionSts: String = "",
    @SerializedName("TimeTracking")
    var timeTracking: String = "",
    @SerializedName("StrMasterLocCd")
    var strMasterLocCd: String = "",
    @SerializedName("CallFrmMobileApp")
    var callFrmMobileApp: String = "",
    @SerializedName("PageNumber")
    var pageNumber: Int = 0,
    @SerializedName("RowspPage")
    var rowspPage: Int = 0
)