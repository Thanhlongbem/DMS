package com.ziperp.dms.common.table.model

import com.google.gson.annotations.SerializedName

data class SaleReportRequest(

	@SerializedName("CalculateBy")
	val calculateBy: String = "",

	@SerializedName("Database")
	val database: String = "",

	@SerializedName("SalesChannel")
	val salesChannel: String = "",

	@SerializedName("ItemType")
	val itemType: String = "",

	@SerializedName("ItemCd")
	val itemCd: String = "",

	@SerializedName("ItemGrp1")
	val itemGrp1: String = "",

	@SerializedName("ItemGrp2")
	val itemGrp2: String = "",

	@SerializedName("ItemModel")
	val itemModel: String = "",

	@SerializedName("ItemBrand")
	val itemBrand: String = "",

	@SerializedName("MasterLocCd")
	val masterLocCd: String = "",

	@SerializedName("Month")
	val month: String = "",

	@SerializedName("PageNumber")
	val pageNumber: Int? = 0,

	@SerializedName("RowspPage")
	val rowspPage: Int? = 0
)
