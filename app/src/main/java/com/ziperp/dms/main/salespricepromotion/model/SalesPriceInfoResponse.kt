package com.ziperp.dms.main.salespricepromotion.model


import com.google.gson.annotations.SerializedName

data class  SalesPriceInfoResponse(
    @SerializedName("Table")
    val general: List<GeneralData> = listOf(),
    @SerializedName("Table1")
    val items: List<Item> = listOf(),
    @SerializedName("Table2")
    val unitApplied: List<UnitApplied> = listOf(),
    @SerializedName("Table3")
    val customers: List<Customer> = listOf()
) {
    data class GeneralData(
        @SerializedName("PriceListCd")
        val priceListCd: String = "",
        @SerializedName("PriceListNm")
        val priceListNm: String = "",
        @SerializedName("PriceListDesc")
        val priceListDesc: String = "",
        @SerializedName("CurrencyCd")
        val currencyCd: String = "",
        @SerializedName("DateStartEffect")
        val dateStartEffect: String = "",
        @SerializedName("DateEndEffect")
        val dateEndEffect: String = "",
        @SerializedName("ActiveYn")
        val activeYn: Int = 0,
        @SerializedName("DefaultYn")
        val defaultYn: Int = 0,
        @SerializedName("btnActivePriceList")
        val btnActivePriceList: String = "",
        @SerializedName("ActiveDate")
        val activeDate: String = "",
        @SerializedName("ActiveMan")
        val activeMan: String = "",
        @SerializedName("ValidStatusNm")
        val validStatusNm: String = "",
        @SerializedName("ValidStatus")
        val validStatus: Int = 0,
        @SerializedName("ActiveStsNm")
        val activeStsNm: String = "",
        @SerializedName("ActiveSts")
        val activeSts: Int = 0,
        @SerializedName("StrItem")
        val strItem: String = ""
    )

    data class Item(
        @SerializedName("ItemNo")
        val itemNo: String = "",
        @SerializedName("ItemNm")
        val itemNm: String = "",
        @SerializedName("ItemSpec")
        val itemSpec: String = "",
        @SerializedName("UoMNm")
        val uoMNm: String = "",
        @SerializedName("ItemCd")
        val itemCd: String = "",
        @SerializedName("ItemCd2")
        val itemCd2: String = "",
        @SerializedName("SalesPrice")
        val salesPrice: Double = 0.0,
        @SerializedName("UoMCd")
        val uoMCd: String = "",
        @SerializedName("UoMCd2")
        val uoMCd2: String = "",
        @SerializedName("DiscountRate")
        val discountRate: Double = 0.0,
        @SerializedName("ModifierRate")
        val modifierRate: Double = 0.0,
        @SerializedName("PriceMethodCd")
        val priceMethodCd: Int = 0,
        @SerializedName("PriceMethod")
        val priceMethod: String = "",
        @SerializedName("SeqNo")
        val seqNo: String = "",
        @SerializedName("SalesQty")
        val salesQty: Double = 0.0,
        @SerializedName("VatRate")
        val vatRate: Double = 0.0,
        @SerializedName("StrItemInfo")
        val strItemInfo: String = ""
    )

    data class UnitApplied(
        @SerializedName("BizUnitNm")
        val bizUnitNm: String = "",
        @SerializedName("RegionNm")
        val regionNm: String = "",
        @SerializedName("BizUnitCd")
        val bizUnitCd: String = "",
        @SerializedName("BizUnitOldCd")
        val bizUnitOldCd: String = "",
        @SerializedName("StrBizUnitApplied")
        val strBizUnitApplied: String = ""
    )

    data class Customer(
        @SerializedName("CstNm")
        val cstNm: String = "",
        @SerializedName("CstCd")
        val cstCd: String = "",
        @SerializedName("CstCdOld")
        val cstCdOld: String = "",
        @SerializedName("CustType")
        val custType: Int = 0,
        @SerializedName("LookupCd")
        val lookupCd: String = "",
        @SerializedName("CstAttrNm")
        val cstAttrNm: String = "",
        @SerializedName("StrCustomerApplied")
        val strCustomerApplied: String = ""
    )
}