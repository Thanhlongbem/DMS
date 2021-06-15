package com.ziperp.dms.main.saleorder.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.*
import com.ziperp.dms.main.saleorder.model.SalesOrderResponse
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.item_sale_order.view.*

class SaleOrderAdapter : BaseAdapter<SalesOrderResponse.SaleOrder>() {

    override fun getLayoutId(): Int = R.layout.item_sale_order
    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<SalesOrderResponse.SaleOrder> {
        override fun bind(item: SalesOrderResponse.SaleOrder) {
            itemView.apply {
                tv_title.text = "${DataConvertUtils.convertTitle(item.orderNo)} - ${item.orderDate.reformatDate()}"
                tv_place.text = item.txtCustNm
                tv_amount.text = (item.fltTotalAmt).format() + " " + item.txtCurrency
                tv_location.text = item.txtDelvAddr.ifEmptyLetBe("N/A")
                tv_phone.text = item.txtCstPhone.ifEmptyLetBe("N/A")
                tv_owner.text = item.txtCstContact.ifEmptyLetBe("N/A")
                tv_remark.text = item.remark.ifEmptyLetBe("N/A")

                when(item.orderStatus) {
                    0 -> {
                        tv_status.text = R.string.str_opening.getString()
                        tv_status.setTextColor(R.color.color_tag_opening.getColor())
                        tv_status.setBackgroundResource(R.drawable.bg_vc_opening_state)
                    }
                    1 -> {
                        tv_status.text = R.string.str_confirm.getString()
                        tv_status.setTextColor(R.color.color_purple.getColor())
                        tv_status.setBackgroundResource(R.drawable.bg_vc_confirmed_state)
                    }
                    2 -> {

                    }
                    3 -> {
                        tv_status.text = R.string.str_issued.getString()
                        tv_status.setTextColor(R.color.color_issued.getColor())
                        tv_status.setBackgroundResource(R.drawable.bg_vc_issued_state)
                    }
                    4 -> {

                    }
                }
                if (item.chkSOStop == 1) {
                    tv_status.text = R.string.str_stop.getString()
                    tv_status.setTextColor(R.color.color_tag_stop.getColor())
                    tv_status.setBackgroundResource(R.drawable.bg_vc_stop_state)
                }
            }
        }
    }
}