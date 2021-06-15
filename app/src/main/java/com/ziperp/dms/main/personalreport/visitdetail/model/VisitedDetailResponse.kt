package com.ziperp.dms.main.personalreport.visitdetail.model


import com.google.gson.annotations.SerializedName
import com.ziperp.dms.main.trackingreports.visitcustomer.model.TrackingVSDetail

data class VisitedDetailResponse(
    @SerializedName("Table")
    val data: List<TrackingVSDetail> = listOf(),
    @SerializedName("Table1")
    val record: List<Table1> = listOf()
) {
    data class Table1(
        @SerializedName("TotalRecords")
        val totalRecords: Double = 0.0,
        @SerializedName("TitleSummation")
        val titleSummation: String = ""
    )
}