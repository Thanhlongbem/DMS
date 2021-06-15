package com.ziperp.dms.main.saleorder.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CUDSalesOrderRequest(
    @SerializedName("MasterLocCd")
    var masterLocCd: String = "",
    @SerializedName("OrderNo")
    var orderNo: String = "",
    @SerializedName("OrderMngtNo")
    var orderMngtNo: String = "",
    @SerializedName("PoNo")
    var poNo: String = "",
    @SerializedName("PoNm")
    var poNm: String = "",
    @SerializedName("OrderVer")
    var orderVer: String = "",
    @SerializedName("OrderDate")
    var orderDate: String = "",
    @SerializedName("WhType")
    var whType: String = "",
    @SerializedName("Remark")
    var remark: String = "",
    @SerializedName("ProjectCd")
    var projectCd: String = "",
    @SerializedName("CstCd")
    var cstCd: String = "",
    @SerializedName("CstPhone")
    var cstPhone: String = "",
    @SerializedName("SChannels")
    var sChannels: String = "",
    @SerializedName("ShipMethod")
    var shipMethod: String = "",
    @SerializedName("StaffId")
    var staffId: String = "",
    @SerializedName("DeptCd")
    var deptCd: String = "",
    @SerializedName("PaymentTerm")
    var paymentTerm: String = "",
    @SerializedName("CurrencyCd")
    var currencyCd: String = "",
    @SerializedName("ExRate")
    var exRate: Int = 0,
    @SerializedName("DocNo")
    var docNo: String = "",
    @SerializedName("SrcType")
    var srcType: String = "",
    @SerializedName("DelvAddress")
    var delvAddress: String = "",
    @SerializedName("DelvDate")
    var delvDate: String = "",
    @SerializedName("DelvTime")
    var delvTime: String = "",
    @SerializedName("DelvDateTime")
    var delvDateTime: String = "",
    @SerializedName("ContactName")
    var contactName: String = "",
    @SerializedName("ContactPhone")
    var contactPhone: String = ""
): Serializable