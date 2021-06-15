package com.ziperp.dms.main.personalreport.salesresult.model


import com.google.gson.annotations.SerializedName
import com.ziperp.dms.core.token.DmsUserManager

data class YourSalesResultRequest(
    @SerializedName("StaffId")
    var staffId: String = DmsUserManager.userInfo.staffId,
    @SerializedName("StartDate")
    var startDate: String = "",
    @SerializedName("EndDate")
    var endDate: String = "",
    @SerializedName("ItemCate")
    var itemCate: String = "",
    @SerializedName("ItemCd")
    var itemCd: String = "",
    @SerializedName("ItemBrand")
    var itemBrand: String = "",
    @SerializedName("ItemModel")
    var itemModel: String = "",
    @SerializedName("CstCd")
    var cstCd: String = "",
    @SerializedName("AnalysisBasic")
    var analysisBasic: String = "",
    @SerializedName("PageNumber")
    var pageNumber: Int = 0,
    @SerializedName("RowspPage")
    var rowspPage: Int = 0
)