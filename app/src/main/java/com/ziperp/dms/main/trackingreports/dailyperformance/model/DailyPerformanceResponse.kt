package com.ziperp.dms.main.trackingreports.dailyperformance.model


import com.google.gson.annotations.SerializedName

data class DailyPerformanceResponse(
    @SerializedName("Table")
    val data: List<DailyReport> = listOf()
) {
    data class DailyReport(
        @SerializedName("TypeObjc")
        val typeObjc: String = "",
        @SerializedName("ObjctNm")
        val objctNm: String = "",
        @SerializedName("NumbVisit")
        val numbVisit: String = "",
        @SerializedName("NumbOrder")
        val numbOrder: String = "",
        @SerializedName("OrderQty")
        val orderQty: String = "",
        @SerializedName("NumbMorningVist")
        val numbMorningVist: String = "",
        @SerializedName("NumbAfternoonVisit")
        val numbAfternoonVisit: String = "",
        @SerializedName("Distance")
        val distance: String = "",
        @SerializedName("ParentCd")
        val parentCd: String = ""
    )
}