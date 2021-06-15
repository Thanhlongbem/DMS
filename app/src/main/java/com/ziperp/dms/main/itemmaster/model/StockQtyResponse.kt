package com.ziperp.dms.main.itemmaster.model

import com.google.gson.annotations.SerializedName

data class StockQtyResponse(

	@SerializedName("Table")
	val listItems: List<Item> = arrayListOf()
){
	data class Item(

		@SerializedName("txtUoM")
		val txtUoM: String? = "",

		@SerializedName("txtItemNo")
		val txtItemNo: String? = "",

		@SerializedName("txtItemNm")
		val txtItemNm: String? = "",

		@SerializedName("fltDefectAmt")
		val fltDefectAmt: Double? = null,

		@SerializedName("txtLocType")
		val txtLocType: String? = "",

		@SerializedName("fltDefectQty")
		val fltDefectQty: Double? = 0.0,

		@SerializedName("txtLotSeriCtrlNm")
		val txtLotSeriCtrlNm: String? = "",

		@SerializedName("txtBizUnit")
		val txtBizUnit: String? = "",

		@SerializedName("GridId")
		val gridId: String? = "",

		@SerializedName("fltQty")
		val fltQty: Double = 0.0,

		@SerializedName("txtLocation")
		val txtLocation: String? = "",

		@SerializedName("fltAmt")
		val fltAmt: Double? = 0.0
	)
}


