package com.ziperp.dms.main.salespricepromotion.view.salesprice

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.format
import com.ziperp.dms.main.salespricepromotion.model.SalesPriceInfoResponse
import kotlinx.android.synthetic.main.item_sales_price_item.view.*

class SalesPriceItemAdapter : BaseAdapter<SalesPriceInfoResponse.Item>() {

    override fun getLayoutId(): Int = R.layout.item_sales_price_item

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<SalesPriceInfoResponse.Item> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: SalesPriceInfoResponse.Item) {
            itemView.apply {
                tv_title.text = "${item.itemNo} - ${item.itemNm}"
                tv_sales_unit.text = item.uoMNm
                tv_sales_qty.text = item.salesQty.toString()
                tv_sales_price.text = item.salesPrice.format()
                tv_vat_rate.text = "${item.vatRate} %"
            }
        }
    }
}