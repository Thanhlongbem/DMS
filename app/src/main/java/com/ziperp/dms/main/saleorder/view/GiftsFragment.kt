package com.ziperp.dms.main.saleorder.view

import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.main.saleorder.adapter.GiftItemAdapter
import com.ziperp.dms.main.saleorder.model.SaleOrderDetailResponse
import kotlinx.android.synthetic.main.fragment_gifts.*

class GiftsFragment(val data: SaleOrderDetailResponse): BaseFragment() {
    lateinit var adapter: GiftItemAdapter

    override fun setLayoutId(): Int = R.layout.fragment_gifts

    override fun initView() {
        adapter = GiftItemAdapter()
        adapter.addData(data.orderItems)
        val layoutManager = LinearLayoutManager(context)
        rv_listGiftItems.layoutManager = layoutManager
        rv_listGiftItems.adapter = adapter
        rv_listGiftItems.setHasFixedSize(true)
    }

    fun update(response: SaleOrderDetailResponse) {
        adapter.updateData(response.orderItems)
    }

}