package com.ziperp.dms.main.saleorder.model


import com.google.gson.annotations.SerializedName

data class UnitPriceRequest(
    @SerializedName("PriceMode")
    val priceMode: String = "",
    @SerializedName("CstCd")
    val cstCd: String = "",
    @SerializedName("CurrencyCd")
    val currencyCd: String = "",
    @SerializedName("Date")
    val date: String = "",
    @SerializedName("MasterLocCd")
    val masterLocCd: String = "",
    @SerializedName("DeptCd")
    val deptCd: String = "",
    @SerializedName("StaffId")
    val staffId: String = "",
    @SerializedName("StrData")
    val strData: String = ""
)