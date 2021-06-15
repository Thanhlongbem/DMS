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
import com.ziperp.dms.extensions.format
import com.ziperp.dms.main.trackingreports.reportsummation.model.SummationStockResponse
import com.ziperp.dms.main.trackingreports.reportsummation.viewmodel.ReportSummationViewModel
import kotlinx.android.synthetic.main.fragment_summation_stock.*

class StockSummationFragment(var listOrder: List<SummationStockResponse.SummationStock> = listOf()) : BaseFragment() {

    val viewModel: ReportSummationViewModel by activityViewModels()

    lateinit var adapter: StockSummationAdapter

    private lateinit var scrollListener: RecyclerViewLoadMoreScroll

    override fun setLayoutId(): Int = R.layout.fragment_summation_stock

    @SuppressLint("SetTextI18n")
    override fun initView() {
        adapter = StockSummationAdapter()
        adapter.updateData(listOrder)
        val layoutManager = LinearLayoutManager(activity)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                if (viewModel.stockPagingParam.hasNext()) {
                    viewModel.getStockReport(true)
                }
            }
        })

        recycler_view.addOnScrollListener(scrollListener)
        dataObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun dataObserver() {
        viewModel.stockData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    scrollListener.setLoaded()
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        adapter.updateData(response.data)
                        if (response.data.isNotEmpty()) {
                            tv_total.text = response.data.map { item -> item.stkCntQty }.reduce { sum, e -> sum + e }.format()
                        } else {
                            tv_total.text = "0"
                        }
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