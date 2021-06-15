package com.ziperp.dms.main.trackingreports.reportsummation.model


import com.google.gson.annotations.SerializedName

data class ReportSummationRequest(
    @SerializedName("StartDate")
    var startDate: String = "20210101",
    @SerializedName("EndDate")
    var endDate: String = "20211231",
    @SerializedName("CstCd")
    var cstCd: String = "",
    @SerializedName("PageNumber")
    var pageNumber: Int = 1,
    @SerializedName("RowspPage")
    var rowspPage: Int = 50
)