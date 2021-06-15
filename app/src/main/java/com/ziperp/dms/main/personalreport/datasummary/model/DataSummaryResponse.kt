package com.ziperp.dms.main.personalreport.datasummary.model


import com.google.gson.annotations.SerializedName

data class DataSummaryResponse(
    @SerializedName("Table")
    val data: List<DataSummary> = listOf()
) {
    data class DataSummary(
        @SerializedName("NewCstBeginMonth")
        val newCstBeginMonth: Int = 0,
        @SerializedName("NewCstInPeriod")
        val newCstInPeriod: Int = 0,
        @SerializedName("NewCstEndMonth")
        val newCstEndMonth: Int = 0,
        @SerializedName("NumSalesOrder")
        val numSalesOrder: Int = 0,
        @SerializedName("TotalQuantity")
        val totalQuantity: Double = 0.0,
        @SerializedName("TotalAmount")
        val totalAmount: Double = 0.0,
        @SerializedName("NumbCustomerOnRoute")
        val numbCustomerOnRoute: Int = 0,
        @SerializedName("NumbCustomerVisited")
        val numbCustomerVisited: Int = 0,
        @SerializedName("NumbCustomerNotVisit")
        val numbCustomerNotVisit: Int = 0,
        @SerializedName("NumbVisitTimes")
        val numbVisitTimes: Int = 0,
        @SerializedName("VisitRightRoute")
        val visitRightRoute: Int = 0,
        @SerializedName("VisitWrongRoute")
        val visitWrongRoute: Int = 0,
        @SerializedName("VisitRightLoc")
        val visitRightLoc: Int = 0,
        @SerializedName("VisitWrongLoc")
        val visitWrongLoc: Int = 0,
        @SerializedName("VisitWithOrder")
        val visitWithOrder: Int = 0,
        @SerializedName("MorningVisit")
        val morningVisit: Int = 0,
        @SerializedName("AfternoonVisit")
        val afternoonVisit: Int = 0,
        @SerializedName("MoveDistance")
        val moveDistance: Double = 0.0
    )
}