package com.ziperp.dms.main.salespricepromotion.model


import com.google.gson.annotations.SerializedName

data class SalesPriceInfoRequest(
    @SerializedName("PriceListCd")
    val priceListCd: String = ""
)