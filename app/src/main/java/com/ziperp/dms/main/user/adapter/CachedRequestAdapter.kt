package com.ziperp.dms.main.user.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.dao.CachedRequest
import com.ziperp.dms.extensions.toSlashDateTimeString
import com.ziperp.dms.main.customer.list.model.CUDCustomerRequest
import kotlinx.android.synthetic.main.item_cached_request.view.*
import java.util.*

class CachedRequestAdapter : BaseAdapter<CachedRequest>() {

    var syncFunction: ((position: Int)-> Unit)? = null

    override fun getLayoutId(): Int = R.layout.item_cached_request

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<CachedRequest> {
        override fun bind(item: CachedRequest) {
            val bodyRequest = Gson().fromJson(item.body, BodyRequest::class.java)
            val customerRequest = Gson().fromJson(
                bodyRequest.pJSONData,
                CUDCustomerRequest::class.java
            )
            val date = Date(item.timestamp)
            itemView.apply {
                txt_request_name.text = "${customerRequest.cstNm}"
                txt_request_body.text = "${date.toSlashDateTimeString()}"
                view_sync.setOnClickListener{syncFunction?.invoke(absoluteAdapterPosition)}
                view_delete.setOnClickListener{deleteFunction?.invoke(absoluteAdapterPosition)}
            }
        }
    }

    fun removeAt(position: Int){
        data.removeAt(position)
        notifyDataSetChanged()
    }


}