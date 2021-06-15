package com.ziperp.dms.core.form.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DialogMetaResponse(

	@SerializedName("Table")
	val selections: List<ValueSelection> = arrayListOf(),

	@SerializedName("Table1")
	val columns: List<Column> = arrayListOf(),

	@SerializedName("Table2")
	val conditions: List<Condition> = arrayListOf(),

	@SerializedName("Table3")
	var params: List<Param> = arrayListOf()
){
	data class Param(

		@SerializedName("Item1")
		var item1: String = "",

		@SerializedName("Item2")
		var item2: String = "",

		@SerializedName("ParentCd")
		var parentCd: String = "",

		@SerializedName("DlgCd")
		var dlgCd: String = "",

		@SerializedName("Item10")
		var item10: String = "",

		@SerializedName("Item5")
		var item5: String = "",

		@SerializedName("Item6")
		var item6: String = "",

		@SerializedName("Item3")
		var item3: String = "",

		@SerializedName("CmbCd")
		var cmbCd: String = "",

		@SerializedName("Item4")
		var item4: String = "",

		@SerializedName("Item9")
		var item9: String = "",

		@SerializedName("Item7")
		var item7: String = "",

		@SerializedName("Item8")
		var item8: String = "",

		@SerializedName("JoinCd")
		var joinCd: String = ""
	): Serializable

	data class Condition(

		@SerializedName("SubCondNm")
		val subCondNm: String = "",

		@SerializedName("SubCondCd")
		val subCondCd: String = ""
	)

	data class Column(

		@SerializedName("ColumnId")
		val columnId: String = "",

		@SerializedName("ColumnNmDicId")
		val columnNmDicId: String = "",

		@SerializedName("ColumnNm")
		val columnNm: String = "",

		@SerializedName("ColKind")
		val colKind: String = "",

		@SerializedName("ColTitleLen")
		val colTitleLen: Int = 0,

		@SerializedName("ColDataType")
		val colDataType: String = "",

		@SerializedName("ColDecPoint")
		val colDecPoint: Int = 0,

		@SerializedName("ColSortOrder")
		val colSortOrder: Int = 0,

		@SerializedName("ColShowYn")
		val colShowYn: Int = 0,

		@SerializedName("ColWidth")
		val colWidth: Int = 0
	)

	data class ValueSelection(

		@SerializedName("SearchFirst")
		val searchFirst: String = "",

		@SerializedName("DlgQrySP")
		val dlgQrySP: String = "",

		@SerializedName("DlgNm")
		val dlgNm: String = "",

		@SerializedName("ColumnCode")
		val columnCode: String = "",

		@SerializedName("ColumnValue")
		val columnValue: String = ""
	)
}


