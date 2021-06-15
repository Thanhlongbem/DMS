package com.ziperp.dms.main.visitcustomer.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.*
import com.ziperp.dms.main.visitcustomer.model.SaleOrderVisit
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.item_sale_order_of_visit_customer.view.*

class SaleOrderOfVisitInfoAdapter: BaseAdapter<SaleOrderVisit>(){

    override fun getLayoutId(): Int = R.layout.item_sale_order_of_visit_customer

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<SaleOrderVisit>{
        override fun bind(item: SaleOrderVisit){
            itemView.apply {
                tv_saleOrderName.text = "${DataConvertUtils.convertTitle(item.orderNo)} - ${item.orderDate.reformatDate()}"
                tv_customer_name.text = item.txtCustNm.ifEmptyLetBe("N/A")
                tv_saleOrderPrice.text = item.fltTotalAmt.format()
                tv_noteContent.text = item.remark.ifEmptyLetBe("N/A")
                tv_state.text = if (item.orderStatus == 0) R.string.str_opening.getString() else R.string.str_confirmed.getString()
                tv_state.setTextColor(if (item.orderStatus == 0) R.color.color_tag_opening.getColor() else R.color.color_purple.getColor())
                tv_state.setBackgroundResource(if (item.orderStatus == 0) R.drawable.bg_vc_opening_state else R.drawable.bg_vc_confirmed_state)
            }
        }
    }

}