package com.ziperp.dms.main.personalreport.visitdetail.view

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.ifEmptyLetBe
import com.ziperp.dms.main.personalreport.visitdetail.model.NotVisitedResponse
import kotlinx.android.synthetic.main.item_customer_not_visited.view.*

class NotVisitedAdapter: BaseAdapter<NotVisitedResponse.NotVisitedCustomer>() {
    override fun getLayoutId(): Int = R.layout.item_customer_not_visited

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<NotVisitedResponse.NotVisitedCustomer> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: NotVisitedResponse.NotVisitedCustomer) {
            itemView.apply {
                tv_customer_name.text = item.cstNm
                tv_location.text = item.address
                tv_phone.text = item.repPersonPhone.ifEmptyLetBe("N/A")
                tv_representation.text = item.repPerson.ifEmptyLetBe("N/A")
                tv_last_visited.text = "${item.lastVisitedBy.ifEmptyLetBe("N/A")} - ${item.lastTimeVisit.ifEmptyLetBe("N/A")}"
            }
        }
    }
}