package com.ziperp.dms.main.trackingreports.salesresult.view

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
import com.ziperp.dms.main.trackingreports.salesresult.SalesResultViewModel
import com.ziperp.dms.main.trackingreports.salesresult.model.SalesSummationResponse
import kotlinx.android.synthetic.main.fragment_recycler_view.*

class SummationFragment(var listStaff: List<SalesSummationResponse.Staff> = listOf()) : BaseFragment() {

    val viewModel: SalesResultViewModel by activityViewModels()

    lateinit var adapter: SummationAdapter

    private lateinit var scrollListener: RecyclerViewLoadMoreScroll

    override fun setLayoutId(): Int = R.layout.fragment_recycler_view

    @SuppressLint("SetTextI18n")
    override fun initView() {
        adapter = SummationAdapter()
        adapter.updateData(listStaff)
        val layoutManager = LinearLayoutManager(activity)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                if (viewModel.summationPagingParam.hasNext()) {
                    viewModel.getSummationResult(true)
                }
            }
        })

        recycler_view.addOnScrollListener(scrollListener)
        dataObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun dataObserver() {
        viewModel.summationData.observe(this, Observer {
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