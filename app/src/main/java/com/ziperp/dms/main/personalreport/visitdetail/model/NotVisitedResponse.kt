package com.ziperp.dms.main.personalreport.visitdetail.model


import com.google.gson.annotations.SerializedName

data class NotVisitedResponse(
    @SerializedName("Table")
    val data: List<NotVisitedCustomer> = listOf(),
    @SerializedName("Table1")
    val record: List<Table1> = listOf()
) {
    data class NotVisitedCustomer(
        @SerializedName("CstNm")
        val cstNm: String = "",
        @SerializedName("CstCd")
        val cstCd: String = "",
        @SerializedName("RepPerson")
        val repPerson: String = "",
        @SerializedName("RepPersonPhone")
        val repPersonPhone: String = "",
        @SerializedName("Address")
        val address: String = "",
        @SerializedName("LastVisitedBy")
        val lastVisitedBy: String = "",
        @SerializedName("LastTimeVisit")
        val lastTimeVisit: String = ""
    )

    data class Table1(
        @SerializedName("TotalRecords")
        val totalRecords: Double = 0.0
    )
}