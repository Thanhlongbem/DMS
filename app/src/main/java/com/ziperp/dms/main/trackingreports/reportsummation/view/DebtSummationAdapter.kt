package com.ziperp.dms.main.trackingreports.reportsummation.view

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.getColor
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.main.trackingreports.reportsummation.model.SummationDebtResponse
import kotlinx.android.synthetic.main.item_summation_debt.view.*

class DebtSummationAdapter: BaseAdapter<SummationDebtResponse.SummationDebt>() {
    override fun getLayoutId(): Int = R.layout.item_summation_debt

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<SummationDebtResponse.SummationDebt> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: SummationDebtResponse.SummationDebt) {
            itemView.apply {
                tv_order_no.text = item.taxInvNo
                tv_amount.text = item.totalAmtBc.format()
                tv_time.text = item.invDate.reformatDate()
                tv_paid_amount.text = "${item.collectedBc.format()}/${item.uncollectedBc.format()}"

                tv_status.text = item.invStatus
                when(item.invStatusCode) {
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