package com.ziperp.dms.main.trackingreports.picture.view

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.main.trackingreports.picture.model.TrackingPictureListResponse
import kotlinx.android.synthetic.main.item_tracking_picture.view.*

class TrackingPictureAdapter: BaseAdapter<TrackingPictureListResponse.VisitedCustomerPicture>() {
    override fun getLayoutId(): Int = R.layout.item_tracking_picture

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<TrackingPictureListResponse.VisitedCustomerPicture> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: TrackingPictureListResponse.VisitedCustomerPicture) {
            itemView.apply {
                tv_staff_name.text = "${item.staffNo} - ${item.staffNm}"
                tv_dept.text = item.deptNm
                tv_business_unit.text = item.masterLocNm
                tv_total_visited.text = item.cstVisited.toString()
                tv_has_pic.text = item.visitWithImg.toString()
                tv_no_pic.text = item.visitNoImg.toString()
                tv_total_pic.text = item.numberImg.toString()
            }
        }
    }

}