package com.ziperp.dms.main.personalreport.salesresult.model


import com.google.gson.annotations.SerializedName

data class CustomerResultResponse(
    @SerializedName("Table")
    val data: List<CustomerResult> = listOf(),
    @SerializedName("Table1")
    val record: List<Table1> = listOf()
) {
    data class CustomerResult(
        @SerializedName("CustomerNm")
        val customerNm: String = "",
        @SerializedName("Quantity")
        val quantity: Double = 0.0,
        @SerializedName("TotAmount")
        val totAmount: Double = 0.0
    )

    data class Table1(
        @SerializedName("TotalRecords")
        val totalRecords: Int = 0
    )
}