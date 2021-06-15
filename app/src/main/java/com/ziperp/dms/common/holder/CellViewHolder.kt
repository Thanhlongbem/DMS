package com.ziperp.dms.common.holder

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.ziperp.dms.R
import com.ziperp.dms.common.model.Cell

class CellViewHolder(itemView: View) : AbstractViewHolder(itemView) {
    val cellTextview: TextView = itemView.findViewById(R.id.cell_data)
    val cellContainer: LinearLayout = itemView.findViewById(R.id.cell_container)
    fun setCell(cell: Cell?) {
        cellTextview.text = cell?.data.toString()

        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can ignore them.

        // It is necessary to remeasure itself.
        cellContainer.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        cellTextview.requestLayout()
    }

}