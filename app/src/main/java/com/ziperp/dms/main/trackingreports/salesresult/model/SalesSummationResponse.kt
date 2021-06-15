package com.ziperp.dms.main.trackingreports.salesresult.model


import com.google.gson.annotations.SerializedName

data class SalesSummationResponse(
    @SerializedName("Table")
    val data: List<Staff> = listOf(),
    @SerializedName("Table1")
    val record: List<Table1> = listOf()
) {
    data class Staff(
        @SerializedName("StaffNm")
        val staffNm: String = "",
        @SerializedName("DeptNm")
        val deptNm: String = "",
        @SerializedName("BusinessUnit")
        val businessUnit: String = "",
        @SerializedName("TotalQty")
        val totalQty: Double = 0.0,
        @SerializedName("TotalAmt")
        val totalAmt: Double = 0.0,
        @SerializedName("TotSalesOrder")
        val totSalesOrder: Int = 0,
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