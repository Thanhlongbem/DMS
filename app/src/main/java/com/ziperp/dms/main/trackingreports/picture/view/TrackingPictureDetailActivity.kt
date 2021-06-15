package com.ziperp.dms.main.trackingreports.picture.view

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
import com.ziperp.dms.main.trackingreports.picture.model.GroupPictureOfCustomer
import com.ziperp.dms.main.trackingreports.visitcustomer.view.TrackingVisitCustomerActivity
import kotlinx.android.synthetic.main.activity_staff_detail.*

class TrackingPictureDetailActivity: BaseActivity() {

    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var adapter: GroupPictureAdapter
    private val viewModel by lazy { getViewModel { Injection.provideTrackingPictureViewModel() } }
    var staffID = ""

    override fun setLayoutId(): Int = R.layout.activity_staff_detail

    override fun initView() {
        setToolbar(R.string.str_tracking_visit_customer.getString(), true)

        staffID = intent.getStringExtra(TrackingVisitCustomerActivity.EXTRA_STAFF_ID)?: ""
        initRecyclerView()
        viewModel.getTrackingPictureDetail(staffID)
        dataObserver()
    }

    private fun initRecyclerView() {
        adapter = GroupPictureAdapter()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                if (viewModel.pagingParam.hasNext()) {
                    viewModel.getTrackingPictureDetail(staffID)
                }
            }
        })

        recyclerView.addOnScrollListener(scrollListener)

        swipe_refresh.setOnRefreshListener {
            viewModel.pagingParam.clear()
            viewModel.getTrackingPictureDetail(staffID)
        }
    }

    private fun dataObserver() {
        viewModel.VSPictureDetailData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    scrollListener.setLoaded()
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        val listGroup = response.data.groupBy { it.cstVisitNo }.map { pair -> GroupPictureOfCustomer(pair.key, pair.value) }
                        if(swipe_refresh.isRefreshing){
                            adapter.updateData(listGroup)
                        } else {
                            adapter.addData(listGroup)
                        }
                        setToolbar(
                            "${response.record[0].totalRecords.toInt()} ${R.string.str_visit_customer.getString()}",
                            true
                        )
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