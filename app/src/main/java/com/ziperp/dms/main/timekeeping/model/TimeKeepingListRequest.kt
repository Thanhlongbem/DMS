package com.ziperp.dms.main.timekeeping.model


import com.google.gson.annotations.SerializedName

data class TimeKeepingListRequest(
    @SerializedName("StaffId")
    var staffId: String = "",
    @SerializedName("TimeKeepDay")
    var timeKeepDay: String = ""
)