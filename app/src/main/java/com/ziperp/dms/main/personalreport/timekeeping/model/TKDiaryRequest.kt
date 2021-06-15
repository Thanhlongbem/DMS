package com.ziperp.dms.main.personalreport.timekeeping.model


import com.google.gson.annotations.SerializedName

data class TKDiaryRequest(
    @SerializedName("StaffId")
    var staffId: String = "",
    @SerializedName("Month")
    var month: String = ""
)