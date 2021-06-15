package com.ziperp.dms.main.trackingreports.salesresult.model


import com.google.gson.annotations.SerializedName

data class SalesResultRequest(
    @SerializedName("SearchInfo")
    var  searchInfo: String = "",
    @SerializedName("StartMonth")
    var  startMonth: String = "",
    @SerializedName("EndMonth")
    var  endMonth: String = "",
    @SerializedName("ItemCate")
    var  itemCate: String = "",
    @SerializedName("ItemCd")
    var  itemCd: String = "",
    @SerializedName("ItemBrand")
    var  itemBrand: String = "",
    @SerializedName("ItemModel")
    var  itemModel: String = "",
    @SerializedName("DeptCd")
    var  deptCd: String = "",
    @SerializedName("MasterLocCd")
    var  masterLocCd: String = "",
    @SerializedName("AnalysisBasic")
    var  analysisBasic: String = "",
    @SerializedName("PageNumber")
    var  pageNumber: Int = 0,
    @SerializedName("RowspPage")
    var  rowspPage: Int = 0
)