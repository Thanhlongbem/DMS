package com.ziperp.dms.main.trackingreports.dailyperformance.model


import com.google.gson.annotations.SerializedName

data class DailyPerformanceRequest(
    @SerializedName("DayTracking")
    var dayTracking: String = "",
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