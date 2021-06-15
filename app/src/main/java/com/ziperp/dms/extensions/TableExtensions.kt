package com.ziperp.dms.extensions

import com.evrencoskun.tableview.TableView
import com.ziperp.dms.common.adapter.TableViewAdapter
import com.ziperp.dms.common.model.TableViewModel
import com.ziperp.dms.common.table.TableViewListener

fun TableView.init(adapter: TableViewAdapter, viewModel: TableViewModel) {
    this.setAdapter(adapter)
    this.tableViewListener = TableViewListener(this)
    this.isIgnoreSelectionColors = true
    adapter.setAllItems(
        viewModel.columnHeaderList,
        viewModel.rowHeaderList,
        viewModel.cellList
    )
}