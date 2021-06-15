package com.ziperp.dms.main.trackingreports.visitcustomer.view

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.main.trackingreports.visitcustomer.model.TrackingVisitCustomer
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.item_tracking_visit_customer.view.*

class TrackingVisitCustomerAdapter: BaseAdapter<TrackingVisitCustomer>() {

    override fun getLayoutId(): Int = R.layout.item_tracking_visit_customer

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<TrackingVisitCustomer> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: TrackingVisitCustomer) {
            itemView.apply {
                tv_staff_name.text = "${item.staffNo} - ${DataConvertUtils.convertTitle(item.staffNm)}"
                tv_not_visited.text = item.notVisit.toString()
                tv_in_route.text = item.visitOnRoute.toString()
                tv_not_in_route.text = item.visitNotOnRoute.toString()
                tv_have_image.text = item.visitWithImg.toString()
                tv_no_image.text = item.visitNoImg.toString()
                tv_have_order.text = item.visitWithOrder.toString()
                tv_not_order.text = item.visitNoOrder.toString()
                tv_right_location.text = item.numbVisitValid.toString()
                tv_wrong_location.text = item.numbVisitInvalid.toString()
            }
        }
    }
}