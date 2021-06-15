package com.ziperp.dms.main.trackingreports.picture.model


import com.google.gson.annotations.SerializedName

data class TrackingPictureDetailResponse(
    @SerializedName("Table")
    val data: List<Picture> = listOf(),
    @SerializedName("Table1")
    val record: List<Table1> = listOf()
) {
    data class Picture(
        @SerializedName("CstVisitNo")
        val cstVisitNo: String = "",
        @SerializedName("CstNm")
        val cstNm: String = "",
        @SerializedName("NumPhotos")
        val numPhotos: Int = 0,
        @SerializedName("CheckOutDay")
        val checkOutDay: String = "",
        @SerializedName("CheckOutTime")
        val checkOutTime: String = "",
        @SerializedName("FileURL")
        val fileURL: String = "",
        @SerializedName("FileNm")
        val fileNm: String = "",
        @SerializedName("FileNote")
        val fileNote: String = "",
        @SerializedName("FileLength")
        val fileLength: String = "",
        @SerializedName("AttachedDate")
        val attachedDate: String = "",
        @SerializedName("OpenFile")
        val openFile: String = ""
    )

    data class Table1(
        @SerializedName("TotalRecords")
        val totalRecords: Double = 0.0
    )
}