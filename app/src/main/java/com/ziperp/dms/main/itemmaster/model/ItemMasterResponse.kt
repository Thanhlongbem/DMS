package com.ziperp.dms.main.itemmaster.model


import com.google.gson.annotations.SerializedName

data class ItemMasterResponse(
    @SerializedName("Table")
    val data: List<ItemMaster> = listOf(),
    @SerializedName("Table1")
    val record: List<ItemMasterRecord> = listOf()
) {
    data class ItemMaster(
        @SerializedName("txtItemNm")
        val txtItemNm: String = "",
        @SerializedName("txtItemNo")
        val txtItemNo: String = "",
        @SerializedName("txtItemSpec")
        val txtItemSpec: String = "",
        @SerializedName("txtBarcode")
        val txtBarcode: String = "",
        @SerializedName("txtItemEnNm")
        val txtItemEnNm: String = "",
        @SerializedName("txtDeclareNm")
        val txtDeclareNm: String = "",
        @SerializedName("DefBuyPrice")
        val defBuyPrice: Double = 0.0,
        @SerializedName("DefSellPrice")
        val defSellPrice: Double = 0.0,
        @SerializedName("txtLotSeriCtrl")
        val txtLotSeriCtrl: String = "",
        @SerializedName("chkCmbParentItemYn")
        val chkCmbParentItemYn: Int = 0,
        @SerializedName("chkInclAccessoryYn")
        val chkInclAccessoryYn: Int = 0,
        @SerializedName("VatRate")
        val vatRate: Double = 0.0,
        @SerializedName("SafetyStkQty")
        val safetyStkQty: Double = 0.0,
        @SerializedName("MaxStkQty")
        val maxStkQty: Double = 0.0,
        @SerializedName("ReOrderPoint")
        val reOrderPoint: Double = 0.0,
        @SerializedName("MasterLocCd")
        val masterLocCd: String = "",
        @SerializedName("BasicWHCd")
        val basicWHCd: String = "",
        @SerializedName("DefDeptCd")
        val defDeptCd: String = "",
        @SerializedName("cmbVatType")
        val cmbVatType: String = "",
        @SerializedName("txtItemType")
        val txtItemType: String = "",
        @SerializedName("ItemType")
        val itemType: String = "",
        @SerializedName("txtStkItemDiv")
        val txtStkItemDiv: String = "",
        @SerializedName("demo")
        val demo: Int = 0,
        @SerializedName("txtItemSts")
        val txtItemSts: String = "",
        @SerializedName("ItemSts")
        val itemSts: String = "",
        @SerializedName("txtMasterLocCd")
        val txtMasterLocCd: String = "",
        @SerializedName("txtBasicWhCd")
        val txtBasicWhCd: String = "",
        @SerializedName("txtDefDeptCd")
        val txtDefDeptCd: String = "",
        @SerializedName("txtWhUnitCd")
        val txtWhUnitCd: String = "",
        @SerializedName("txtSalesUnitCd")
        val txtSalesUnitCd: String = "",
        @SerializedName("txtBuyUnitCd")
        val txtBuyUnitCd: String = "",
        @SerializedName("fltConvStkQtyPur")
        val fltConvStkQtyPur: Double = 0.0,
        @SerializedName("flConvQty1Pur")
        val flConvQty1Pur: Double = 0.0,
        @SerializedName("flConvQty2Pur")
        val flConvQty2Pur: Double = 0.0,
        @SerializedName("fltConvStkQtySales")
        val fltConvStkQtySales: Double = 0.0,
        @SerializedName("flConvQty1Sales")
        val flConvQty1Sales: Double = 0.0,
        @SerializedName("flConvQty2Sales")
        val flConvQty2Sales: Double = 0.0,
        @SerializedName("txtBOMUnitCd")
        val txtBOMUnitCd: String = "",
        @SerializedName("MinOrderQty")
        val minOrderQty: Double = 0.0,
        @SerializedName("StepLotSize")
        val stepLotSize: Double = 0.0,
        @SerializedName("txtDefVendorCd")
        val txtDefVendorCd: String = "",
        @SerializedName("txtDefMakerCd")
        val txtDefMakerCd: String = "",
        @SerializedName("txtItemCate")
        val txtItemCate: String = "",
        @SerializedName("fltCustomTariff")
        val fltCustomTariff: Double = 0.0,
        @SerializedName("txtHSNo")
        val txtHSNo: String = "",
        @SerializedName("fltSpecialTaxRate")
        val fltSpecialTaxRate: Double = 0.0,
        @SerializedName("fltOtherTaxRate")
        val fltOtherTaxRate: Double = 0.0,
        @SerializedName("txtDomYn")
        val txtDomYn: String = "",
        @SerializedName("txtBrand")
        val txtBrand: String = "",
        @SerializedName("txtModel")
        val txtModel: String = "",
        @SerializedName("txtItemGroup")
        val txtItemGroup: String = "",
        @SerializedName("RegDate")
        val regDate: String = "",
        @SerializedName("Creator")
        val creator: String = "",
        @SerializedName("KeepCond")
        val keepCond: String = "",
        @SerializedName("KeepCondItem")
        val keepCondItem: String = "",
        @SerializedName("txtCO")
        val txtCO: String = "",
        @SerializedName("LinkFormId")
        val linkFormId: String = "",
        @SerializedName("GridKey")
        val itemCd: String = "",
        @SerializedName("LinkPara")
        val linkPara: String = ""
    )

    data class ItemMasterRecord(
        @SerializedName("TotalRecords")
        val totalRecords: Double = 0.0
    )
}