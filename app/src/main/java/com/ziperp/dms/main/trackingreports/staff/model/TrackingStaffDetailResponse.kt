package com.ziperp.dms.main.trackingreports.staff.model


import com.google.gson.annotations.SerializedName

data class TrackingStaffDetailResponse(
    @SerializedName("Table")
    val data: List<StaffDetail> = listOf(),
    @SerializedName("Table1")
    val record: List<Table1> = listOf()
) {
    data class StaffDetail(
        @SerializedName("TimeLogPos")
        val timeLogPos: String = "",
        @SerializedName("LogPosTime")
        val logPosTime: String = "",
        @SerializedName("TypeCheckin")
        val typeCheckin: String = "",
        @SerializedName("TypeCheckinNm")
        val typeCheckinNm: String = "",
        @SerializedName("CstNm")
        val cstNm: String = "",
        @SerializedName("PosName")
        val posName: String = "",
        @SerializedName("BatteryPer")
        val batteryPer: String = "",
        @SerializedName("CstVisitNo")
        val cstVisitNo: String = "",
        @SerializedName("Remark")
        val remark: String = ""
    )

    data class Table1(
        @SerializedName("TotalRecords")
        val totalRecords: Double = 0.0
    )
}