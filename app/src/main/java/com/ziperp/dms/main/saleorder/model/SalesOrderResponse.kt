package com.ziperp.dms.main.saleorder.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SalesOrderResponse(
    @SerializedName("Table")
    val data: List<SaleOrder> = listOf(),
    @SerializedName("Table1")
    val record: List<Record> = listOf()
) {
    data class SaleOrder(
        @SerializedName("chkSOCfm")
        val chkSOCfm: Int = 0,
        @SerializedName("chkSOStop")
        val chkSOStop: Int = 0,
        @SerializedName("txtBizUnit")
        val txtBizUnit: String = "",
        @SerializedName("SChannels")
        val sChannels: String = "",
        @SerializedName("OrderDate")
        val orderDate: String = "",
        @SerializedName("DelvDate")
        val delvDate: String = "",
        @SerializedName("OrderNo")
        val orderNo: String = "",
        @SerializedName("txtQuotaNo")
        val txtQuotaNo: String = "",
        @SerializedName("OrderStatusNm")
        val orderStatusNm: String = "",
        @SerializedName("OrderStatus")
        val orderStatus: Int = 0,
        @SerializedName("txtCustNm")
        val txtCustNm: String = "",
        @SerializedName("txtCurrency")
        val txtCurrency: String = "",
        @SerializedName("fltTotalQty")
        val fltTotalQty: Double = 0.0,
        @SerializedName("fltSubTotal")
        val fltSubTotal: Double = 0.0,
        @SerializedName("fltSubTotalBase")
        val fltSubTotalBase: Double = 0.0,
        @SerializedName("fltTotalAmt")
        val fltTotalAmt: Double = 0.0,
        @SerializedName("fltTotalAmtBase")
        val fltTotalAmtBase: Double = 0.0,
        @SerializedName("MinPrice")
        val minPrice: String = "",
        @SerializedName("fltExtendedPrice")
        val fltExtendedPrice: Double = 0.0,
        @SerializedName("fltExtPriceBase")
        val fltExtPriceBase: Double = 0.0,
        @SerializedName("fltVatAmt")
        val fltVatAmt: Double = 0.0,
        @SerializedName("fltVatAmtBase")
        val fltVatAmtBase: Double = 0.0,
        @SerializedName("fltOrderQty")
        val fltOrderQty: Double = 0.0,
        @SerializedName("fltDiscountAmt")
        val fltDiscountAmt: Double = 0.0,
        @SerializedName("fltDiscAmtBase")
        val fltDiscAmtBase: Double = 0.0,
        @SerializedName("txtSalesman")
        val txtSalesman: String = "",
        @SerializedName("Creator")
        val creator: String = "",
        @SerializedName("CreateDate")
        val createDate: String = "",
        @SerializedName("txtDept")
        val txtDept: String = "",
        @SerializedName("txtDelvAddr")
        val txtDelvAddr: String = "",
        @SerializedName("txtCstPhone")
        val txtCstPhone: String = "",
        @SerializedName("txtCstAddr")
        val txtCstAddr: Any = Any(),
        @SerializedName("txtCstContact")
        val txtCstContact: String = "",
        @SerializedName("txtPjtNm")
        val txtPjtNm: String = "",
        @SerializedName("PoNo")
        val poNo: String = "",
        @SerializedName("Remark")
        val remark: String = "",
        @SerializedName("LinkPara")
        val linkPara: String = "",
        @SerializedName("LinkFormId")
        val linkFormId: String = "",
        @SerializedName("GridKey")
        val gridKey: String = ""
    ): Serializable

    data class Record(
        @SerializedName("TotalRecords")
        val totalRecords: Double = 0.0
    )
}