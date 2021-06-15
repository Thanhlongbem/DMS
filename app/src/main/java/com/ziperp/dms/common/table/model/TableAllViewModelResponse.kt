package com.ziperp.dms.common.table.model

import com.google.gson.annotations.SerializedName

data class TableAllViewModelResponse(

	@SerializedName("Table")
	val table: List<TableItem?>? = arrayListOf(),

	@SerializedName("Table1")
	val table1: List<Table1Item?>? = arrayListOf()
)

data class Table1Item(

	@SerializedName("ColumnId")
	val columnId: String = "",

	@SerializedName("ColumnNm")
	val columnNm: String = "",

	@SerializedName("ColCtrlKey")
	val colCtrlKey: String = "",

	@SerializedName("ColTitleLen")
	val colTitleLen: Int = 0,

	@SerializedName("CtrlViewPermit")
	val ctrlViewPermit: Int = 0,

	@SerializedName("ColParentYn")
	val colParentYn: Int = 0,

	@SerializedName("ColDataType")
	val colDataType: String = "",

	@SerializedName("ColPermission")
	val colPermission: Int = 0,

	@SerializedName("ColDecPoint")
	val colDecPoint: Int = 0,

	@SerializedName("ColSortOrder")
	val colSortOrder: Int = 0,

	@SerializedName("ColShowYn")
	val colShowYn: Int = 0,

	@SerializedName("ColWidth")
	val colWidth: Int = 0,

	@SerializedName("ColTotType")
	val colTotType: String = "",

	@SerializedName("ColParentId")
	val colParentId: String = "",

	@SerializedName("ColDetailYn")
	val colDetailYn: Any? = null,

	@SerializedName("FormId")
	val formId: String = "",

	@SerializedName("GridId")
	val gridId: Int = 0
)

data class TableItem(

	@SerializedName("Grid3DynYn")
	val grid3DynYn: Int = 0,

	@SerializedName("Grid1EditYn")
	val grid1EditYn: Int = 0,

	@SerializedName("Grid1ScaleX")
	val grid1ScaleX: Int = 0,

	@SerializedName("Grid3PopupYn")
	val grid3PopupYn: Int = 0,

	@SerializedName("Grid1TotalRowYn")
	val grid1TotalRowYn: Int = 0,

	@SerializedName("Grid3TotalRowYn")
	val grid3TotalRowYn: Int = 0,

	@SerializedName("Grid1ScaleY")
	val grid1ScaleY: Int = 0,

	@SerializedName("Grid1ChildId")
	val grid1ChildId: String = "",

	@SerializedName("Grid3ChildId")
	val grid3ChildId: String = "",

	@SerializedName("Grid2ChildId")
	val grid2ChildId: String = "",

	@SerializedName("GridType")
	val gridType: Int = 0,

	@SerializedName("Grid1PivotYn")
	val grid1PivotYn: Int = 0,

	@SerializedName("Grid2PivotYn")
	val grid2PivotYn: Int = 0,

	@SerializedName("Grid3PivotYn")
	val grid3PivotYn: Int = 0,

	@SerializedName("Grid1DynYn")
	val grid1DynYn: Int = 0,

	@SerializedName("G2UseMstDetailYn")
	val g2UseMstDetailYn: Int = 0,

	@SerializedName("Grid2ScaleY")
	val grid2ScaleY: Int = 0,

	@SerializedName("Grid3OffsetX")
	val grid3OffsetX: Int = 0,

	@SerializedName("Grid1PopupYn")
	val grid1PopupYn: Int = 0,

	@SerializedName("Grid2ScaleX")
	val grid2ScaleX: Int = 0,

	@SerializedName("Grid3OffsetY")
	val grid3OffsetY: Int = 0,

	@SerializedName("Grid2PopupYn")
	val grid2PopupYn: Int = 0,

	@SerializedName("Grid3EditYn")
	val grid3EditYn: Int = 0,

	@SerializedName("Grid1Title")
	val grid1Title: String = "",

	@SerializedName("Grid1OffsetX")
	val grid1OffsetX: Int = 0,

	@SerializedName("G1UseMstDetailYn")
	val g1UseMstDetailYn: Int = 0,

	@SerializedName("Grid1OffsetY")
	val grid1OffsetY: Int = 0,

	@SerializedName("Grid2OffsetY")
	val grid2OffsetY: Int = 0,

	@SerializedName("Grid2OffsetX")
	val grid2OffsetX: Int = 0,

	@SerializedName("Grid2DynYn")
	val grid2DynYn: Int = 0,

	@SerializedName("Grid2Title")
	val grid2Title: String = "",

	@SerializedName("Grid2TotalRowYn")
	val grid2TotalRowYn: Int = 0,

	@SerializedName("GridCount")
	val gridCount: Int = 0,

	@SerializedName("Grid3Title")
	val grid3Title: String = "",

	@SerializedName("Grid3ScaleX")
	val grid3ScaleX: Int = 0,

	@SerializedName("G3UseMstDetailYn")
	val g3UseMstDetailYn: Int = 0,

	@SerializedName("Grid3ScaleY")
	val grid3ScaleY: Int = 0,

	@SerializedName("Grid2EditYn")
	val grid2EditYn: Int = 0,

	@SerializedName("TabId")
	val tabId: Int = 0
)
