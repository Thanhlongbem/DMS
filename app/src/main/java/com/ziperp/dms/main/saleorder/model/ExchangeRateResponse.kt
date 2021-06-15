package com.ziperp.dms.main.saleorder.model


import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @SerializedName("Table")
    val rates: List<ExchangeRate> = listOf()
) {
    data class ExchangeRate(
        @SerializedName("ExRate")
        val exRate: Double = 0.0
    )
}