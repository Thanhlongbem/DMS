package com.ziperp.dms.main.saleorder.model


import com.google.gson.annotations.SerializedName

data class CUDLinesRequest(
    @SerializedName("OrderNo")
    var orderNo: String = "",
    @SerializedName("OrderLineNo")
    var orderLineNo: String = "",
    @SerializedName("ItemCd")
    var itemCd: String = "",
    @SerializedName("UoMCd")
    var uoMCd: String = "",
    @SerializedName("WhCd")
    var whCd: String = "",
    @SerializedName("OrderQty")
    var orderQty: Int = 0,
    @SerializedName("UnitPrice")
    var unitPrice: Double = 0.0,
    @SerializedName("DiscType")
    var discType: Int = 0,
    @SerializedName("Discount")
    var discount: Int = 0,
    @SerializedName("DiscountAmt")
    var discountAmt: Int = 0,
    @SerializedName("PriceChk")
    var priceChk: String = "",
    @SerializedName("PriceListCd")
    var priceListCd: String = "",
    @SerializedName("PriceListLine")
    var priceListLine: String = "",
    @SerializedName("ParentItem")
    var parentItem: String = "",
    @SerializedName("Remark")
    var remark: String = "",
    @SerializedName("QuotationNo")
    var quotationNo: String = "",
    @SerializedName("QuotationVer")
    var quotationVer: String = "",
    @SerializedName("QuotationLineNo")
    var quotationLineNo: String = "",
    @SerializedName("CstItemNm")
    var cstItemNm: String = "",
    @SerializedName("CstItemNo")
    var cstItemNo: String = "",
    @SerializedName("DelvDateTime")
    var delvDateTime: String = "",
    @SerializedName("DelvAddr")
    var delvAddr: String = "",
    @SerializedName("SrcType")
    var srcType: String = "",
    @SerializedName("DocNo")
    var docNo: String = "",
    @SerializedName("DocLineNo")
    var docLineNo: String = ""
)