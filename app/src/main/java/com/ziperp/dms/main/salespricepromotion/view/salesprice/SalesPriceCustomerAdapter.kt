package com.ziperp.dms.main.salespricepromotion.view.salesprice

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.main.salespricepromotion.model.PromotionInfoResponse
import com.ziperp.dms.main.salespricepromotion.model.SalesPriceInfoResponse
import kotlinx.android.synthetic.main.item_sales_price_customer.view.*

class SalesPriceCustomerAdapter : BaseAdapter<Any>() {

    override fun getLayoutId(): Int = R.layout.item_sales_price_customer

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<Any> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: Any) {
            itemView.apply {
                when(item) {
                    is SalesPriceInfoResponse.UnitApplied -> tv_content.text = item.bizUnitNm
                    is SalesPriceInfoResponse.Customer -> tv_content.text = item.cstNm
                    is PromotionInfoResponse.UnitApplied -> tv_content.text = item.strBizUnitApplied
                    is PromotionInfoResponse.Customer -> tv_content.text = item.strCustomerApplied
                }
            }
        }
    }
}