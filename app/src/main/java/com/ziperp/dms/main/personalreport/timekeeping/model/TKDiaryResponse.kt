package com.ziperp.dms.main.personalreport.timekeeping.model


import com.google.gson.annotations.SerializedName

data class TKDiaryResponse(
    @SerializedName("Table")
    val data: List<TimeKeepingDiary> = listOf(),
    @SerializedName("Table1")
    val summary: List<Summary> = listOf()
) {
    data class TimeKeepingDiary(
        @SerializedName("Day")
        val day: String = "",
        @SerializedName("CheckinTime")
        val checkinTime: String = "",
        @SerializedName("NumbLate")
        val numbLate: String = "",
        @SerializedName("CheckoutTime")
        val checkoutTime: String = "",
        @SerializedName("NumbEarly")
        val numbEarly: String = "",
        @SerializedName("WorkDuration")
        val workDuration: String = ""
    )

    data class Summary(
        @SerializedName("TotWorkTime")
        val totWorkTime: String = "",
        @SerializedName("NumbLateTimes")
        val numbLateTimes: String = "",
        @SerializedName("NumbLeaveEarly")
        val numbLeaveEarly: String = ""
    )
}