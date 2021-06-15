package com.ziperp.dms.main.trackingreports.newpoint.model


import com.google.gson.annotations.SerializedName

data class NewSalesPointResponse(
    @SerializedName("Table")
    val data: List<SalesPoint> = listOf()
) {
    data class SalesPoint(
        @SerializedName("txtGroupNm")
        val txtGroupNm: String = "",
        @SerializedName("intBeginQty")
        val intBeginQty: String = "",
        @SerializedName("intNewQty")
        val intNewQty: String = "",
        @SerializedName("intEndQty")
        val intEndQty: String = ""
    )
}