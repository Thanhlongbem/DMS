package com.ziperp.dms.main.salespricepromotion.model


import com.google.gson.annotations.SerializedName

data class PromotionInfoResponse(
    @SerializedName("Table")
    val generalInfo: List<GeneralInfo> = listOf(),
    @SerializedName("Table1")
    val condition: List<Condition> = listOf(),
    @SerializedName("Table2")
    val items: List<Item> = listOf(),
    @SerializedName("Table3")
    val unitApplied: List<UnitApplied> = listOf(),
    @SerializedName("Table4")
    val customers: List<Customer> = listOf()
) {
    data class GeneralInfo(
        @SerializedName("PromoCd")
        val promoCd: String = "",
        @SerializedName("PromoNm")
        val promoNm: String = "",
        @SerializedName("PromoDesc")
        val promoDesc: String = "",
        @SerializedName("CurrencyCd")
        val currencyCd: String = "",
        @SerializedName("DateStartEffect")
        val dateStartEffect: String = "",
        @SerializedName("DateEndEffect")
        val dateEndEffect: String = "",
        @SerializedName("ActiveYn")
        val activeYn: Int = 0,
        @SerializedName("btnActivePromotion")
        val btnActivePromotion: String = "",
        @SerializedName("ActiveDate")
        val activeDate: String = "",
        @SerializedName("ActiveMan")
        val activeMan: String = "",
        @SerializedName("CouponYn")
        val couponYn: String = "",
        @SerializedName("CouponCd")
        val couponCd: String = "",
        @SerializedName("UsesPerCust")
        val usesPerCust: Int = 0,
        @SerializedName("UsesPerCoupon")
        val usesPerCoupon: Int = 0,
        @SerializedName("ApplyRule")
        val applyRule: String = "",
        @SerializedName("BuyXQty")
        val buyXQty: Double = 0.0,
        @SerializedName("GetNQty")
        val getNQty: Double = 0.0,
        @SerializedName("DiscountType")
        val discountType: String = "",
        @SerializedName("DiscountValue")
        val discountValue: Double = 0.0,
        @SerializedName("MaxDiscQtyApply")
        val maxDiscQtyApply: Int = 0,
        @SerializedName("MaxDiscAmt")
        val maxDiscAmt: Double = 0.0,
        @SerializedName("EachMth")
        val eachMth: Int = 0,
        @SerializedName("Combination")
        val combination: String = "",
        @SerializedName("PriorityLvl")
        val priorityLvl: Int = 0,
        @SerializedName("ApplyByItemGrp")
        val applyByItemGrp: Int = 0,
        @SerializedName("CombinationNm")
        val combinationNm: String = "",
        @SerializedName("ValidStatusNm")
        val validStatusNm: String = "",
        @SerializedName("ValidStatus")
        val validStatus: Int = 0,
        @SerializedName("ActiveStsNm")
        val activeStsNm: String = "",
        @SerializedName("ActiveSts")
        val activeSts: Int = 0,
        @SerializedName("MaximumGiftGet")
        val maximumGiftGet: String = "",
        @SerializedName("PromoAction")
        val promoAction: String = "",
        @SerializedName("StepBuyItem")
        val stepBuyItem: String = ""
    )

    data class Condition(
        @SerializedName("AndOrOp")
        val andOrOp: Int = 0,
        @SerializedName("CondTypeCd")
        val condTypeCd: String = "",
        @SerializedName("CondTypeCd2")
        val condTypeCd2: String = "",
        @SerializedName("CondType")
        val condType: String = "",
        @SerializedName("ConditionCd")
        val conditionCd: String = "",
        @SerializedName("ConditionCd2")
        val conditionCd2: String = "",
        @SerializedName("Condition")
        val condition: String = "",
        @SerializedName("OperatorCd")
        val operatorCd: String = "",
        @SerializedName("Operator")
        val `operator`: String = "",
        @SerializedName("LookupCd")
        val lookupCd: String = "",
        @SerializedName("DataType")
        val dataType: String = "",
        @SerializedName("ControlType")
        val controlType: String = "",
        @SerializedName("Serl")
        val serl: String = "",
        @SerializedName("Value")
        val value: String = "",
        @SerializedName("ValueComboCd")
        val valueComboCd: Any = Any(),
        @SerializedName("ValueLookup")
        val valueLookup: String = "",
        @SerializedName("ValueDate")
        val valueDate: Any = Any(),
        @SerializedName("StrConditionApply")
        val strConditionApply: String = ""
    )

    data class Item(
        @SerializedName("ItemNm")
        val itemNm: String = "",
        @SerializedName("CategoryNm")
        val categoryNm: Any = Any(),
        @SerializedName("ItemNo")
        val itemNo: String = "",
        @SerializedName("ItemSpec")
        val itemSpec: String = "",
        @SerializedName("ItemCd")
        val itemCd: String = "",
        @SerializedName("ItemCd2")
        val itemCd2: String = "",
        @SerializedName("CategoryCd")
        val categoryCd: String = "",
        @SerializedName("CodeCd2")
        val codeCd2: String = "",
        @SerializedName("GiftCd")
        val giftCd: String = "",
        @SerializedName("GiftCdOld")
        val giftCdOld: String = "",
        @SerializedName("GiftVal")
        val giftVal: Any = Any(),
        @SerializedName("EffectFrDate")
        val effectFrDate: Any = Any(),
        @SerializedName("EffectToDate")
        val effectToDate: Any = Any(),
        @SerializedName("GiftNm")
        val giftNm: Any = Any(),
        @SerializedName("GiftTypeCd")
        val giftTypeCd: Any = Any(),
        @SerializedName("GiftTypeNm")
        val giftTypeNm: Any = Any(),
        @SerializedName("Value")
        val value: Double = 0.0,
        @SerializedName("Qty")
        val qty: Double = 0.0,
        @SerializedName("SortOrder")
        val sortOrder: Int = 0,
        @SerializedName("StrGiftItem")
        val strGiftItem: String = ""
    )

    data class UnitApplied(
        @SerializedName("BizUnitNm")
        val bizUnitNm: String = "",
        @SerializedName("BizUnitCd")
        val bizUnitCd: String = "",
        @SerializedName("BizUnitOldCd")
        val bizUnitOldCd: String = "",
        @SerializedName("LookupCd")
        val lookupCd: String = "",
        @SerializedName("LocationType")
        val locationType: Int = 0,
        @SerializedName("LocationTypeNm")
        val locationTypeNm: String = "",
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