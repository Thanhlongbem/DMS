package com.ziperp.dms.main.trackingreports.reportsummation.model


import com.google.gson.annotations.SerializedName

data class SummationStockResponse(
    @SerializedName("Table")
    val data: List<SummationStock> = listOf(),
    @SerializedName("Table1")
    val record: List<Table1> = listOf()
) {
    data class SummationStock(
        @SerializedName("ItemNm")
        val itemNm: String = "",
        @SerializedName("StkUoMNm")
        val stkUoMNm: String = "",
        @SerializedName("StkCntQty")
        val stkCntQty: Double = 0.0,
        @SerializedName("LotNo")
        val lotNo: String = "",
        @SerializedName("SerialNo")
        val serialNo: String = "",
        @SerializedName("MfgDate")
        val mfgDate: String = "",
        @SerializedName("ExpiryDate")
        val expiryDate: String = "",
        @SerializedName("Remark")
        val remark: String = ""
    )

    data class Table1(
        @SerializedName("TotalRecords")
        val totalRecords: Double = 0.0
    )
}