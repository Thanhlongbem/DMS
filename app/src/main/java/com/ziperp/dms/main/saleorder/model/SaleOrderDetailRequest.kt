package com.ziperp.dms.main.saleorder.model

import com.google.gson.annotations.SerializedName

data class SaleOrderDetailRequest (
    @SerializedName("OrderNo")
    var orderNo: String = ""
)