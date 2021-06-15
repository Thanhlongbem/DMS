package com.ziperp.dms.main.customer.list.view

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
import com.ziperp.dms.base.BaseCUDActivity.Companion.CREATE_MODE
import com.ziperp.dms.base.BaseCUDActivity.Companion.EXTRA_CUD_MODE
import com.ziperp.dms.common.paging.OnLoadMoreListener
import com.ziperp.dms.common.paging.RecyclerViewLoadMoreScroll
import com.ziperp.dms.core.filter.FilterDialogFragment
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.FormDataFactory
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.core.rest.RESULT_RELOAD
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.toast
import com.ziperp.dms.main.customer.customerdetail.view.CustomerDetailActivity
import com.ziperp.dms.main.customer.customerdetail.view.CustomerModifyActivity
import com.ziperp.dms.main.saleorder.modify.SalesOrderModifyActivity
import com.ziperp.dms.main.trackingreports.reportsummation.view.ReportSummationActivity
import com.ziperp.dms.main.trackingreports.reportsummation.view.ReportSummationActivity.Companion.EXTRA_CUSTOMER
import com.ziperp.dms.main.user.view.CachedDataActivity
import com.ziperp.dms.utils.DataConvertUtils
import com.ziperp.dms.utils.LogUtils
import com.ziperp.dms.utils.NetWorkConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_customer.*
import kotlinx.android.synthetic.main.activity_customer.recyclerView
import kotlinx.android.synthetic.main.activity_customer.swipe_refresh
import kotlinx.android.synthetic.main.activity_sale_order.btn_add_new
import kotlinx.android.synthetic.main.activity_sale_order.loading_progressbar
import kotlinx.android.synthetic.main.search_box.*
import java.util.concurrent.TimeUnit

class CustomerActivity : BaseActivity() {

    lateinit var adapter: CustomerAdapter
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private var filterDialog: FilterDialogFragment? = null

    private val viewModel by lazy {
        getViewModel { Injection.provideCustomerViewModel() }
    }

    override fun setLayoutId(): Int = R.layout.activity_customer

    override fun initView() {
        setToolbar(R.string.str_customer.getString(), true)
        swipe_refresh.setOnRefreshListener {
            LogUtils.d("Refresh")
            viewModel.pagingParam.clear()
            viewModel.getAccountList(false)
        }

        viewModel.getAccountList()
        viewModel.accountListData.observe(this, Observer {
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
                        if (response.record.isNotEmpty()) {
                            setToolbar("${response.record[0].totalRecords.toInt()} ${R.string.str_customer.getString()}", true)
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

        setUpRecyclerView()
        setUpSearchBox()
        setupFloatingButton()
        //TODO: Hidden Menu
        //toolbar_title.setOnClickListener {  startActivity(CachedDataActivity.newInstance(this, 3)) }


    }

    private fun setUpRecyclerView() {
        adapter = CustomerAdapter()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        adapter.onClickListener = { position ->
            detailCustomer(adapter.data[position].gridKey)
        }

        adapter.showSales = { customer ->
            startActivity(
                SalesOrderModifyActivity.newInstance(
                    DataConvertUtils.convertTitle(customer.txtCstNm),
                    customer.gridKey, customer.txtAccountPhoneCol, CREATE_MODE, this
                )
            )
        }

        adapter.showReport = { customer ->
            val intent = Intent(this, ReportSummationActivity::class.java)
            intent.putExtra(EXTRA_CUSTOMER, customer.gridKey)
            startActivity(intent)
        }

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                if (viewModel.pagingParam.hasNext()) {
                    viewModel.getAccountList(true)
                }
            }
        })

        recyclerView.addOnScrollListener(scrollListener)
    }

    fun refreshData(){
        viewModel.pagingParam.clear()
        viewModel.getAccountList(false)
    }

    @SuppressLint("CheckResult")
    private fun setUpSearchBox() {
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
                    viewModel.requestBody.cstNm = it
                } else {
                    viewModel.requestBody.cstNm = ""
                }
                viewModel.getAccountList()
            }, {
                LogUtils.e(it.toString())
            })
    }

    private fun setupFloatingButton() {
        btn_add_new.setOnClickListener {
            val intent = Intent(this, CustomerModifyActivity::class.java)
            intent.putExtra(EXTRA_CUD_MODE, CREATE_MODE)
            startActivity(intent)
            fab_menu.close(true)
        }
        btn_sync.setOnClickListener {
            startActivity(CachedDataActivity.newInstance(this, 3))
            fab_menu.close(true)
        }
    }

    private fun detailCustomer(code: String) {
        val intent = Intent(this, CustomerDetailActivity::class.java)
        intent.putExtra(Constants.EXTRA_CUSTOMER_CODE, code)
        startActivityForResult(intent, 100)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        menu?.findItem(R.id.action_filter)?.isVisible = true
        menu?.findItem(R.id.action_sync)?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> showFilterDialog()
            R.id.action_sync -> syncData()
        }
        return true
    }

    private fun syncData() {
        if(NetWorkConnection.isNetworkAvailable()){
            showLoading(true)
            Injection.provideCustomerDetailRepository()
                .syncAllData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    "Complete synchronize data!".toast()
                    showLoading(false)
                },{
                    showLoading(false)
                    "Error while synchronizing data ${it.message}".toast()
                })
        }else{
            "No network connection!".toast()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == RESULT_RELOAD) {
            swipe_refresh.isRefreshing = true
            refreshData()
        }
    }

    private fun showFilterDialog() {
        val data = viewModel.latestItemControls ?: FormDataFactory.get(Form.FORM_ID_CUSTOMER)

        filterDialog = FilterDialogFragment.newInstance(data)
        filterDialog?.show(supportFragmentManager, null)
        filterDialog?.onResult = { itemControls ->
            adapter.removeAll()
            viewModel.pagingParam.clear()
            viewModel.applyFilter(itemControls)
        }
    }
}