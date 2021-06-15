package com.ziperp.dms.main.itemmaster.model

import com.google.gson.annotations.SerializedName

data class StockQtyByLotSerialResponse(

	@SerializedName("Table")
	val listItems: List<Item> = arrayListOf()
){
	data class Item(

		@SerializedName("txtUoM")
		val txtUoM: String? = "",

		@SerializedName("GridKey")
		val gridKey: String? = "",

		@SerializedName("txtItemNo")
		val txtItemNo: String? = "",

		@SerializedName("txtCategory")
		val txtCategory: String? = "",

		@SerializedName("fltStkBalQty")
		val fltStkBalQty: Double? = 0.0,

		@SerializedName("txtWhNm")
		val txtWhNm: String? = "",

		@SerializedName("txtItemNm")
		val txtItemNm: String? = "",

		@SerializedName("txtItemDesc")
		val txtItemDesc: String? = "",

		@SerializedName("txtBizAndWh")
		val txtBizAndWh: String? = "",

		@SerializedName("fltInStkQty")
		val fltInStkQty: Double? = 0.0,

		@SerializedName("txtManufacturer")
		val txtManufacturer: String? = "",

		@SerializedName("txtBizUnitShortNm")
		val txtBizUnitShortNm: String? = "",

		@SerializedName("txtBizUnit")
		val txtBizUnit: String? = "",

		@SerializedName("txtLotSeriNo")
		val txtLotSeriNo: String? = "",

		@SerializedName("txtModel")
		val txtModel: String? = "",

		@SerializedName("dtMfgDate")
		val dtMfgDate: String? = "",

		@SerializedName("fltOutStkQty")
		val fltOutStkQty: Double? = 0.0,

		@SerializedName("dtExpiryDate")
		val dtExpiryDate: String? = "",

		@SerializedName("LinkFormId")
		val linkFormId: String? = "",

		@SerializedName("fltBeginStkQty")
		val fltBeginStkQty: Double = 0.0,

		@SerializedName("txtLotSeriCtrlNm")
		val txtLotSeriCtrlNm: String? = "",

		@SerializedName("txtItemGrp")
		val txtItemGrp: String? = "",

		@SerializedName("LinkPara")
		val linkPara: String? = ""
	)
}


