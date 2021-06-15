package com.ziperp.dms.main.trackingreports.visitcustomer.view

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.getColor
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.ifEmptyLetBe
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.main.trackingreports.visitcustomer.model.TrackingVSDetail
import kotlinx.android.synthetic.main.item_tracking_visit_cst_detail.view.*

class TrackingVSDetailAdapter: BaseAdapter<TrackingVSDetail>() {

    override fun getLayoutId(): Int = R.layout.item_tracking_visit_cst_detail

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<TrackingVSDetail> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: TrackingVSDetail) {
            itemView.apply {
                tv_visit_customer.text = item.cstNm
                tv_location.text = item.visitPosNm
                tv_visit_day.text = item.visitDay.reformatDate()
                tv_checkin_time.text = item.checkInTime.ifEmptyLetBe("N/A")
                tv_check_out_time.text = item.checkOutTime.ifEmptyLetBe("N/A")
                tv_duration.text = item.durCheckIn.ifEmptyLetBe("N/A")
                tv_sales_order_amount.text = "${item.amountOrder}/${item.numOrders}"
                tv_images_amount.text = item.numPhotos.toString()

                img_route_status.isSelected = item.routeStsNm == "Đúng tuyến"
                tv_route_status.isSelected = item.routeStsNm == "Đúng tuyến"
                tv_route_status.text = item.routeStsNm
                img_location_status.isSelected = item.visitSts == 1
                tv_location_status.isSelected = item.visitSts == 1
                tv_location_status.text = item.visitStsNm

                if (item.openYn == 0) { //Open
                    tv_status.text = R.string.str_opening.getString()
                    tv_status.setTextColor(R.color.bg_tag_confirmed.getColor())
                    tv_status.setBackgroundResource(R.drawable.bg_vc_issued_state)
                } else {
                    tv_status.text = R.string.str_close.getString()
                    tv_status.setTextColor(R.color.color_red.getColor())
                    tv_status.setBackgroundResource(R.drawable.bg_vc_stop_state)
                }

            }
        }
    }
}