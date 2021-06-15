package com.ziperp.dms.main.trackingreports.picture.model


import com.google.gson.annotations.SerializedName

data class TrackingPictureListResponse(
    @SerializedName("Table")
    val data: List<VisitedCustomerPicture> = listOf(),
    @SerializedName("Table1")
    val record: List<Table1> = listOf()
) {
    data class VisitedCustomerPicture(
        @SerializedName("StaffNo")
        val staffNo: String = "",
        @SerializedName("StaffId")
        val staffId: String = "",
        @SerializedName("StaffNm")
        val staffNm: String = "",
        @SerializedName("DeptNm")
        val deptNm: String = "",
        @SerializedName("MasterLocNm")
        val masterLocNm: String = "",
        @SerializedName("CstVisited")
        val cstVisited: Int = 0,
        @SerializedName("VisitWithImg")
        val visitWithImg: Int = 0,
        @SerializedName("VisitNoImg")
        val visitNoImg: Int = 0,
        @SerializedName("NumberImg")
        val numberImg: Int = 0,
        @SerializedName("Year")
        val year: String = ""
    )

    data class Table1(
        @SerializedName("TotalRecords")
        val totalRecords: Double = 0.0
    )
}