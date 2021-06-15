package com.ziperp.dms.common.table

import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.sort.SortState
import com.ziperp.dms.R
import com.ziperp.dms.common.holder.ColumnHeaderViewHolder

class ColumnHeaderLongPressPopup(
    viewHolder: ColumnHeaderViewHolder,
    private val mTableView: TableView
) :
    PopupMenu(viewHolder.itemView.context, viewHolder.itemView),
    PopupMenu.OnMenuItemClickListener {
    private val mXPosition: Int = viewHolder.adapterPosition
    private fun initialize() {
        createMenuItem()
        changeMenuItemVisibility()
        setOnMenuItemClickListener(this)
    }

    private fun createMenuItem() {
        val context = mTableView.context
        this.menu.add(
            Menu.NONE,
            ASCENDING,
            0,
            context.getString(R.string.sort_ascending)
        )
        this.menu.add(
            Menu.NONE,
            DESCENDING,
            1,
            context.getString(R.string.sort_descending)
        )
        this.menu.add(
            Menu.NONE,
            HIDE_ROW,
            2,
            context.getString(R.string.hiding_row_sample)
        )
        this.menu.add(
            Menu.NONE,
            SHOW_ROW,
            3,
            context.getString(R.string.showing_row_sample)
        )
        this.menu.add(
            Menu.NONE,
            SCROLL_ROW,
            4,
            context.getString(R.string.scroll_to_row_position)
        )
        this.menu.add(
            Menu.NONE,
            SCROLL_ROW,
            0,
            "change width"
        )
        // add new one ...
    }

    private fun changeMenuItemVisibility() {
        // Determine which one shouldn't be visible
        val sortState = mTableView.getSortingStatus(mXPosition)
        if (sortState == SortState.UNSORTED) {
            // Show others
        } else if (sortState == SortState.DESCENDING) {
            // Hide DESCENDING menu item
            menu.getItem(1).isVisible = false
        } else if (sortState == SortState.ASCENDING) {
            // Hide ASCENDING menu item
            menu.getItem(0).isVisible = false
        }
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        // Note: item id is index of menu item..
        when (menuItem.itemId) {
            ASCENDING -> mTableView.sortColumn(
                mXPosition,
                SortState.ASCENDING
            )
            DESCENDING -> mTableView.sortColumn(
                mXPosition,
                SortState.DESCENDING
            )
            HIDE_ROW -> mTableView.hideRow(5)
            SHOW_ROW -> mTableView.showRow(5)
            SCROLL_ROW -> mTableView.scrollToRowPosition(5)
        }
        return true
    }

    companion object {
        // Menu Item constants
        private const val ASCENDING = 1
        private const val DESCENDING = 2
        private const val HIDE_ROW = 3
        private const val SHOW_ROW = 4
        private const val SCROLL_ROW = 5
    }

    init {
        initialize()
    }
}
