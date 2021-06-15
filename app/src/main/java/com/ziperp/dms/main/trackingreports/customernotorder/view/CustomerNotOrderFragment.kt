package com.ziperp.dms.main.trackingreports.customernotorder.view

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.common.paging.OnLoadMoreListener
import com.ziperp.dms.common.paging.RecyclerViewLoadMoreScroll
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.main.trackingreports.customernotorder.CustomerNotOrderViewModel
import com.ziperp.dms.main.trackingreports.customernotorder.model.CustomerNotOrderResponse
import kotlinx.android.synthetic.main.fragment_customer_not_order.*

class CustomerNotOrderFragment(val response: CustomerNotOrderResponse) : BaseFragment() {

    val viewModel: CustomerNotOrderViewModel by activityViewModels()

    lateinit var adapter: NewNotOrderAdapter

    private lateinit var scrollListener: RecyclerViewLoadMoreScroll

    override fun setLayoutId(): Int = R.layout.fragment_customer_not_order

    @SuppressLint("SetTextI18n")
    override fun initView() {
        adapter = NewNotOrderAdapter()
        adapter.updateData(response.data)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                if (viewModel.notOrderPagingParam.hasNext()) {
                    viewModel.getCustomerNotOrder(true)
                }
            }
        })

        recyclerView.addOnScrollListener(scrollListener)

        dataObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun dataObserver() {
        viewModel.customerNotOrderData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    scrollListener.setLoaded()
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        adapter.updateData(response.data)
                        if (response.record.isNotEmpty()) tv_header.text =
                            "${response.record[0].totalRecords} ${response.record[0].titleSummation}"
                    }
                }
                Status.LOADING -> { loading_progressbar.visibility = View.VISIBLE }
                Status.ERROR -> {
                    scrollListener.setLoaded()
                    loading_progressbar.visibility = View.GONE
                }
            }
        })
    }
}