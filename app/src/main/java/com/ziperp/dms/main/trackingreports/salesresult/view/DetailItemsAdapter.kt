package com.ziperp.dms.main.trackingreports.salesresult.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.format
import com.ziperp.dms.main.trackingreports.salesresult.model.SalesDetailResponse
import kotlinx.android.synthetic.main.item_sales_detail.view.*

class DetailItemsAdapter: BaseAdapter<SalesDetailResponse.SalesDetail>() {
    override fun getLayoutId(): Int = R.layout.item_sales_detail

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<SalesDetailResponse.SalesDetail> {
        override fun bind(item: SalesDetailResponse.SalesDetail) {
            itemView.apply {
                tv_item.text = item.itemNm
                tv_quantity.text = "${item.quantity.format()} ${item.uoMNm}"
                tv_amount.text = item.totAmount.format()
            }
        }
    }
}