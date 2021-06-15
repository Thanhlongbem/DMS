package com.ziperp.dms.main.trackingreports.staff.view

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.common.paging.OnLoadMoreListener
import com.ziperp.dms.common.paging.RecyclerViewLoadMoreScroll
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.main.trackingreports.staff.viewmodel.TrackingStaffViewModel
import com.ziperp.dms.main.trackingreports.visitcustomer.view.TrackingVisitCustomerActivity.Companion.EXTRA_STAFF_ID
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_list_staff.*
import kotlinx.android.synthetic.main.fragment_list_staff.loading_progressbar
import kotlinx.android.synthetic.main.fragment_list_staff.recyclerView
import kotlinx.android.synthetic.main.fragment_list_staff.swipe_refresh
import kotlinx.android.synthetic.main.fragment_list_visit_customer.*

class StaffListFragment: BaseFragment() {

    lateinit var adapter: StaffListAdapter
    val viewModel: TrackingStaffViewModel by activityViewModels()
    val trackingStaffActivity by lazy { activity as TrackingStaffActivity }

    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    var isRefreshing: Boolean = false
        get() {return swipe_refresh.isRefreshing}

    override fun setLayoutId(): Int = R.layout.fragment_list_staff

    override fun initView() {
        initRecyclerView()
        dataObserve()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = StaffListAdapter()
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        adapter.onClickListener = { position ->
            val staff = adapter.data[position]
            val intent = Intent(activity, TrackingStaffDetailActivity::class.java)
            intent.putExtra(EXTRA_STAFF_ID, staff.staffId)
            startActivity(intent)
        }

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                if (viewModel.pagingParam.hasNext()) {
                    viewModel.getTrackingStaffList()
                }
            }
        })

        recyclerView.addOnScrollListener(scrollListener)

        swipe_refresh.setOnRefreshListener {
            refreshData()
        }
    }

    fun refreshData(){
        viewModel.pagingParam.clear()
        viewModel.getTrackingStaffList()
    }

    private fun dataObserve() {
        viewModel.staffListData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    scrollListener.setLoaded()
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        if(swipe_refresh.isRefreshing){
                            adapter.updateData(response.listStaff)
                        } else {
                            adapter.addData(response.listStaff)
                        }
                        trackingStaffActivity.setToolbar(
                            "${response.record[0].totalRecords.toInt()} Staffs",
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

        viewModel.updateStaffState.observe(this, Observer {
            when (it){
                1 -> adapter.removeAll()
            }
        })
    }
}