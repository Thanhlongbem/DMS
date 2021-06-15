package com.ziperp.dms.common.holder

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.ziperp.dms.R

class RowHeaderViewHolder(itemView: View) :
    AbstractViewHolder(itemView) {
    val rowHeaderTextview: TextView = itemView.findViewById(R.id.row_header_textview)
    val root: RelativeLayout = itemView.findViewById(R.id.root)

}