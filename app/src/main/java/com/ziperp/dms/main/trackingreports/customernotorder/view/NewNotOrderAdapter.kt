package com.ziperp.dms.main.trackingreports.customernotorder.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.ifEmptyLetBe
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.main.trackingreports.customernotorder.model.CustomerNotOrderResponse
import com.ziperp.dms.main.trackingreports.customernotorder.model.NewCustomerResponse
import kotlinx.android.synthetic.main.item_customer_not_order.view.*

class NewNotOrderAdapter : BaseAdapter<Any>() {
    override fun getLayoutId(): Int = R.layout.item_customer_not_order

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<Any> {
        override fun bind(item: Any) {
            itemView.apply {
                if (item is NewCustomerResponse.NewCustomer) {
                    tv_customer_name.text = item.cstNm
                    tv_location.text = item.address.ifEmptyLetBe("N/A")
                    tv_register.text = item.regMan
                    tv_create_time.text = item.regDate.reformatDate()
                    tv_group_1.text = item.customerGrp1.ifEmptyLetBe("N/A")
                    tv_group_2.text = item.customerGrp2.ifEmptyLetBe("N/A")
                } else if (item is CustomerNotOrderResponse.CustomerNotOrder) {
                    tv_customer_name.text = item.cstNm
                    tv_location.text = item.address.ifEmptyLetBe("N/A")
                    tv_register.text = item.salesman.ifEmptyLetBe("N/A")
                    tv_create_time.text = item.lastOrderDate.reformatDate().ifEmptyLetBe("N/A")
                    tv_group_1.text = item.customerGrp1.ifEmptyLetBe("N/A")
                    tv_group_2.text = item.customerGrp2.ifEmptyLetBe("N/A")
                }
            }


        }
    }
}