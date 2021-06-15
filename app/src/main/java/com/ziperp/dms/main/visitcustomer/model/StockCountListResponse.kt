package com.ziperp.dms.main.visitcustomer.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StockCountListResponse(
    @SerializedName("Table")
    var data: List<StockCount> = listOf()
) {
    data class StockCount(
        @SerializedName("CstVisitNo")
        var cstVisitNo: String = "",
        @SerializedName("CstVisitLineNo")
        var cstVisitLineNo: String = "",
        @SerializedName("ItemCd")
        var itemCd: String = "",
        @SerializedName("ItemNm")
        var itemNm: String = "",
        @SerializedName("ItemNo")
        var itemNo: String = "",
        @SerializedName("CntUoMCd")
        var cntUoMCd: String = "",
        @SerializedName("CntUoMNm")
        var cntUoMNm: String = "",
        @SerializedName("CntQty")
        var cntQty: Double = 0.0,
        @SerializedName("StkUoMCd")
        var stkUoMCd: String = "",
        @SerializedName("StkUoMNm")
        var stkUoMNm: String = "",
        @SerializedName("StkCntQty")
        var stkCntQty: Double = 0.0,
        @SerializedName("ConvQty1")
        var convQty1: Double = 0.0,
        @SerializedName("ConvQty2")
        var convQty2: Double = 0.0,
        @SerializedName("LotNo")
        var lotNo: String = "",
        @SerializedName("SerialNo")
        var serialNo: String = "",
        @SerializedName("MfgDate")
        var mfgDate: String = "",
        @SerializedName("ExpiryDate")
        var expiryDate: String = "",
        @SerializedName("Remark")
        var remark: String = ""
    ): Serializable{
        fun toStockItem(): StockItem{
            val item = StockItem()
            item.itemCd = itemCd
            item.itemNm = itemNm
            item.itemNo = itemNo
            item.wHUnitNm = stkUoMNm
            item.convStkQty = stkCntQty
            item.convQty1 = convQty1
            item.convQty2 = convQty2
            return item
        }
    }
}