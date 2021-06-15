package com.ziperp.dms.main.salespricepromotion.model


import com.google.gson.annotations.SerializedName

data class SalesPriceResponse(
    @SerializedName("Table")
    val data: List<SalesPrice> = listOf()
) {
    data class SalesPrice(
        @SerializedName("PriceListCd")
        val priceListCd: String = "",
        @SerializedName("PriceListNm")
        val priceListNm: String = "",
        @SerializedName("PriceListDesc")
        val priceListDesc: String = "",
        @SerializedName("StartDate")
        val startDate: String = "",
        @SerializedName("EndDate")
        val endDate: String = "",
        @SerializedName("Currency")
        val currency: String = "",
        @SerializedName("StrStore")
        val strStore: String = "",
        @SerializedName("StrCustomer")
        val strCustomer: String = "",
        @SerializedName("ValidStatusNm")
        val validStatusNm: String = "",
        @SerializedName("ValidStatus")
        val validStatus: Int = 0,
        @SerializedName("ActiveStsNm")
        val activeStsNm: String = "",
        @SerializedName("ActiveSts")
        val activeSts: Int = 0,
        @SerializedName("ActivatedDate")
        val activatedDate: String = "",
        @SerializedName("ActivatedMan")
        val activatedMan: String = ""
    )
}