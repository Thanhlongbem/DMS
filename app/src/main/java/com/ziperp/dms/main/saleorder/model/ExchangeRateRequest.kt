package com.ziperp.dms.main.saleorder.model


import com.google.gson.annotations.SerializedName

data class ExchangeRateRequest(
    @SerializedName("CurrCd")
    val currCd: String = "",
    @SerializedName("Date")
    val date: String = "",
    @SerializedName("MasterLocCd")
    val masterLocCd: String = ""
)