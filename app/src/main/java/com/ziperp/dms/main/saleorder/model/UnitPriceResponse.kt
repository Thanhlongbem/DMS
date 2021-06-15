package com.ziperp.dms.main.saleorder.model


import com.google.gson.annotations.SerializedName

data class UnitPriceResponse(
    @SerializedName("Table")
    val unitPrices: List<UnitPrice> = listOf()
) {
    data class UnitPrice(
        @SerializedName("MasterLocCd")
        val masterLocCd: String = "",
        @SerializedName("ItemCd")
        val itemCd: String = "",
        @SerializedName("Serl")
        val serl: String = "",
        @SerializedName("UoMCd")
        val uoMCd: String = "",
        @SerializedName("Qty")
        val qty: Double = 0.0,
        @SerializedName("StkUoMCd")
        val stkUoMCd: String = "",
        @SerializedName("Price")
        val price: Double = 0.0,
        @SerializedName("StkQty")
        val stkQty: Double = 0.0,
        @SerializedName("ListPrice")
        val listPrice: Double = 0.0,
        @SerializedName("StdPrice")
        val stdPrice: Double = 0.0,
        @SerializedName("LimitPrice")
        val limitPrice: Double = 0.0,
        @SerializedName("PriceUoMCd")
        val priceUoMCd: String = "",
        @SerializedName("PriceUnitQty")
        val priceUnitQty: Double = 0.0,
        @SerializedName("PLUnitPrice")
        val pLUnitPrice: Double = 0.0,
        @SerializedName("PurchaseCost")
        val purchaseCost: Double = 0.0,
        @SerializedName("TotalCost")
        val totalCost: Double = 0.0,
        @SerializedName("PriceListCd")
        val priceListCd: String = "",
        @SerializedName("PriceListLine")
        val priceListLine: String?,
        @SerializedName("Discount")
        val discount: Double = 0.0,
        @SerializedName("DiscountAmt")
        val discountAmt: Double = 0.0,
        @SerializedName("DiscountCd")
        val discountCd: String = "",
        @SerializedName("DefMakerCd")
        val defMakerCd: String?,
        @SerializedName("MakerCd")
        val makerCd: String?,
        @SerializedName("MinPurQty")
        val minPurQty: String?,
        @SerializedName("PurPkgQty")
        val purPkgQty: String?,
        @SerializedName("CurrencyCd")
        val currencyCd: String?,
        @SerializedName("BidQty")
        val bidQty: String?,
        @SerializedName("VatRate")
        val vatRate: Double = 0.0,
        @SerializedName("VatType")
        val vatType: String = "",
        @SerializedName("VatTypeCd")
        val vatTypeCd: Int = 0,
        @SerializedName("ExRate")
        val exRate: String?,
        @SerializedName("BuyVATinPriceYn")
        val buyVATinPriceYn: String?,
        @SerializedName("SellVATinPriceYn")
        val sellVATinPriceYn: String?,
        @SerializedName("ErrCd")
        val errCd: String = "",
        @SerializedName("ErrMsg")
        val errMsg: String = "",
        @SerializedName("Status")
        val status: String = "",
        @SerializedName("MedPermitSts")
        val medPermitSts: String = ""
    )

    fun isSuccess(): Boolean{
        return unitPrices.isNotEmpty() && unitPrices[0].status == "OK"
    }

    fun message(): String{
        return if(unitPrices.isNotEmpty()){
            "${unitPrices[0].errCd} - ${unitPrices[0].errMsg}"
        }else{
            ""
        }
    }

}