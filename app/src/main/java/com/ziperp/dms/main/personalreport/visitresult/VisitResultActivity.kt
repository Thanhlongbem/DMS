package com.ziperp.dms.main.personalreport.visitresult

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.common.paging.OnLoadMoreListener
import com.ziperp.dms.common.paging.RecyclerViewLoadMoreScroll
import com.ziperp.dms.core.filter.FilterDialogFragment
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.FormDataFactory
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.main.trackingreports.visitcustomer.view.TrackingVisitCustomerAdapter
import kotlinx.android.synthetic.main.fragment_list_staff.*

class VisitResultActivity: BaseActivity() {

    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private var filterDialog: FilterDialogFragment? = null
    lateinit var adapter: TrackingVisitCustomerAdapter

    private val viewModel by lazy { getViewModel { Injection.provideVisitResultViewModel() } }

    override fun setLayoutId(): Int = R.layout.activity_staff_detail

    override fun initView() {
        setToolbar(R.string.str_tracking_visit_customer.getString(), true)
        initRecyclerView()
        dataObserver()
        viewModel.getVisitResult()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = TrackingVisitCustomerAdapter()
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                if (viewModel.pagingParam.hasNext()) {
                    viewModel.getVisitResult(true)
                }
            }
        })

        recyclerView.addOnScrollListener(scrollListener)

        swipe_refresh.setOnRefreshListener {
            viewModel.pagingParam.clear()
            viewModel.getVisitResult(false)
        }
    }

    private fun dataObserver() {
        viewModel.visitResultListData.observe(this, Observer {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        menu?.findItem(R.id.action_filter)?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> showFilterDialog()
        }
        return true
    }

    private fun showFilterDialog() {
        val data = viewModel.latestItemControls ?: FormDataFactory.get(Form.FORM_ID_VISIT_RESULT)

        filterDialog = FilterDialogFragment.newInstance(data)
        filterDialog?.show(supportFragmentManager, null)
        filterDialog?.onResult = { itemControls ->
            adapter.removeAll()
            viewModel.pagingParam.clear()
            viewModel.applyFilter(itemControls)
        }
    }

    companion object {
        const val EXTRA_STAFF_ID = "EXTRA_STAFF_ID"
    }
}