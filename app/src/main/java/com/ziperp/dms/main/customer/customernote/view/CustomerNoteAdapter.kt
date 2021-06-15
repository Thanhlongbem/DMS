package com.ziperp.dms.main.customer.customernote.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.main.customer.customernote.model.CustomerNoteResponse
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.item_customer.view.tv_title
import kotlinx.android.synthetic.main.item_customer_note.view.*

class CustomerNoteAdapter : BaseAdapter<CustomerNoteResponse.CustomerNote>() {

    override fun getLayoutId(): Int = R.layout.item_customer_note
    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<CustomerNoteResponse.CustomerNote> {
        override fun bind(item: CustomerNoteResponse.CustomerNote) {
            itemView.apply {
                tv_title.text = DataConvertUtils.convertTitle(item.noteTitle)
                tv_staff_name.text = if (item.regMan.isNullOrEmpty()) R.string.str_not_available.getString() else item.regMan
                tv_create_time.text = if (item.regDate.isNullOrEmpty()) R.string.str_not_available.getString() else item.regDate.split("|")[0]
            }
        }
    }

}