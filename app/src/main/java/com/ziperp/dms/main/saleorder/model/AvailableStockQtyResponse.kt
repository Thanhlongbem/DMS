package com.ziperp.dms.main.saleorder.model


import com.google.gson.annotations.SerializedName

data class AvailableStockQtyResponse(
    @SerializedName("Table")
    val quantities: List<AvailableStockQty> = listOf()
) {
    data class AvailableStockQty(
        @SerializedName("RowNo")
        val rowNo: Int = 0,
        @SerializedName("SeqNo")
        val seqNo: String = "",
        @SerializedName("LocCd")
        val locCd: String = "",
        @SerializedName("PartnerCd")
        val partnerCd: String = "",
        @SerializedName("ItemCd")
        val itemCd: String = "",
        @SerializedName("LotSeriNo")
        val lotSeriNo: String = "",
        @SerializedName("AvailStkQty")
        val availStkQty: Double = 0.0
    )
}