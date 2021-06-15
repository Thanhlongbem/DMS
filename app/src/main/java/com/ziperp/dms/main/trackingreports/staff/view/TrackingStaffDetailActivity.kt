package com.ziperp.dms.main.trackingreports.staff.view

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.common.paging.OnLoadMoreListener
import com.ziperp.dms.common.paging.RecyclerViewLoadMoreScroll
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.main.trackingreports.visitcustomer.view.TrackingVisitCustomerActivity.Companion.EXTRA_STAFF_ID
import kotlinx.android.synthetic.main.activity_staff_detail.*

class TrackingStaffDetailActivity: BaseActivity() {

    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var adapter: StaffDetailAdapter
    private val viewModel by lazy { getViewModel { Injection.provideTrackingStaffViewModel() } }
    var staffID = ""

    override fun setLayoutId(): Int = R.layout.activity_staff_detail

    override fun initView() {
        setToolbar(R.string.str_tracking_staff_location.getString(), true)

        staffID = intent.getStringExtra(EXTRA_STAFF_ID)?: ""
        initRecyclerView()
        viewModel.getTrackingStaffDetail(staffID)
        dataObserver()
    }

    private fun initRecyclerView() {
        adapter = StaffDetailAdapter()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                if (viewModel.pagingParam.hasNext()) {
                    viewModel.getTrackingStaffDetail(staffID)
                }
            }
        })

        recyclerView.addOnScrollListener(scrollListener)

        swipe_refresh.setOnRefreshListener {
            viewModel.pagingParam.clear()
            viewModel.getTrackingStaffDetail(staffID)
        }
    }

    private fun dataObserver() {
        viewModel.staffDetailData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    scrollListener.setLoaded()
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        if(swipe_refresh.isRefreshing){
                            adapter.updateData(response.data)
                        } else {
                            adapter.addData(response.data)
                        }
                    }
                    if (swipe_refresh.isRefreshing) swipe_refresh.isRefreshing = false
                }
                Status.LOADING -> {
                    if (!swipe_refresh.isRefreshing){
                        loading_progressbar.visibility = View.VISIBLE
                    }
                }
                Status.ERROR -> {
                    scrollListener.setLoaded()
                    loading_progressbar.visibility = View.GONE
                    if (swipe_refresh.isRefreshing) swipe_refresh.isRefreshing = false
                }
            }
        })
    }
}