package com.ziperp.dms.main.trackingreports.salesonroute.view

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.format
import com.ziperp.dms.main.trackingreports.salesonroute.model.SalesOnRouteResponse
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.item_sales_on_route.view.*

class SalesOnRouteAdapter: BaseAdapter<SalesOnRouteResponse.SalesOnRoute>() {

    override fun getLayoutId(): Int = R.layout.item_sales_on_route

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<SalesOnRouteResponse.SalesOnRoute> {
        override fun bind(item: SalesOnRouteResponse.SalesOnRoute) {
            itemView.apply {
                tv_0.text = DataConvertUtils.convertTitle(item.deptStaffNm)
                tv_1.text = DataConvertUtils.convertTitle(item.totalCst).toDouble().format()
                tv_2.text = DataConvertUtils.convertTitle(item.totalCstOrder).toDouble().format()
                tv_3.text = DataConvertUtils.convertTitle(item.totalSO).toDouble().format()
                tv_4.text = DataConvertUtils.convertTitle(item.totalOrderQty?: "0").toDouble().format()

                if (item.deptStaffNm.contains("#@$")) layout_item.setBackgroundColor(Color.parseColor(item.deptStaffNm.substringAfterLast("#@$")))
                else layout_item.setBackgroundColor(Color.parseColor("#ffffff"))
            }
        }
    }
}