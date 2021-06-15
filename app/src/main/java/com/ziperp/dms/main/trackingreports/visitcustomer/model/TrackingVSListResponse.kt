package com.ziperp.dms.main.trackingreports.visitcustomer.model


import com.google.gson.annotations.SerializedName

data class TrackingVSListResponse(
    @SerializedName("Table")
    val data: List<TrackingVisitCustomer> = listOf(),
    @SerializedName("Table1")
    val record: List<Table1> = listOf()
) {
    data class Table1(
        @SerializedName("TotalRecords")
        val totalRecords: Double = 0.0
    )
}

data class TrackingVisitCustomer(
    @SerializedName("StaffNo")
    val staffNo: String = "",
    @SerializedName("StaffId")
    val staffId: String = "",
    @SerializedName("StaffNm")
    val staffNm: String = "",
    @SerializedName("DeptNm")
    val deptNm: String = "",
    @SerializedName("MasterLocNm")
    val masterLocNm: String = "",
    @SerializedName("CstMustVisit")
    val cstMustVisit: Int = 0,
    @SerializedName("NotVisit")
    val notVisit: Int = 0,
    @SerializedName("NumbVisitValid")
    val numbVisitValid: Int = 0,
    @SerializedName("NumbVisitInvalid")
    val numbVisitInvalid: Int = 0,
    @SerializedName("VisitWithOrder")
    val visitWithOrder: Int = 0,
    @SerializedName("VisitNoOrder")
    val visitNoOrder: Int = 0,
    @SerializedName("VstOrderOnRoute")
    val vstOrderOnRoute: Int = 0,
    @SerializedName("VstOrderWrongRoute")
    val vstOrderWrongRoute: Int = 0,
    @SerializedName("VisitWithImg")
    val visitWithImg: Int = 0,
    @SerializedName("VisitNoImg")
    val visitNoImg: Int = 0,
    @SerializedName("VisitOnRoute")
    val visitOnRoute: Int = 0,
    @SerializedName("VisitNotOnRoute")
    val visitNotOnRoute: Int = 0,
    @SerializedName("VisitWithCount")
    val visitWithCount: Int = 0,
    @SerializedName("VisitNoCount")
    val visitNoCount: Int = 0,
    @SerializedName("Year")
    val year: String = ""
)