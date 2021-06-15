package com.ziperp.dms.main.trackingreports.reportsummation.model


import com.google.gson.annotations.SerializedName

data class SummationOrderResponse(
    @SerializedName("Table")
    val data: List<SummationOrder> = listOf(),
    @SerializedName("Table1")
    val record: List<Table1> = listOf()
) {
    data class SummationOrder(
        @SerializedName("OrderDate")
        val orderDate: String = "",
        @SerializedName("OrderNo")
        val orderNo: String = "",
        @SerializedName("OrderStatusNm")
        val orderStatusNm: String = "",
        @SerializedName("OrderStatus")
        val orderStatus: Int = 0,
        @SerializedName("Currency")
        val currency: String = "",
        @SerializedName("TotalQty")
        val totalQty: Double = 0.0,
        @SerializedName("TotalAmt")
        val totalAmt: Double = 0.0,
        @SerializedName("Salesman")
        val salesman: String = "",
        @SerializedName("Remark")
        val remark: String = ""
    )

    data class Table1(
        @SerializedName("TotalRecords")
        val totalRecords: Double = 0.0
    )
}