package com.ziperp.dms.main.saleorder.model


import com.google.gson.annotations.SerializedName

data class SaleOrderRequest(
    @SerializedName("OrderInfo")
    var orderInfo: String = "",
    @SerializedName("OrderDateFr")
    var orderDateFr: String = "",
    @SerializedName("OrderDateTo")
    var orderDateTo: String = "",
    @SerializedName("Salesman")
    var salesman: String = "",
    @SerializedName("SalesDept")
    var salesDept: String = "",
    @SerializedName("CstCd")
    var cstCd: String = "",// 003305: TODO: Remove when release
    @SerializedName("BusinessUnit")
    var businessUnit: String = "",
    @SerializedName("OrderStatus")
    var orderStatus: String = "",
    @SerializedName("PageNumber")
    var pageNumber: Int = 0,
    @SerializedName("RowspPage")
    var rowspPage: Int = 0
)