package com.ziperp.dms.main.customer.list.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.extensions.setOnClickNetworkListener
import com.ziperp.dms.main.customer.list.model.Customer
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.item_customer.view.*

class CustomerAdapter : BaseAdapter<Customer>() {

    override fun getLayoutId(): Int = R.layout.item_customer
    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)
    var showSales: ((Customer) -> Unit)? = null
    var showReport: ((Customer) -> Unit)? = null

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<Customer> {
        override fun bind(item: Customer) {
            itemView.apply {
                tv_title.text = DataConvertUtils.convertTitle(item.txtCstNm)
                tv_location.text = if (item.txtAddress.isNullOrEmpty()) R.string.str_not_available.getString() else item.txtAddress
                tv_phone.text = if (item.txtPhone.isNullOrEmpty()) R.string.str_not_available.getString() else item.txtAccountPhoneCol
                tv_owner.text = if (item.repPerson.isNullOrEmpty()) R.string.str_not_available.getString() else item.repPerson
                tv_sold.text = if (item.regMan.isNullOrEmpty()) R.string.str_not_available.getString() else item.regMan
                tv_sold_time.text = if (item.regDate.isNullOrEmpty()) R.string.str_not_available.getString() else item.regDate.reformatDate()
                tag_sales.setOnClickListener { showSales?.invoke(item) }
                tag_report.setOnClickNetworkListener { showReport?.invoke(item) }
            }
        }
    }

}