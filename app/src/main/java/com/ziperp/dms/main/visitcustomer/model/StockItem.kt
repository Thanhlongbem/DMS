package com.ziperp.dms.main.visitcustomer.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StockItem(
    @SerializedName("NUMBER")
    var nUMBER: Int = 0,
    @SerializedName("ItemNo")
    var itemNo: String = "",
    @SerializedName("ItemNm")
    var itemNm: String = "",
    @SerializedName("BarCode")
    var barCode: String = "",
    @SerializedName("ItemSpec")
    var itemSpec: String = "",
    @SerializedName("ItemDetailDesc")
    var itemDetailDesc: String = "",
    @SerializedName("ItemGrp1Nm")
    var itemGrp1Nm: String = "",
    @SerializedName("ItemGrp2Nm")
    var itemGrp2Nm: String = "",
    @SerializedName("ItemGrp3Nm")
    var itemGrp3Nm: String = "",
    @SerializedName("ItemGrp4Nm")
    var itemGrp4Nm: String = "",
    @SerializedName("ItemGrp1")
    var itemGrp1: String = "",
    @SerializedName("ItemGrp2")
    var itemGrp2: String = "",
    @SerializedName("ItemGrp3")
    var itemGrp3: String = "",
    @SerializedName("ItemGrp4")
    var itemGrp4: String = "",
    @SerializedName("ModelNm")
    var modelNm: String = "",
    @SerializedName("ModelNo")
    var modelNo: String = "",
    @SerializedName("ItemBrandNm")
    var itemBrandNm: String = "",
    @SerializedName("ItemTypeNm")
    var itemTypeNm: String = "",
    @SerializedName("ItemCategoryNm")
    var itemCategoryNm: String = "",
    @SerializedName("HSNo")
    var hSNo: String = "",
    @SerializedName("DefVendorNm")
    var defVendorNm: String = "",
    @SerializedName("VendorNm")
    var vendorNm: String = "",
    @SerializedName("VendorNo")
    var vendorNo: String = "",
    @SerializedName("CurrencyCd")
    var currencyCd: String = "",
    @SerializedName("DefMakerNm")
    var defMakerNm: String = "",
    @SerializedName("MakerNm")
    var makerNm: String = "",
    @SerializedName("MakerNo")
    var makerNo: String = "",
    @SerializedName("WHUnitNm")
    var wHUnitNm: String = "",
    @SerializedName("UoMCd")
    var uoMCd: String = "",
    @SerializedName("UoMNm")
    var uoMNm: String = "",
    @SerializedName("WHUnitCd")
    var wHUnitCd: String = "",
    @SerializedName("ItemTypeCd")
    var itemTypeCd: String = "",
    @SerializedName("ItemCategoryCd")
    var itemCategoryCd: String = "",
    @SerializedName("DefVendorCd")
    var defVendorCd: String = "",
    @SerializedName("VendorCd")
    var vendorCd: String = "",
    @SerializedName("ItemBrandCd")
    var itemBrandCd: String = "",
    @SerializedName("DefMakerCd")
    var defMakerCd: String = "",
    @SerializedName("MakerCd")
    var makerCd: String = "",
    @SerializedName("ItemCd")
    var itemCd: String = "",
    @SerializedName("ItemEnNm")
    var itemEnNm: String = "",
    @SerializedName("ItemStatus")
    var itemStatus: String = "",
    @SerializedName("LotNoCtrlYn")
    var lotNoCtrlYn: Int = 0,
    @SerializedName("SerialNoCtrlYn")
    var serialNoCtrlYn: Int = 0,
    @SerializedName("ConvStkQty")
    var convStkQty: Double = 0.0,
    @SerializedName("ConvQty1")
    var convQty1: Double = 0.0,
    @SerializedName("ConvQty2")
    var convQty2: Double = 0.0,
    @SerializedName("WhCd")
    var whCd: String = "",
    @SerializedName("WhNm")
    var whNm: String = "",
    @SerializedName("VATRate")
    var vATRate: Double = 0.0,
    @SerializedName("VatTypeCd")
    var vatTypeCd: Int = 0,
    @SerializedName("VatTypeNm")
    var vatTypeNm: String = "",
    @SerializedName("InclAccessoryYn")
    var inclAccessoryYn: Int = 0,
    @SerializedName("SellVATinPriceYn")
    var sellVATinPriceYn: Int = 0,
    @SerializedName("BuyVATinPriceYn")
    var buyVATinPriceYn: Int = 0,
    @SerializedName("ItemDeclareNm")
    var itemDeclareNm: String = "",
    @SerializedName("StdPacking")
    var stdPacking: String = "",
    @SerializedName("KeepCond")
    var keepCond: String = "",
    @SerializedName("OriginCountryNm")
    var originCountryNm: String = "",
    @SerializedName("OriginCountryCd")
    var originCountryCd: String = ""
): Serializable