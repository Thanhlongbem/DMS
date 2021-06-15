package com.ziperp.dms.main.salespricepromotion.view.salesprice

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.main.salespricepromotion.model.SalesPriceInfoResponse
import kotlinx.android.synthetic.main.fragment_recycler_view.*

class SalesPriceItemFragment(val data: List<SalesPriceInfoResponse.Item>) : BaseFragment() {

    lateinit var adapter: SalesPriceItemAdapter

    override fun setLayoutId(): Int = R.layout.fragment_recycler_view

    @SuppressLint("SetTextI18n")
    override fun initView() {
        adapter = SalesPriceItemAdapter()
        adapter.updateData(data)
        val layoutManager = LinearLayoutManager(activity)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)
    }
}