package com.ziperp.dms.main.salespricepromotion.model


import com.google.gson.annotations.SerializedName

data class PromotionInfoRequest(
    @SerializedName("PromoCd")
    var promoCode: String = ""
)