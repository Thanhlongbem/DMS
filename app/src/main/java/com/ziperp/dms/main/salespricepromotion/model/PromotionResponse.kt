package com.ziperp.dms.main.salespricepromotion.model


import com.google.gson.annotations.SerializedName

data class PromotionResponse(
    @SerializedName("Table")
    val data: List<Promotion> = listOf()
) {
    data class Promotion(
        @SerializedName("PromotionNo")
        val promotionNo: String = "",
        @SerializedName("PromotionNm")
        val promotionNm: String = "",
        @SerializedName("PromotionDesc")
        val promotionDesc: String = "",
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
        @SerializedName("StrGift")
        val strGift: String = "",
        @SerializedName("ValidStatusNm")
        val validStatusNm: String = "",
        @SerializedName("ValidStatus")
        val validStatus: Int = 0,
        @SerializedName("ActiveSts")
        val activeSts: Int = 0,
        @SerializedName("ActiveStsNm")
        val activeStsNm: String = "",
        @SerializedName("dtActivatedDate")
        val dtActivatedDate: String = "",
        @SerializedName("txtActivatedMan")
        val txtActivatedMan: String = "",
        @SerializedName("PromoAction")
        val promoAction: String = ""
    )
}