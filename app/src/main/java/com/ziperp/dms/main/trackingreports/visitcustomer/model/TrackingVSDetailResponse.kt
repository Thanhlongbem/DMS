package com.ziperp.dms.main.trackingreports.visitcustomer.model


import com.google.gson.annotations.SerializedName

data class TrackingVSDetailResponse(
    @SerializedName("Table")
    val data: List<TrackingVSDetail> = listOf(),
    @SerializedName("Table1")
    val record: List<Table1> = listOf()
) {
    data class Table1(
        @SerializedName("TotalRecords")
        val totalRecords: Double = 0.0
    )
}

data class TrackingVSDetail(
    @SerializedName("CstVisitNo")
    val cstVisitNo: String = "",
    @SerializedName("CstNm")
    val cstNm: String = "",
    @SerializedName("OpenYn")
    val openYn: Int = 0,
    @SerializedName("OpenYnNm")
    val openYnNm: String = "",
    @SerializedName("VisitSts")
    val visitSts: Int = 0,
    @SerializedName("VisitStsNm")
    val visitStsNm: String = "",
    @SerializedName("RouteStsNm")
    val routeStsNm: String = "",
    @SerializedName("DurCheckIn")
    val durCheckIn: String = "",
    @SerializedName("Distance")
    val distance: String = "",
    @SerializedName("NumOrders")
    val numOrders: Int = 0,
    @SerializedName("AmountOrder")
    val amountOrder: Double = 0.0,
    @SerializedName("NumPhotos")
    val numPhotos: Int = 0,
    @SerializedName("VisitDay")
    val visitDay: String = "",
    @SerializedName("CheckInTime")
    val checkInTime: String = "",
    @SerializedName("CheckOutTime")
    val checkOutTime: String = "",
    @SerializedName("VisitPosNm")
    val visitPosNm: String = "",
    @SerializedName("BatteryPer")
    val batteryPer: Int = 0
)