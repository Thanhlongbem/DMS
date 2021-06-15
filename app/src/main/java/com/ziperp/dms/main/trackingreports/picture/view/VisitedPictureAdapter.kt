package com.ziperp.dms.main.trackingreports.picture.view

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.trackingreports.picture.model.TrackingPictureDetailResponse
import com.ziperp.dms.utils.AppUtil
import kotlinx.android.synthetic.main.item_customer_image_grid.view.*
import kotlinx.android.synthetic.main.item_customer_image_list.view.*

class VisitedPictureAdapter: BaseAdapter<TrackingPictureDetailResponse.Picture>() {
    override fun getLayoutId(): Int = R.layout.item_report_picture

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<TrackingPictureDetailResponse.Picture> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: TrackingPictureDetailResponse.Picture) {
            itemView.apply {
                AppUtil.loadImageFromURL(context, Constants.API_BASE_PATH + item.fileURL, if(imgLinear != null) imgLinear else imgGrid)
            }
        }
    }
}