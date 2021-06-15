package com.ziperp.dms.common.holder

import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.evrencoskun.tableview.ITableView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder
import com.evrencoskun.tableview.sort.SortState
import com.ziperp.dms.R
import com.ziperp.dms.common.model.ColumnHeader

class ColumnHeaderViewHolder(itemView: View, private val tableView: ITableView?) :
    AbstractSorterViewHolder(itemView) {
    private val columnHeaderContainer: LinearLayout = itemView.findViewById(R.id.column_header_container)
    private val columnHeaderTextview: TextView = itemView.findViewById(R.id.column_header_textView)
    private val columnHeaderSortButton: ImageButton = itemView.findViewById(R.id.column_header_sortButton)

    /**
     * This method is calling from onBindColumnHeaderHolder on TableViewAdapter
     */
    fun setColumnHeader(columnHeader: ColumnHeader?) {
        columnHeaderTextview.text = columnHeader?.data.toString()

        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can remove them.

        // It is necessary to remeasure itself.
        columnHeaderContainer.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        columnHeaderTextview.requestLayout()
    }

    private val mSortButtonClickListener =
        View.OnClickListener {
            if (sortState == SortState.ASCENDING) {
                tableView!!.sortColumn(adapterPosition, SortState.DESCENDING)
            } else if (sortState == SortState.DESCENDING) {
                tableView!!.sortColumn(adapterPosition, SortState.ASCENDING)
            } else {
                // Default one
                tableView!!.sortColumn(adapterPosition, SortState.DESCENDING)
            }
        }

    override fun onSortingStatusChanged(sortState: SortState) {
        Log.e(
            LOG_TAG,
            " + onSortingStatusChanged : x:  " + adapterPosition + " old state "
                    + getSortState() + " current state : " + sortState + " visiblity: " +
                    columnHeaderSortButton.visibility
        )
        super.onSortingStatusChanged(sortState)

        // It is necessary to remeasure itself.
        columnHeaderContainer.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        controlSortState(sortState)
        Log.e(
            LOG_TAG,
            " - onSortingStatusChanged : x:  " + adapterPosition + " old state "
                    + getSortState() + " current state : " + sortState + " visiblity: " +
                    columnHeaderSortButton.visibility
        )
        columnHeaderTextview.requestLayout()
        columnHeaderSortButton.requestLayout()
        columnHeaderContainer.requestLayout()
        itemView.requestLayout()
    }

    private fun controlSortState(sortState: SortState) {
        if (sortState == SortState.ASCENDING) {
            columnHeaderSortButton.visibility = View.VISIBLE
        } else if (sortState == SortState.DESCENDING) {
            columnHeaderSortButton.visibility = View.VISIBLE
        } else {
            columnHeaderSortButton.visibility = View.INVISIBLE
        }
    }

    companion object {
        private val LOG_TAG = ColumnHeaderViewHolder::class.java.simpleName
    }

    init {

        // Set click listener to the sort button
        columnHeaderSortButton.setOnClickListener(mSortButtonClickListener)
    }
}
