package com.ziperp.dms.main.visitcustomer.view

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.jakewharton.rxbinding3.widget.textChanges
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.base.BaseCUDActivity
import com.ziperp.dms.core.filter.FilterDialogFragment
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.FormDataFactory
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.toast
import com.ziperp.dms.main.customer.customerdetail.view.CustomerModifyActivity
import com.ziperp.dms.main.itemmaster.adapter.SQPagerAdapter
import com.ziperp.dms.main.user.view.CachedDataActivity
import com.ziperp.dms.main.visitcustomer.viewmodel.VisitCustomerViewModel
import com.ziperp.dms.utils.LogUtils
import com.ziperp.dms.utils.NetWorkConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_visited_customer.*
import kotlinx.android.synthetic.main.search_box.*
import java.util.concurrent.TimeUnit

class VisitedCustomerActivity : BaseActivity() {

    private lateinit var visitCustomerFragment: VisitCustomerFragment
    private lateinit var mapFragment: MapFragment
    private var currentFragment: Int = 0 // 0: List 1: Map

    private val viewModel by lazy {
        getViewModel { Injection.provideVisitCustomerViewModel() }
    }

    var isRefreshing: Boolean = false
        get() {return visitCustomerFragment.isRefreshing}

    override fun setLayoutId(): Int = R.layout.activity_visited_customer

    override fun initView() {
        setToolbar(R.string.str_visit_customer.getString(), true)
        visitCustomerFragment = VisitCustomerFragment()
        mapFragment = MapFragment()

        val pagerAdapter = SQPagerAdapter(this, listFragments = listOf(visitCustomerFragment, mapFragment))
        viewpager.adapter = pagerAdapter
        viewpager.offscreenPageLimit = 2
        viewpager.isUserInputEnabled = false

        fab_menu.setClosedOnTouchOutside(true)
        btn_sort.setOnClickListener {
            visitCustomerFragment.adapter.sortDataByLocation()
            fab_menu.close(true)
        }

        btn_sync.setOnClickListener {
            startActivity(CachedDataActivity.newInstance(this, 6))
            fab_menu.close(true)
        }

        btn_add_new.setOnClickListener {
            val intent = Intent(this, CustomerModifyActivity::class.java)
            intent.putExtra(BaseCUDActivity.EXTRA_CUD_MODE, BaseCUDActivity.CREATE_MODE)
            startActivity(intent)
            fab_menu.close(true)
        }

        viewModel.getVisitCustomer()
        viewModel.getVisitCustomerMap()

        search_box.textChanges()
            .skip(1)
            .map { it.toString() }
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.d("Search String ${it.isNotBlank()}.. " + it)

                viewModel.updateVisitCustomerState.postValue(VisitCustomerViewModel.CLEAR_DATA_STATE)// clear data
                viewModel.pagingParam.clear()
                if (it.isNotBlank()) {
                    viewModel.visitCustomerRequest.cstInfo = it
                } else {
                    viewModel.visitCustomerRequest.cstInfo = ""
                }
                viewModel.getVisitCustomer()
                viewModel.getVisitCustomerMap()
            }, {
                LogUtils.e(it.toString())
            }).disposedBy(compositeDisposable)

        //TODO: Hidden Menu
//        toolbar_title.setOnClickListener {  startActivity(CachedDataActivity.newInstance(this, 6)) }

    }


    private fun switchFragment(index: Int){
        currentFragment = index
        fab_menu.visibility = if (index == 0) View.VISIBLE else View.GONE
        viewpager.setCurrentItem(index, true)
        invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        menu.findItem(R.id.action_filter)?.isVisible = true
        menu.findItem(R.id.action_sync)?.isVisible = true

        when(currentFragment){
            0 -> {
                menu.findItem(R.id.action_location)?.isVisible = true
                menu.findItem(R.id.action_report)?.isVisible = false
            }
            1 -> {
                menu.findItem(R.id.action_location)?.isVisible = false
                menu.findItem(R.id.action_report)?.isVisible = true
            }
            else -> {
                "nothing".toast(applicationContext)
            }
        }
        return true
    }

    private var filterDialog: FilterDialogFragment? = null
    fun showFilterDialog() {
        val data = viewModel.latestItemControls ?: FormDataFactory.get(Form.FORM_ID_VISIT_CUSTOMER)

        LogUtils.d("Data size ${data.size}")
        filterDialog = FilterDialogFragment.newInstance(data)
        filterDialog?.show(supportFragmentManager, null)
        filterDialog?.onResult = { itemControls ->
            viewModel.updateVisitCustomerState.postValue(1)// clear data
            viewModel.pagingParam.clear()
            viewModel.applyFilter(itemControls)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                showFilterDialog()
            }
            R.id.action_location -> {
                LogUtils.d("Location item")
                switchFragment(1)
            }
            R.id.action_report -> {
                LogUtils.d("Report item")
                switchFragment(0)
            }
            R.id.action_sync ->{
                syncAllData()
            }
        }
        return true
    }

    fun syncAllData(){
        if(NetWorkConnection.isNetworkAvailable()){
            showLoading(true)
            Injection.provideVisitCustomerRepository()
                .syncAllData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    "Complete synchronize data!".toast()
                    showLoading(false)
                },{
                    "Error while synchronizing data ${it.message}".toast()
                    showLoading(false)
                }).disposedBy(compositeDisposable)
        }else{
            "No network connection!".toast()
        }
    }


}