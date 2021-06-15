package com.ziperp.dms.main.salespricepromotion.model


import com.google.gson.annotations.SerializedName

data class PromotionRequest(
    @SerializedName("SearchInfo")
    var searchInfo: String = "",
    @SerializedName("Date")
    var date: String = "",
    @SerializedName("BusinessUnit")
    var businessUnit: String = "",
    @SerializedName("ActiveStatus")
    var activeStatus: String = "",
    @SerializedName("CstCd")
    var cstCd: String = "",
    @SerializedName("PageNumber")
    var pageNumber: Int = 1,
    @SerializedName("RowspPage")
    var rowspPage: Int = 50
)