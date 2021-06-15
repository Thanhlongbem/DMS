package com.ziperp.dms.main.saleorder.model


import com.google.gson.annotations.SerializedName

data class ConfirmSaleOrderResponse(
    @SerializedName("Table")
    val confirms: List<ConfirmSaleOrder> = listOf()
) {
    data class ConfirmSaleOrder(
        @SerializedName("OrderNoCfm")
        val orderNoCfm: String = "",
        @SerializedName("Confirmer")
        val confirmer: String = "",
        @SerializedName("OrderStatus")
        val orderStatus: String = "",
        @SerializedName("ConfirmDate")
        val confirmDate: String = "",
        @SerializedName("btnCfmSO")
        val btnCfmSO: String = "",
        @SerializedName("ErrCd")
        val errCd: String = "",
        @SerializedName("ErrMsg")
        val errMsg: String = "",
        @SerializedName("Status")
        val status: String = ""
    )
}