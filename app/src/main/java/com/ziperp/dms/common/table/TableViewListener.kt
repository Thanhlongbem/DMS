package com.ziperp.dms.common.table

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.listener.ITableViewListener
import com.ziperp.dms.common.holder.ColumnHeaderViewHolder

class TableViewListener(tableView: TableView) :
    ITableViewListener {
    private val mContext: Context
    private val mTableView: TableView

    /**
     * Called when user click any cell item.
     *
     * @param cellView : Clicked Cell ViewHolder.
     * @param column   : X (Column) position of Clicked Cell item.
     * @param row      : Y (Row) position of Clicked Cell item.
     */
    override fun onCellClicked(cellView: ViewHolder, column: Int, row: Int) {

        // Do what you want.
    }

    /**
     * Called when user double click any cell item.
     *
     * @param cellView : Clicked Cell ViewHolder.
     * @param column   : X (Column) position of Clicked Cell item.
     * @param row      : Y (Row) position of Clicked Cell item.
     */
    override fun onCellDoubleClicked(
        cellView: ViewHolder,
        column: Int,
        row: Int
    ) {
        // Do what you want.
        showToast("Cell $column $row has been double clicked.")
    }

    /**
     * Called when user long press any cell item.
     *
     * @param cellView : Long Pressed Cell ViewHolder.
     * @param column   : X (Column) position of Long Pressed Cell item.
     * @param row      : Y (Row) position of Long Pressed Cell item.
     */
    override fun onCellLongPressed(
        cellView: ViewHolder, column: Int,
        row: Int
    ) {
        // Do What you want
        showToast("Cell $column $row has been long pressed.")
    }

    /**
     * Called when user click any column header item.
     *
     * @param columnHeaderView : Clicked Column Header ViewHolder.
     * @param column           : X (Column) position of Clicked Column Header item.
     */
    override fun onColumnHeaderClicked(
        columnHeaderView: ViewHolder,
        column: Int
    ) {
        // Do what you want.
    }

    /**
     * Called when user double click any column header item.
     *
     * @param columnHeaderView : Clicked Column Header ViewHolder.
     * @param column           : X (Column) position of Clicked Column Header item.
     */
    override fun onColumnHeaderDoubleClicked(
        columnHeaderView: ViewHolder,
        column: Int
    ) {
        // Do what you want.
        showToast("Column header  $column has been double clicked.")
    }

    /**
     * Called when user long press any column header item.
     *
     * @param columnHeaderView : Long Pressed Column Header ViewHolder.
     * @param column           : X (Column) position of Long Pressed Column Header item.
     */
    override fun onColumnHeaderLongPressed(
        columnHeaderView: ViewHolder,
        column: Int
    ) {
        if (columnHeaderView is ColumnHeaderViewHolder) {
            // Create Long Press Popup
            val popup = ColumnHeaderLongPressPopup(
                columnHeaderView as ColumnHeaderViewHolder, mTableView
            )
            // Show
            popup.show()
        }
    }

    /**
     * Called when user click any Row Header item.
     *
     * @param rowHeaderView : Clicked Row Header ViewHolder.
     * @param row           : Y (Row) position of Clicked Row Header item.
     */
    override fun onRowHeaderClicked(rowHeaderView: ViewHolder, row: Int) {
        // Do whatever you want.
    }

    /**
     * Called when user double click any Row Header item.
     *
     * @param rowHeaderView : Clicked Row Header ViewHolder.
     * @param row           : Y (Row) position of Clicked Row Header item.
     */
    override fun onRowHeaderDoubleClicked(rowHeaderView: ViewHolder, row: Int) {
        // Do whatever you want.
        showToast("Row header $row has been double clicked.")
    }

    /**
     * Called when user long press any row header item.
     *
     * @param rowHeaderView : Long Pressed Row Header ViewHolder.
     * @param row           : Y (Row) position of Long Pressed Row Header item.
     */
    override fun onRowHeaderLongPressed(rowHeaderView: ViewHolder, row: Int) {

        // Create Long Press Popup
        val popup = RowHeaderLongPressPopup(
            rowHeaderView,
            mTableView
        )
        // Show
        popup.show()
    }

    private fun showToast(p_strMessage: String) {
        Toast.makeText(mContext, p_strMessage, Toast.LENGTH_SHORT).show()
    }

    init {
        mContext = tableView.context
        mTableView = tableView
    }
}
