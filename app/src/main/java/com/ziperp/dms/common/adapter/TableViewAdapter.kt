package com.ziperp.dms.common.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.evrencoskun.tableview.sort.SortState
import com.ziperp.dms.R
import com.ziperp.dms.common.holder.CellViewHolder
import com.ziperp.dms.common.holder.ColumnHeaderViewHolder
import com.ziperp.dms.common.holder.RowHeaderViewHolder
import com.ziperp.dms.common.model.Cell
import com.ziperp.dms.common.model.ColumnHeader
import com.ziperp.dms.common.model.RowHeader
import com.ziperp.dms.extensions.getColor

class TableViewAdapter : AbstractTableAdapter<ColumnHeader?, RowHeader?, Cell?>() {

    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layout: View
        layout = inflater.inflate(R.layout.table_view_cell_layout, parent, false)

        return CellViewHolder(layout)
    }

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: Cell?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        val viewHolder = holder as CellViewHolder
        if((rowPosition + 1) % 2 == 1){
            viewHolder.cellContainer.setBackgroundColor(R.color.color_odd_row.getColor())
        }else{
            viewHolder.cellContainer.setBackgroundColor(R.color.cell_background_color.getColor())
        }
        viewHolder.setCell(cellItemModel)
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_column_header_layout, parent, false)

        // Create a ColumnHeader ViewHolder
        return ColumnHeaderViewHolder(layout, tableView)
    }

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        columnHeaderItemModel: ColumnHeader?,
        columnPosition: Int
    ) {

        // Get the holder to update cell item text
        val columnHeaderViewHolder: ColumnHeaderViewHolder = holder as ColumnHeaderViewHolder
        columnHeaderViewHolder.setColumnHeader(columnHeaderItemModel)
    }

    override fun onCreateRowHeaderViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        // Get Row Header xml Layout
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.table_view_row_header_layout, parent, false)

        // Create a Row Header ViewHolder
        return RowHeaderViewHolder(layout)
    }

    override fun onBindRowHeaderViewHolder(holder: AbstractViewHolder, rowHeaderItemModel: RowHeader?, rowPosition: Int
    ) {
        // Get the holder to update row header item text
        val rowHeaderViewHolder: RowHeaderViewHolder = holder as RowHeaderViewHolder
        rowHeaderViewHolder.rowHeaderTextview.text = rowHeaderItemModel?.data.toString()
        if((rowPosition + 1) % 2 == 1){
            rowHeaderViewHolder.root.setBackgroundColor(R.color.color_odd_row.getColor())
        }else{
            rowHeaderViewHolder.root.setBackgroundColor(R.color.cell_background_color.getColor())
        }
    }

    override fun onCreateCornerView(parent: ViewGroup): View {
        // Get Corner xml layout
        val corner = LayoutInflater.from(parent.context).inflate(R.layout.table_view_corner_layout, parent, false)
        corner.setOnClickListener {
            val sortState = this@TableViewAdapter.tableView.rowHeaderSortingStatus
            if (sortState != SortState.ASCENDING) {
                Log.d("TableViewAdapter", "Order Ascending")
                this@TableViewAdapter.tableView.sortRowHeader(SortState.ASCENDING)
            } else {
                Log.d("TableViewAdapter", "Order Descending")
                this@TableViewAdapter.tableView.sortRowHeader(SortState.DESCENDING)
            }
        }
        return corner
    }

    override fun getColumnHeaderItemViewType(position: Int): Int {
        return 0
    }

    override fun getRowHeaderItemViewType(position: Int): Int {
        return 0
    }

    override fun getCellItemViewType(column: Int): Int {
        return 0
    }

    companion object {
        private val LOG_TAG = TableViewAdapter::class.java.simpleName
    }

}
