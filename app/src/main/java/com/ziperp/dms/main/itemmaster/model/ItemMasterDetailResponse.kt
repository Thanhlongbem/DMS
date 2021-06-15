package com.ziperp.dms.main.itemmaster.model


import com.google.gson.annotations.SerializedName

data class ItemMasterDetailResponse(
    @SerializedName("Table")
    val data: List<ItemMasterDetail> = listOf()
) {
    data class ItemMasterDetail(
        @SerializedName("ItemCd")
        val itemCd: String = "",
        @SerializedName("ItemNo")
        val itemNo: String = "",
        @SerializedName("ItemNm")
        val itemNm: String = "",
        @SerializedName("ItemEnNm")
        val itemEnNm: String = "",
        @SerializedName("ItemType")
        val itemType: String = "",
        @SerializedName("Barcode")
        val barcode: String = "",
        @SerializedName("ItemSpec")
        val itemSpec: String = "",
        @SerializedName("ItemSts")
        val itemSts: String = "",
        @SerializedName("WhUnitCd")
        val whUnitCd: String = "",
        @SerializedName("MasterLocCd")
        val masterLocCd: String = "",
        @SerializedName("BasicWHCd")
        val basicWHCd: String = "",
        @SerializedName("DefDeptCd")
        val defDeptCd: String = "",
        @SerializedName("DefPIC")
        val defPIC: String = "",
        @SerializedName("DefBuyPrice")
        val defBuyPrice: Double = 0.0,
        @SerializedName("DefSellPrice")
        val defSellPrice: Double = 0.0,
        @SerializedName("CmbParentItemYn")
        val cmbParentItemYn: Int = 0,
        @SerializedName("InclAccessoryYn")
        val inclAccessoryYn: Int = 0,
        @SerializedName("LotSeriCtrl")
        val lotSeriCtrl: String = "",
        @SerializedName("StkItemDiv")
        val stkItemDiv: String = "",
        @SerializedName("SafetyStkQty")
        val safetyStkQty: Double = 0.0,
        @SerializedName("MaxStkQty")
        val maxStkQty: Double = 0.0,
        @SerializedName("ReOrderPoint")
        val reOrderPoint: Double = 0.0,
        @SerializedName("ItemCategory")
        val itemCategory: String = "",
        @SerializedName("ItemBrand")
        val itemBrand: String = "",
        @SerializedName("ItemGrade")
        val itemGrade: String = "",
        @SerializedName("DomYn")
        val domYn: String = "",
        @SerializedName("VatType")
        val vatType: String = "",
        @SerializedName("VatRate")
        val vatRate: Double = 0.0,
        @SerializedName("SellVATinPriceYn")
        val sellVATinPriceYn: String = "",
        @SerializedName("BuyVATinPriceYn")
        val buyVATinPriceYn: String = "",
        @SerializedName("ItemGrp1")
        val itemGrp1: String = "",
        @SerializedName("ItemGrp2")
        val itemGrp2: String = "",
        @SerializedName("ItemGrp3")
        val itemGrp3: String = "",
        @SerializedName("ItemGrp4")
        val itemGrp4: String = "",
        @SerializedName("ItemGrp1Nm")
        val itemGrp1Nm: String = "",
        @SerializedName("ItemGrp2Nm")
        val itemGrp2Nm: String = "",
        @SerializedName("ItemGrp3Nm")
        val itemGrp3Nm: String = "",
        @SerializedName("ItemGrp4Nm")
        val itemGrp4Nm: String = "",
        @SerializedName("WhUnitNm")
        val whUnitNm: String = "",
        @SerializedName("DefDeptNm")
        val defDeptNm: String = "",
        @SerializedName("DefPICNm")
        val defPICNm: String = "",
        @SerializedName("ItemCategoryNm")
        val itemCategoryNm: String = "",
        @SerializedName("ItemBrandNm")
        val itemBrandNm: String = "",
        @SerializedName("ItemModelNm")
        val itemModelNm: String = "",
        @SerializedName("ItemModel")
        val itemModel: String = "",
        @SerializedName("Width")
        val width: Double = 0.0,
        @SerializedName("WidthCd")
        val widthCd: String = "",
        @SerializedName("Length")
        val length: Double = 0.0,
        @SerializedName("LengthCd")
        val lengthCd: String = "",
        @SerializedName("Height")
        val height: Double = 0.0,
        @SerializedName("HeightCd")
        val heightCd: String = "",
        @SerializedName("NetWeight")
        val netWeight: Double = 0.0,
        @SerializedName("NetWeightCd")
        val netWeightCd: String = "",
        @SerializedName("GrossWeight")
        val grossWeight: Double = 0.0,
        @SerializedName("GrossWeightCd")
        val grossWeightCd: String = "",
        @SerializedName("SKU")
        val sKU: String = "",
        @SerializedName("IMEI")
        val iMEI: String = "",
        @SerializedName("Tag")
        val tag: String = "",
        @SerializedName("VideoURL")
        val videoURL: String = "",
        @SerializedName("DetailDesc")
        val detailDesc: String = "",
        @SerializedName("ExpiredDuration")
        val expiredDuration: Int = 0,
        @SerializedName("ExpiredDurType")
        val expiredDurType: String = "",
        @SerializedName("WarrantyPrd")
        val warrantyPrd: Int = 0,
        @SerializedName("WarrantyPrdType")
        val warrantyPrdType: String = "",
        @SerializedName("WarrantyDesc")
        val warrantyDesc: String = "",
        @SerializedName("CstWarrantyPrd")
        val cstWarrantyPrd: Int = 0,
        @SerializedName("CstWarrantyPrdType")
        val cstWarrantyPrdType: String = "",
        @SerializedName("ExpiredDurRtn")
        val expiredDurRtn: Int = 0,
        @SerializedName("ExpiredDurRtnType")
        val expiredDurRtnType: String = "",
        @SerializedName("CstWarrantyDesc")
        val cstWarrantyDesc: String = "",
        @SerializedName("MinShelfLife")
        val minShelfLife: Int = 0,
        @SerializedName("MinShelfLifeType")
        val minShelfLifeType: String = "",
        @SerializedName("KeepCond")
        val keepCond: String = "",
        @SerializedName("KeepCondItem")
        val keepCondItem: String = "",
        @SerializedName("ConPayType")
        val conPayType: String = "",
        @SerializedName("PickMethod")
        val pickMethod: String = "",
        @SerializedName("WidthNm")
        val widthNm: String = "",
        @SerializedName("HeightNm")
        val heightNm: String = "",
        @SerializedName("LengthNm")
        val lengthNm: String = "",
        @SerializedName("NetWeightNm")
        val netWeightNm: String = "",
        @SerializedName("GrossWeightNm")
        val grossWeightNm: String = "",
        @SerializedName("Size")
        val size: String = "",
        @SerializedName("Diameter")
        val diameter: Double = 0.0,
        @SerializedName("DiameterUoMCd")
        val diameterUoMCd: String = "",
        @SerializedName("DiameterUoMNm")
        val diameterUoMNm: String = "",
        @SerializedName("StandardPacking")
        val standardPacking: String = "",
        @SerializedName("SalesUnitCd")
        val salesUnitCd: String = "",
        @SerializedName("SalesUnitNm")
        val salesUnitNm: String = "",
        @SerializedName("OutboundLossRate")
        val outboundLossRate: Double = 0.0,
        @SerializedName("SalesUoMBase")
        val salesUoMBase: String = "",
        @SerializedName("AvaiPOSYn")
        val avaiPOSYn: String = "",
        @SerializedName("AvaiWebYn")
        val avaiWebYn: String = "",
        @SerializedName("ShippableYn")
        val shippableYn: String = "",
        @SerializedName("NotPrintTagYn")
        val notPrintTagYn: String = "",
        @SerializedName("NotGrantLoyaltyPoint")
        val notGrantLoyaltyPoint: String = "",
        @SerializedName("AssemReqYn")
        val assemReqYn: String = "",
        @SerializedName("NotShipOutCity")
        val notShipOutCity: String = "",
        @SerializedName("BuyGrpCd")
        val buyGrpCd: String = "",
        @SerializedName("DefVendorCd")
        val defVendorCd: String = "",
        @SerializedName("DefVendorNm")
        val defVendorNm: String = "",
        @SerializedName("DefMakerCd")
        val defMakerCd: String = "",
        @SerializedName("DefMakerNm")
        val defMakerNm: String = "",
        @SerializedName("BuyUnitCd")
        val buyUnitCd: String = "",
        @SerializedName("BuyUnitNm")
        val buyUnitNm: String = "",
        @SerializedName("BuyDefCurrCd")
        val buyDefCurrCd: String = "",
        @SerializedName("OriginCountryCd")
        val originCountryCd: String = "",
        @SerializedName("OriginCountryNm")
        val originCountryNm: String = "",
        @SerializedName("OriginCountryCd2")
        val originCountryCd2: String = "",
        @SerializedName("OriginCountryNm2")
        val originCountryNm2: String = "",
        @SerializedName("OriginCountryCd3")
        val originCountryCd3: String = "",
        @SerializedName("OriginCountryNm3")
        val originCountryNm3: String = "",
        @SerializedName("ReOrderPoint1")
        val reOrderPoint1: Double = 0.0,
        @SerializedName("SafetyStkQty1")
        val safetyStkQty1: Double = 0.0,
        @SerializedName("MinOrderQty")
        val minOrderQty: Double = 0.0,
        @SerializedName("StepLotSize")
        val stepLotSize: Double = 0.0,
        @SerializedName("InboundLossRate")
        val inboundLossRate: Double = 0.0,
        @SerializedName("BuyDefExRate")
        val buyDefExRate: Double = 0.0,
        @SerializedName("CustomTariff")
        val customTariff: Double = 0.0,
        @SerializedName("SpecialTaxRate")
        val specialTaxRate: Double = 0.0,
        @SerializedName("OtherTaxRate")
        val otherTaxRate: Double = 0.0,
        @SerializedName("ItemHSNo")
        val itemHSNo: String = "",
        @SerializedName("BuyUoMBase")
        val buyUoMBase: String = "",
        @SerializedName("PRAutoYn")
        val pRAutoYn: String = "",
        @SerializedName("PRCalcType")
        val pRCalcType: String = "",
        @SerializedName("MRPCalcType")
        val mRPCalcType: String = "",
        @SerializedName("IQCYn")
        val iQCYn: String = "",
        @SerializedName("ItemDeclareNm")
        val itemDeclareNm: String = "",
        @SerializedName("AvgBuyLeadtime")
        val avgBuyLeadtime: Int = 0,
        @SerializedName("ReqPermitYn")
        val reqPermitYn: Int = 0,
        @SerializedName("ItemTypeNm")
        val itemTypeNm: String = "",
        @SerializedName("ItemStsNm")
        val itemStsNm: String = "",
        @SerializedName("LotSeriCtrlNm")
        val lotSeriCtrlNm: String = "",
        @SerializedName("WarrantyPrdNm")
        val warrantyPrdNm: String = "",
        @SerializedName("CstWarrantyPrdNm")
        val cstWarrantyPrdNm: String = "",
        @SerializedName("ExpireDurationNm")
        val expireDurationNm: String = "",
        @SerializedName("KeepCondNm")
        val keepCondNm: String = ""
    )
}