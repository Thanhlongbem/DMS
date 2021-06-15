package com.ziperp.dms.main.trackingreports.salesresult.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.extensions.format
import com.ziperp.dms.main.trackingreports.salesresult.model.SalesSummationResponse
import com.ziperp.dms.utils.AppUtil
import kotlinx.android.synthetic.main.item_summation_result.view.*

class SummationAdapter: BaseAdapter<SalesSummationResponse.Staff>() {
    override fun getLayoutId(): Int = R.layout.item_summation_result

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<SalesSummationResponse.Staff> {
        override fun bind(item: SalesSummationResponse.Staff) {
            itemView.apply {
                tv_staff_name.text = item.staffNm
                tv_dept.text = item.deptNm
                tv_business_unit.text = item.businessUnit
                tv_sales_amount.text = item.totalAmt.format()
                tv_sales_quantity.text = item.totalQty.format()
                tv_sales_order.text = item.totSalesOrder.toString()

                AppUtil.loadImageFromURL(context, Constants.API_BASE_PATH + item.imgProfile, img_avatar, defaultImg = R.drawable.avata_default)
            }
        }
    }
}