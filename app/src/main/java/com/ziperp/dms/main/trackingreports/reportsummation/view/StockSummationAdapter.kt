package com.ziperp.dms.main.trackingreports.reportsummation.view

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.main.trackingreports.reportsummation.model.SummationStockResponse
import kotlinx.android.synthetic.main.item_summation_stock.view.*

class StockSummationAdapter: BaseAdapter<SummationStockResponse.SummationStock>() {
    override fun getLayoutId(): Int = R.layout.item_summation_stock

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<SummationStockResponse.SummationStock> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: SummationStockResponse.SummationStock) {
            itemView.apply {
                tv_0.text = item.itemNm
                tv_1.text = item.stkUoMNm
                tv_2.text = item.lotNo
                tv_3.text = item.expiryDate.reformatDate()
                tv_4.text = item.stkCntQty.toString()
            }
        }
    }
}