package com.ziperp.dms.main.trackingreports.staff.model


import com.google.gson.annotations.SerializedName

data class TrackingStaffListResponse(
    @SerializedName("Table")
    val listStaff: List<Staff> = listOf(),
    @SerializedName("Table1")
    val record: List<Record> = listOf()
) {
    data class Staff(
        @SerializedName("StaffNm")
        val staffNm: String = "",
        @SerializedName("DeptNm")
        val deptNm: String = "",
        @SerializedName("MasterLocNm")
        val masterLocNm: String = "",
        @SerializedName("StaffId")
        val staffId: String = "",
        @SerializedName("LogPosTime")
        val logPosTime: String = "",
        @SerializedName("TimeLogPos")
        val timeLogPos: String = "",
        @SerializedName("TypeCheckin")
        val typeCheckin: String = "",
        @SerializedName("TypeCheckinNm")
        val typeCheckinNm: String = "",
        @SerializedName("Color")
        val color: String = "",
        @SerializedName("PosName")
        val posName: String = "",
        @SerializedName("LatPos")
        val latPos: String = "",
        @SerializedName("LngPos")
        val lngPos: String = "",
        @SerializedName("MoveSts")
        val moveSts: String = "",
        @SerializedName("BatteryPer")
        val batteryPer: String = "",
        @SerializedName("StaffSts")
        val staffSts: Int = 0,
        @SerializedName("ImgProfile")
        val imgProfile: String = "",
        @SerializedName("StaffTrackDetail")
        val staffTrackDetail: String = "",
        @SerializedName("PlanVisitQty")
        val planVisitQty: Int = 0,
        @SerializedName("ActVistQty")
        val actVistQty: Int = 0,
        @SerializedName("PlanSalesAmt")
        val planSalesAmt: Double = 0.0,
        @SerializedName("ActSalesAmt")
        val actSalesAmt: Double = 0.0,
        @SerializedName("PlanSOQty")
        val planSOQty: Int = 0,
        @SerializedName("ActSQQty")
        val actSQQty: Int = 0
    )

    data class Record(
        @SerializedName("TotalRecords")
        val totalRecords: Double = 0.0
    )
}