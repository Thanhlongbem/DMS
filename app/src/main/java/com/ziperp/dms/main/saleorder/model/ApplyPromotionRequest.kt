package com.ziperp.dms.main.saleorder.model


import com.google.gson.annotations.SerializedName

data class ApplyPromotionRequest(
    @SerializedName("OrderNo")
    val orderNo: String = "",
    @SerializedName("ApplyYn")
    val applyYn: Int = 0
)