package com.ziperp.dms.main.user.adapter

import android.view.View
import android.view.View.OnTouchListener
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.camera.CustomerImage
import com.ziperp.dms.core.rest.TrackingRequest
import com.ziperp.dms.dao.FormControl
import com.ziperp.dms.extensions.toJsonString
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetail
import com.ziperp.dms.main.customer.customerroute.model.CustomerRoute
import com.ziperp.dms.main.timekeeping.model.TimeKeeping
import com.ziperp.dms.main.visitcustomer.model.VisitCustomer
import kotlinx.android.synthetic.main.item_cached_request.view.*


class CachedDataAdapter : BaseAdapter<Any>() {

    var syncFunction: ((position: Int)-> Unit)? = null

    override fun getLayoutId(): Int = R.layout.item_cached_request

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<Any> {
        override fun bind(item: Any) {
            itemView.view_delete.setOnClickListener {
                deleteFunction?.invoke(absoluteAdapterPosition)
            }
            itemView.btn_delete.setOnClickListener {
                deleteFunction?.invoke(absoluteAdapterPosition)
            }
            itemView.child_scroll.setOnTouchListener(OnTouchListener { v, event -> // Disallow the touch request for parent scroll on touch of child view
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            })

            itemView.view_sync.setOnClickListener { syncFunction?.invoke(absoluteAdapterPosition) }

            when(item){
                is TimeKeeping -> {
                    itemView.apply {
                        txt_request_name.text = "${item.timeKeepNo} isSyncCheckIn: ${item.isSynchonizedCheckIn} isSyncCheckOut: ${item.isSynchonizedCheckOut}"
                        txt_request_body.text = "${item.toJsonString()}"
                    }
                }
                is TrackingRequest ->{
                    itemView.apply {
                        txt_request_name.text = "Tracking Data ${item.TimeLogPos} "
                        txt_request_body.text = "${item.toJsonString()}"
                    }
                }
                is FormControl -> {
                    itemView.apply {
                        txt_request_name.text = "${item.controlFormId}"
                        txt_request_body.text = "${item.data}"
                    }
                }
                is CustomerDetail -> {
                    itemView.apply {
                        txt_request_name.text = "${item.cstCd} isSynced: ${item.isSynchonized} ${item.cstNm}"
                        txt_request_body.text = "${item.toJsonString()}"
                    }
                }

                is CustomerImage -> {
                    itemView.apply {
                        txt_request_name.text = "${item.keyNo} isSynced: ${item.isSynchonized} isVisit: ${item.isVisitCustomer}"
                        txt_request_body.text = "${item.toJsonString()}"
                    }
                }

                is CustomerRoute -> {
                    itemView.apply {
                        txt_request_name.text = "${item.cstCd} isSynced: ${item.isSynchonized} ${item.routeNo} ${item.routeNm}"
                        txt_request_body.text = "${item.toJsonString()}"
                    }
                }

                is VisitCustomer -> {
                    itemView.apply {
                        txt_request_name.text = "VisitNo: ${item.cstVisitNo} CstCd: ${item.cstCd}"
                        txt_request_body.text = "${item.toJsonString()}"
                    }
                }
            }
        }
    }

    fun removeAt(position: Int){
        data.removeAt(position)
        notifyDataSetChanged()
    }


}