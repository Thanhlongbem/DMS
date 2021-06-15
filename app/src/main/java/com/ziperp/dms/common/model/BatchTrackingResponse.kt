package com.ziperp.dms.common.model


import com.google.gson.annotations.SerializedName

data class BatchTrackingResponse(
    @SerializedName("Table")
    val table: List<Table> = listOf()
) {
    data class Table(
        @SerializedName("StaffId")
        val staffId: String = "",
        @SerializedName("TimeLogPos")
        val timeLogPos: String = "",
        @SerializedName("TypeCheckin")
        val typeCheckin: String = "",
        @SerializedName("PosName")
        val posName: String = "",
        @SerializedName("DeviceName")
        val deviceName: String = "",
        @SerializedName("LatPos")
        val latPos: String = "",
        @SerializedName("LngPos")
        val lngPos: String = "",
        @SerializedName("MoveSts")
        val moveSts: Int = 0,
        @SerializedName("BatteryPer")
        val batteryPer: Int = 0,
        @SerializedName("ErrMsg")
        val errMsg: String = "",
        @SerializedName("MsgId")
        val msgId: String = "",
        @SerializedName("Status")
        val status: String = ""
    )
}