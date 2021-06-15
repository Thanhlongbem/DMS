package com.ziperp.dms.main.visitcustomer.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StockCountRequest(
    @SerializedName("CstVisitNo")
    var cstVisitNo: String = "",
    @SerializedName("CstVisitLineNo")
    var cstVisitLineNo: String = "",
    @SerializedName("ItemCd")
    var itemCd: String = "",
    @SerializedName("CntUoMCd")
    var cntUoMCd: String = "",
    @SerializedName("CntQty")
    var cntQty: Double = 0.0,
    @SerializedName("LotSerialNo")
    var lotSerialNo: String = "",
    @SerializedName("MfgDate")
    var mfgDate: String = "",
    @SerializedName("ExpiryDate")
    var expiryDate: String = "",
    @SerializedName("Remark")
    var remark: String = ""
):Serializable

data class StockCountInfo(
    var CstVisitNo: String = ""
)