package com.ziperp.dms.common.model

class TableViewModel(private val allData: List<TableData>) {

    val cellList: List<List<Cell>>
        get() = cellListForSortingTest

    val rowHeaderList: List<RowHeader>
        get() = simpleRowHeaderList

    val columnHeaderList: List<ColumnHeader>
        get() = randomColumnHeaderList



    private val simpleRowHeaderList: List<RowHeader>
        get() {
            val list: MutableList<RowHeader> = ArrayList()
            for (index in allData.indices) {
                list.add(
                    RowHeader(
                        "id",
                        "Row $index"
                    )
                )
            }
            return list
        }

    private val randomColumnHeaderList: List<ColumnHeader>
        get() {
            val list: MutableList<ColumnHeader> = ArrayList()
            allData[0].data.forEach { (columnTitle, _) ->
                list.add(
                    ColumnHeader(
                        "id",
                        columnTitle
                    )
                )
            }
            return list
        }

    private val cellListForSortingTest: List<List<Cell>>
        get() {
            val list: MutableList<MutableList<Cell>> = ArrayList()
            for (rowData in allData) {
                val cellist: MutableList<Cell> = ArrayList()
                rowData.data.forEach { (_, value) ->
                    cellist.add(Cell("id", value))
                }
                list.add(cellist)
            }
            return list
        }

}