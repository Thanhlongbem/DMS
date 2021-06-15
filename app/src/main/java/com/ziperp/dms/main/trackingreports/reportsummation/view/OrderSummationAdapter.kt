package com.ziperp.dms.main.trackingreports.reportsummation.view

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.getColor
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.main.trackingreports.reportsummation.model.SummationOrderResponse
import kotlinx.android.synthetic.main.item_summation_order.view.*

class OrderSummationAdapter: BaseAdapter<SummationOrderResponse.SummationOrder>() {
    override fun getLayoutId(): Int = R.layout.item_summation_order

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<SummationOrderResponse.SummationOrder> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: SummationOrderResponse.SummationOrder) {
            itemView.apply {
                tv_title.text = item.orderNo
                tv_staff.text = item.salesman
                tv_time.text = item.orderDate.reformatDate()
                tv_amount.text = item.totalAmt.format()

                tv_status.text = item.orderStatusNm
                when(item.orderStatus) {
                    0 -> {
                        tv_status.setTextColor(R.color.color_tag_opening.getColor())
                        tv_status.setBackgroundResource(R.drawable.bg_vc_opening_state)
                    }
                    1 -> {
                        tv_status.setTextColor(R.color.color_purple.getColor())
                        tv_status.setBackgroundResource(R.drawable.bg_vc_confirmed_state)
                    }
                    2 -> {

                    }
                    3 -> {
                        tv_status.setTextColor(R.color.color_issued.getColor())
                        tv_status.setBackgroundResource(R.drawable.bg_vc_issued_state)
                    }
                    4 -> {

                    }
                }
            }
        }
    }
}