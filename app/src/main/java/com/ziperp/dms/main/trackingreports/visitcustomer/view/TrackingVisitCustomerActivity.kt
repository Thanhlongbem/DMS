package com.ziperp.dms.main.trackingreports.visitcustomer.view

import android.annotation.SuppressLint
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.textChanges
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
import com.ziperp.dms.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_list_staff.*
import kotlinx.android.synthetic.main.search_box.*
import java.util.concurrent.TimeUnit

class TrackingVisitCustomerActivity: BaseActivity() {

    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private var filterDialog: FilterDialogFragment? = null
    lateinit var adapter: TrackingVisitCustomerAdapter

    private val viewModel by lazy { getViewModel { Injection.provideTrackingVSViewModel() } }

    override fun setLayoutId(): Int = R.layout.activity_item_master

    override fun initView() {
        setToolbar(R.string.str_tracking_visit_customer.getString(), true)
        initSearchBox()
        initRecyclerView()
        dataObserver()
        viewModel.getTrackingVSList()
    }

    @SuppressLint("CheckResult")
    private fun initSearchBox() {
        search_box.textChanges()
            .skip(1)
            .map { it.toString() }
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                adapter.removeAll()
                viewModel.pagingParam.clear()
                if (it.isNotBlank()) {
                    viewModel.requestBody.searchInfo = it
                } else {
                    viewModel.requestBody.searchInfo = ""
                }
                viewModel.getTrackingVSList()
            }, {
                LogUtils.e(it.toString())
            })
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = TrackingVisitCustomerAdapter()
        adapter.onClickListener = {
            val visitCustomer = adapter.data[it]
            val intent = Intent(this, TrackingVSDetailActivity::class.java)
            intent.putExtra(EXTRA_STAFF_ID, visitCustomer.staffId)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                if (viewModel.pagingParam.hasNext()) {
                    viewModel.getTrackingVSList()
                }
            }
        })

        recyclerView.addOnScrollListener(scrollListener)

        swipe_refresh.setOnRefreshListener {
            viewModel.pagingParam.clear()
            viewModel.getTrackingVSList(false)
        }
    }

    private fun dataObserver() {
        viewModel.visitCustomerListData.observe(this, Observer {
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
        val data = viewModel.latestItemControls ?: FormDataFactory.get(Form.FORM_ID_TRACKING_VISIT_CUSTOMER)

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