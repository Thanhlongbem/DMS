package com.ziperp.dms.main.trackingreports.reportsummation.view

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
import com.ziperp.dms.main.trackingreports.reportsummation.model.SummationOrderResponse
import com.ziperp.dms.main.trackingreports.reportsummation.viewmodel.ReportSummationViewModel
import kotlinx.android.synthetic.main.fragment_recycler_view.*

class OrderSummationFragment(var listOrder: List<SummationOrderResponse.SummationOrder> = listOf()) : BaseFragment() {

    val viewModel: ReportSummationViewModel by activityViewModels()

    lateinit var adapter: OrderSummationAdapter

    private lateinit var scrollListener: RecyclerViewLoadMoreScroll

    override fun setLayoutId(): Int = R.layout.fragment_recycler_view

    @SuppressLint("SetTextI18n")
    override fun initView() {
        adapter = OrderSummationAdapter()
        adapter.updateData(listOrder)
        val layoutManager = LinearLayoutManager(activity)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                if (viewModel.orderPagingParam.hasNext()) {
                    viewModel.getOrderReport(true)
                }
            }
        })

        recycler_view.addOnScrollListener(scrollListener)
        dataObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun dataObserver() {
        viewModel.orderData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    scrollListener.setLoaded()
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        adapter.updateData(response.data)
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