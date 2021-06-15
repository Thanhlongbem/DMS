package com.ziperp.dms.main.personalreport.visitresult


import com.google.gson.annotations.SerializedName
import com.ziperp.dms.main.trackingreports.visitcustomer.model.TrackingVisitCustomer

data class VisitResultResponse(
    @SerializedName("Table")
    val data: List<TrackingVisitCustomer> = listOf(),
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