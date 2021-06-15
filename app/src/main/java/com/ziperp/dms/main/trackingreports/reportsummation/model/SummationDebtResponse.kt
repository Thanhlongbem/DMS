package com.ziperp.dms.main.trackingreports.reportsummation.model


import com.google.gson.annotations.SerializedName

data class SummationDebtResponse(
    @SerializedName("Table")
    val data: List<SummationDebt> = listOf(),
    @SerializedName("Table1")
    val record: List<Table1> = listOf()
) {
    data class SummationDebt(
        @SerializedName("InvDate")
        val invDate: String = "",
        @SerializedName("TaxInvNo")
        val taxInvNo: String = "",
        @SerializedName("InvStatus")
        val invStatus: String = "",
        @SerializedName("InvStatusCode")
        val invStatusCode: Int = 0,
        @SerializedName("TotalAmtBc")
        val totalAmtBc: Double = 0.0,
        @SerializedName("CollectedBc")
        val collectedBc: Double = 0.0,
        @SerializedName("UncollectedBc")
        val uncollectedBc: Double = 0.0,
        @SerializedName("Salesman")
        val salesman: String = ""
    )

    data class Table1(
        @SerializedName("TotalRecords")
        val totalRecords: Double = 0.0
    )
}