package com.ziperp.dms.main.customer.customerroute

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.getColor
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.main.customer.customerroute.model.CustomerRoute
import kotlinx.android.synthetic.main.item_customer_route.view.*

class CustomerRouteAdapter: BaseAdapter<CustomerRoute>() {
    override fun getLayoutId(): Int = R.layout.item_customer_route

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<CustomerRoute> {
        override fun bind(item: CustomerRoute) {
            itemView.apply {
                tv_route_name.text = item.routeNm
                tv_EiC.text = item.routeStaffNm
                tv_visit_day.text = String.format(R.string.str_visit_day.getString(), item.visitDayNm)

                if(item.isSynchonized){
                    tv_status.text = item.routeStsNm
                    when (item.routeSts) {
                        "0" -> {
                            tv_status.setTextColor(R.color.color_issued.getColor())
                            tv_status.setBackgroundResource(R.drawable.bg_vc_issued_state)
                        }
                        "1" -> {
                            tv_status.setTextColor(R.color.color_tag_opening.getColor())
                            tv_status.setBackgroundResource(R.drawable.bg_vc_opening_state)
                        }
                        else -> {
                            tv_status.setTextColor(R.color.color_unconfirm.getColor())
                            tv_status.setBackgroundResource(R.drawable.bg_vc_stop_state)
                        }

                    }
                }else{
                    tv_status.text = "Cached data"
                    tv_status.background.setTint(ContextCompat.getColor(context, R.color.color_tag_opening))
                    layout_visit_day.visibility = View.VISIBLE
                }
            }
        }
    }

    fun removeItem(position: Int) {
        data.removeAt(position)
        notifyDataSetChanged()
    }
}