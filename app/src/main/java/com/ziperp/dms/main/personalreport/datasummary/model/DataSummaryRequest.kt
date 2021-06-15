package com.ziperp.dms.main.personalreport.datasummary.model


import com.google.gson.annotations.SerializedName

data class DataSummaryRequest(
    @SerializedName("StaffId")
    var staffId: String = "",
    @SerializedName("StartDate")
    var startDate: String = "",
    @SerializedName("EndDate")
    var endDate: String = ""
)