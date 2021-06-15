package com.ziperp.dms.main.personalreport.visitdetail.model


import com.google.gson.annotations.SerializedName

data class VisitDetailRequest(
    @SerializedName("StaffId")
    var staffId: String = "",
    @SerializedName("TimeTracking")
    var timeTracking: String = "1",
    @SerializedName("CallFrmMobileApp")
    var callFrmMobileApp: String = "1",
    @SerializedName("PageNumber")
    var pageNumber: Int = 0,
    @SerializedName("RowspPage")
    var rowspPage: Int = 0
)