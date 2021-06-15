package com.ziperp.dms.common.table.model

import com.google.gson.annotations.SerializedName

data class SaleReportResponse(

	@SerializedName("Table")
	val table: List<ReportTableItem?>? = arrayListOf()
)

data class ReportTableItem(

	@SerializedName("txtUoM")
	val txtUoM: String? = "",

	@SerializedName("txtItemNo")
	val txtItemNo: String? = "",

	@SerializedName("fltResultQty")
	val fltResultQty: Double? = 0.0,

	@SerializedName("fltPlanAmt")
	val fltPlanAmt: Double? = 0.0,

	@SerializedName("txtItemNm")
	val txtItemNm: String? = "",

	@SerializedName("fltResultAmt")
	val fltResultAmt: Double? = 0.0,

	@SerializedName("txtItemSpec")
	val txtItemSpec: String? = "",

	@SerializedName("fltPlanQty")
	val fltPlanQty: Double? = 0.0
) {

}
