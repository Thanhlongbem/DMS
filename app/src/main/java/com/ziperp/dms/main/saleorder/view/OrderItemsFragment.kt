package com.ziperp.dms.main.saleorder.view

import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.main.saleorder.adapter.OrderItemAdapter
import com.ziperp.dms.main.saleorder.model.SaleOrderDetailResponse
import kotlinx.android.synthetic.main.fragment_order_item.*

class OrderItemsFragment(val data: SaleOrderDetailResponse): BaseFragment() {
    lateinit var adapter: OrderItemAdapter

    override fun setLayoutId(): Int = R.layout.fragment_order_item

    override fun initView() {
        adapter = OrderItemAdapter()

        val layoutManager = LinearLayoutManager(context)
        rv_listOrderItems.layoutManager = layoutManager
        rv_listOrderItems.adapter = adapter
        rv_listOrderItems.setHasFixedSize(true)

        update(data)
    }

    fun update(response: SaleOrderDetailResponse) {
        adapter.updateData(response.orderItems)
    }

}