package com.ziperp.dms.main.visitcustomer.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VisitCustomerOrderResponse(
    @SerializedName("Table")
    val data: List<SaleOrderVisit> = listOf()
)

data class SaleOrderVisit(
    @SerializedName("OrderMngtNo")
    val orderMngtNo: String = "",
    @SerializedName("OrderNo")
    val orderNo: String = "",
    @SerializedName("OrderDate")
    val orderDate: String = "",
    @SerializedName("txtCustNm")
    val txtCustNm: String = "",
    @SerializedName("txtCstNo")
    val txtCstNo: String = "",
    @SerializedName("Remark")
    val remark: String = "",
    @SerializedName("fltTotalAmt")
    val fltTotalAmt: Double = 0.0,
    @SerializedName("OrderStatusNm")
    val orderStatusNm: String = "",
    @SerializedName("OrderStatus")
    val orderStatus: Int = 0
): Serializable