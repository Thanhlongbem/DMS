package com.ziperp.dms.common.model

import com.google.gson.annotations.SerializedName

data class TrackingResponse(

	@SerializedName("Table")
	val table: List<TableItem> = arrayListOf()
)

data class TableItem(

	@SerializedName("Status")
	val status: String? = "",

	@SerializedName("TimeLogPos")
	val timeLogPos: String? = "",

	@SerializedName("StaffId")
	val staffId: String? = "",

	@SerializedName("ErrMsg")
	val errMsg: String? = "",

	@SerializedName("ErrCd")
	val errCd: String? = ""
)
