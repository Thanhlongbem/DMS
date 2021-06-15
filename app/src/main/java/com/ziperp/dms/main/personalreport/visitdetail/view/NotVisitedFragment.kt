package com.ziperp.dms.main.personalreport.visitdetail.view

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
import com.ziperp.dms.main.personalreport.visitdetail.model.NotVisitedResponse
import com.ziperp.dms.main.personalreport.visitdetail.viewmodel.VisitDetailViewModel
import kotlinx.android.synthetic.main.fragment_recycler_view.*

class NotVisitedFragment(var listStaff: List<NotVisitedResponse.NotVisitedCustomer> = listOf()) : BaseFragment() {

    val viewModel: VisitDetailViewModel by activityViewModels()

    lateinit var adapter: NotVisitedAdapter

    private lateinit var scrollListener: RecyclerViewLoadMoreScroll

    override fun setLayoutId(): Int = R.layout.fragment_recycler_view

    @SuppressLint("SetTextI18n")
    override fun initView() {
        adapter = NotVisitedAdapter()
        adapter.updateData(listStaff)
        val layoutManager = LinearLayoutManager(activity)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                if (viewModel.notVisitedPagingParam.hasNext()) {
                    viewModel.getVisitedDetail(true)
                }
            }
        })

        recycler_view.addOnScrollListener(scrollListener)
        dataObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun dataObserver() {
        viewModel.notVisitedData.observe(this, Observer {
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