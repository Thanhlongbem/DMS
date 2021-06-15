package com.ziperp.dms.main.trackingreports.newpoint.model


import com.google.gson.annotations.SerializedName

data class NewSalesPointRequest(
    @SerializedName("MonthTracking")
    var monthTracking: String = "",
    @SerializedName("StaffId")
    var staffId: String = "",
    @SerializedName("DeptCd")
    var deptCd: String = "",
    @SerializedName("MasterLocCd")
    var masterLocCd: String = "",
    @SerializedName("PageNumber")
    var pageNumber: Int = 0,
    @SerializedName("RowspPage")
    var rowspPage: Int = 0
)