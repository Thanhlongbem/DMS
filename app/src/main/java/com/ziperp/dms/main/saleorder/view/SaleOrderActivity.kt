package com.ziperp.dms.main.saleorder.view

import android.annotation.SuppressLint
import android.app.Activity
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
import com.ziperp.dms.base.BaseCUDActivity.Companion.CREATE_MODE
import com.ziperp.dms.common.paging.OnLoadMoreListener
import com.ziperp.dms.common.paging.RecyclerViewLoadMoreScroll
import com.ziperp.dms.core.filter.FilterDialogFragment
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.FormDataFactory
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.main.saleorder.adapter.SaleOrderAdapter
import com.ziperp.dms.main.saleorder.modify.SalesOrderModifyActivity
import com.ziperp.dms.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_sale_order.*
import kotlinx.android.synthetic.main.search_box.*
import java.util.concurrent.TimeUnit

class SaleOrderActivity : BaseActivity() {

    lateinit var adapter: SaleOrderAdapter
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private var filterDialog: FilterDialogFragment? = null

    private val viewModel by lazy {
        getViewModel { Injection.provideSaleOrderViewModel() }
    }

    override fun setLayoutId(): Int = R.layout.activity_sale_order

    @SuppressLint("CheckResult")
    override fun initView() {
        setToolbar(R.string.str_sales_order.getString(), true)

        viewModel.getSaleOrder()

        viewModel.saleOrderLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    scrollListener.setLoaded()
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        if (swipe_refresh.isRefreshing) {
                            adapter.updateData(response.data)
                        } else {
                            adapter.addData(response.data)
                        }
                        setToolbar(
                            "${response.record[0].totalRecords.toInt()} Orders",
                            true
                        )
                    }
                    if (swipe_refresh.isRefreshing) swipe_refresh.isRefreshing = false
                }
                Status.LOADING -> {
                    if (!swipe_refresh.isRefreshing) {
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

        adapter = SaleOrderAdapter()
        adapter.onClickListener = {position ->
            startActivityForResult(SalesOrderDetailActivity.newInstance(adapter.data[position], this), 100)
        }
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                if (viewModel.pagingParam.hasNext()) {
                    viewModel.getSaleOrder(true)
                }
            }
        })

        recyclerView.addOnScrollListener(scrollListener)

        search_box.textChanges()
            .skip(1)
            .map { it.toString() }
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.d("Search String ${it.isNotBlank()}.. " + it)
                adapter.removeAll()
                viewModel.pagingParam.clear()
                if (it.isNotBlank()) {
                    viewModel.saleOrderRequest.orderInfo = it
                } else {
                    viewModel.saleOrderRequest.orderInfo = ""
                }
                viewModel.getSaleOrder()
            }, {
                LogUtils.e(it.toString())
            })

        btn_add_new.setOnClickListener {
            startActivity(SalesOrderModifyActivity.newInstance(mode = CREATE_MODE, context = this))
        }

        swipe_refresh.setOnRefreshListener {
            LogUtils.d("Refresh")
            refreshData()
        }
    }

    fun refreshData() {
        viewModel.pagingParam.clear()
        viewModel.getSaleOrder(false)
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
        val data = viewModel.latestItemControls ?: FormDataFactory.get(Form.FORM_ID_SALE_ORDER)

        filterDialog = FilterDialogFragment.newInstance(data)
        filterDialog?.show(supportFragmentManager, null)
        filterDialog?.onResult = { itemControls ->
            adapter.removeAll()
            viewModel.pagingParam.clear()
            viewModel.applyFilter(itemControls)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            swipe_refresh.isRefreshing = true
            refreshData()
        }
    }

}