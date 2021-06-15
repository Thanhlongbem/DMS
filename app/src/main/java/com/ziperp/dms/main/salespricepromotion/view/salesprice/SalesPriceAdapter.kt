package com.ziperp.dms.main.salespricepromotion.view.salesprice

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.getColor
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.main.salespricepromotion.model.SalesPriceResponse
import kotlinx.android.synthetic.main.item_sales_price.view.*
import kotlinx.android.synthetic.main.item_sales_price.view.tv_status
import kotlinx.android.synthetic.main.item_sales_price.view.tv_title

class SalesPriceAdapter : BaseAdapter<SalesPriceResponse.SalesPrice>() {
    override fun getLayoutId(): Int = R.layout.item_sales_price

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<SalesPriceResponse.SalesPrice> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: SalesPriceResponse.SalesPrice) {
            itemView.apply {
                tv_title.text = item.priceListNm
                tv_price_unit.text = item.currency
                tv_period.text = "${item.startDate.reformatDate()} - ${item.endDate.reformatDate()}"
                tv_address.text = item.strStore
                tv_customer_type.text = item.strCustomer
                tv_remark.text = item.priceListDesc
                tv_status.text = item.activeStsNm
                tv_valid_status.text = item.validStatusNm
                when(item.activeSts) {
                    0 -> {
                        tv_status.setTextColor(R.color.color_tag_opening.getColor())
                        tv_status.setBackgroundResource(R.drawable.bg_vc_opening_state)
                    }
                    1 -> {
                        tv_status.setTextColor(R.color.color_issued.getColor())
                        tv_status.setBackgroundResource(R.drawable.bg_vc_issued_state)
                    }
                }
                when(item.validStatus) {
                    0 -> {
                        tv_valid_status.setTextColor(R.color.color_tag_stop.getColor())
                        tv_valid_status.setBackgroundResource(R.drawable.bg_vc_stop_state)
                    }
                    1 -> {
                        tv_valid_status.setTextColor(R.color.color_issued.getColor())
                        tv_valid_status.setBackgroundResource(R.drawable.bg_vc_issued_state)
                    }
                }
            }
        }
    }
}