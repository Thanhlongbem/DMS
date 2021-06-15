package com.ziperp.dms.main.trackingreports.customernotorder.model


import com.google.gson.annotations.SerializedName

data class NewNotOrderRequest(
    @SerializedName("DeptCd")
    var deptCd: String = "",
    @SerializedName("TimeTracking")
    var timeTracking: String = "",
    @SerializedName("MasterLocCd")
    var masterLocCd: String = "",
    @SerializedName("CallFrmMobileApp")
    var callFrmMobileApp: String = "",
    @SerializedName("ModeGetData")
    var modeGetData: String = "",
    @SerializedName("PageNumber")
    var pageNumber: Int = 0,
    @SerializedName("RowspPage")
    var rowspPage: Int = 0
)