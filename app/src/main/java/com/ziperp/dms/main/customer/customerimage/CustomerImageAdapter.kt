package com.ziperp.dms.main.customer.customerimage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.camera.CustomerImage
import com.ziperp.dms.camera.CustomerImagesResponse
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.utils.AppUtil
import kotlinx.android.synthetic.main.item_customer_image_grid.view.*
import kotlinx.android.synthetic.main.item_customer_image_list.view.*

class CustomerImageAdapter: BaseAdapter<CustomerImage>() {

    private var viewMode: Boolean = true

    override fun getLayoutId(): Int = if (viewMode) R.layout.item_customer_image_list else R.layout.item_customer_image_grid

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<CustomerImage> {
        override fun bind(item: CustomerImage) {
            itemView.apply {
                tv_fileLength?.text = item.fileLength
                tv_NameLinear?.text = item.fileNm
                tv_nameGrid?.text = item.fileNm
                tv_TimeLinear?.text = "${item.keyNo} isVisit: ${item.isVisitCustomer} isSync ${item.isSynchonized}"
//                tv_TimeLinear?.text = ""item.attachedDate.reformatDate()
                tv_TimeGrid?.text = item.attachedDate.reformatDate()

                AppUtil.loadImageFromURL(context, Constants.API_BASE_PATH + item.download, if(imgLinear != null) imgLinear else imgGrid)
            }
        }
    }

    fun switchViewMode(mode: Boolean) {
        viewMode = mode
        getLayoutId()
        notifyDataSetChanged()
    }
}