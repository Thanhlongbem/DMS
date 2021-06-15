package com.ziperp.dms.main.trackingreports.customernotorder.model


import com.google.gson.annotations.SerializedName

data class CustomerNotOrderResponse(
    @SerializedName("Table")
    val data: List<CustomerNotOrder> = listOf(),
    @SerializedName("Table1")
    val record: List<Table1> = listOf()
) {
    data class CustomerNotOrder(
        @SerializedName("CstNm")
        val cstNm: String = "",
        @SerializedName("RegMan")
        val regMan: String = "",
        @SerializedName("CstOwner")
        val cstOwner: String = "",
        @SerializedName("RegDate")
        val regDate: Any = Any(),
        @SerializedName("Address")
        val address: String = "",
        @SerializedName("CustomerGrp1")
        val customerGrp1: String = "",
        @SerializedName("CustomerGrp2")
        val customerGrp2: String = "",
        @SerializedName("CstCd")
        val cstCd: String = "",
        @SerializedName("LastOrderDate")
        val lastOrderDate: String = "",
        @SerializedName("Salesman")
        val salesman: String = ""
    )

    data class Table1(
        @SerializedName("TotalRecords")
        val totalRecords: Int = 0,
        @SerializedName("TitleSummation")
        val titleSummation: String = ""
    )
}