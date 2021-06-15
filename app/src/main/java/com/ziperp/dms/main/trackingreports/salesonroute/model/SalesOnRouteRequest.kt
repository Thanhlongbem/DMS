package com.ziperp.dms.main.trackingreports.salesonroute.model


import com.google.gson.annotations.SerializedName

data class SalesOnRouteRequest(
    @SerializedName("MonthTracking")
    var monthTracking: String = "",
    @SerializedName("StaffId")
    var staffId: String = "",
    @SerializedName("DeptCd")
    var deptCd: String = "",
    @SerializedName("MasterLocCd")
    var masterLocCd: String = "",
    @SerializedName("DayVisit")
    var dayVisit: String = "",
    @SerializedName("PageNumber")
    var pageNumber: Int = 1,
    @SerializedName("RowspPage")
    var rowspPage: Int = 50
)