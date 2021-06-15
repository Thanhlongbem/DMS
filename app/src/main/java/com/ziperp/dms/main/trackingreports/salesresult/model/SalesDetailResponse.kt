package com.ziperp.dms.main.trackingreports.salesresult.model


import com.google.gson.annotations.SerializedName

data class SalesDetailResponse(
    @SerializedName("Table")
    val data: List<SalesDetail> = listOf(),
    @SerializedName("Table1")
    val record: List<Table1> = listOf()
) {
    data class SalesDetail(
        @SerializedName("StaffNm")
        val staffNm: String = "",
        @SerializedName("ItemNm")
        val itemNm: String = "",
        @SerializedName("UoMNm")
        val uoMNm: String = "",
        @SerializedName("Quantity")
        val quantity: Double = 0.0,
        @SerializedName("TotAmount")
        val totAmount: Double = 0.0,
        @SerializedName("ImgProfile")
        val imgProfile: String = "",
        @SerializedName("StaffId")
        val staffId: String = ""
    )

    data class Table1(
        @SerializedName("TotalRecords")
        val totalRecords: Int = 0
    )
}