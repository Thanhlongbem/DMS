package com.ziperp.dms.main.personalreport.salesresult.view

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.format
import com.ziperp.dms.main.personalreport.salesresult.model.CustomerResultResponse
import com.ziperp.dms.main.personalreport.salesresult.model.ItemResultResponse
import kotlinx.android.synthetic.main.item_your_sales_result.view.*

class YourSalesResultAdapter(): BaseAdapter<Any>() {

    override fun getLayoutId(): Int = R.layout.item_your_sales_result

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<Any> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: Any) {
            var position = 1
            itemView.apply {
                if (item is CustomerResultResponse.CustomerResult) {
                    tv_0.text = "${position++}"
                    tv_1.text = item.customerNm
                    tv_2.text = item.quantity.toInt().toString()
                    tv_3.text = item.totAmount.format()
                }
                if (item is ItemResultResponse.ItemResult) {
                    tv_0.text = "${position++}"
                    tv_1.text = item.itemNm
                    tv_2.text = "${item.quantity.toInt()} ${item.uoMNm}"
                    tv_3.text = item.totAmount.format()
                }
            }
        }
    }
}