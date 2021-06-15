package com.ziperp.dms.main.saleorder.model


import com.google.gson.annotations.SerializedName

data class ApplyPromotionResponse(
    @SerializedName("Table")
    val promotion: List<Table> = listOf()
) {
    data class Table(
        @SerializedName("OrderNo")
        val orderNo: String = "",
        @SerializedName("btnApplyPromotion_TKU")
        val btnApplyPromotionTKU: String = "",
        @SerializedName("ErrCd")
        val errCd: String = "",
        @SerializedName("ErrMsg")
        val errMsg: String = "",
        @SerializedName("Status")
        val status: String = ""
    )
}