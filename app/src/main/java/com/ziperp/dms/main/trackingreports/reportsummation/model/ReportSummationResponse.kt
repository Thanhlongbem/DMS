package com.ziperp.dms.main.trackingreports.reportsummation.model


import com.google.gson.annotations.SerializedName

data class ReportSummationResponse(
    @SerializedName("Table")
    val data: List<ReportSummation> = listOf()
) {
    data class ReportSummation(
        @SerializedName("CustomerName")
        val customerName: String = "",
        @SerializedName("VisitBegin")
        val visitBegin: Int = 0,
        @SerializedName("VisitInPeriod")
        val visitInPeriod: Int = 0,
        @SerializedName("VisitEnd")
        val visitEnd: Int = 0,
        @SerializedName("OrderBegin")
        val orderBegin: Int = 0,
        @SerializedName("OrderInPeriod")
        val orderInPeriod: Int = 0,
        @SerializedName("OrderEnd")
        val orderEnd: Int = 0,
        @SerializedName("QtyBegin")
        val qtyBegin: Double = 0.0,
        @SerializedName("QtyInPeriod")
        val qtyInPeriod: Double = 0.0,
        @SerializedName("QtyEnd")
        val qtyEnd: Double = 0.0,
        @SerializedName("AmountBegin")
        val amountBegin: Double = 0.0,
        @SerializedName("AmountInPeriod")
        val amountInPeriod: Double = 0.0,
        @SerializedName("AmountEnd")
        val amountEnd: Double = 0.0,
        @SerializedName("DebtBegin")
        val debtBegin: Double = 0.0,
        @SerializedName("DebtInPeriod")
        val debtInPeriod: Double = 0.0,
        @SerializedName("DebtEnd")
        val debtEnd: Double = 0.0
    )
}