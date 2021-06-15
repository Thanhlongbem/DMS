package com.ziperp.dms.main.trackingreports.staff.view

import android.annotation.SuppressLint
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding3.widget.textChanges
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.core.filter.FilterDialogFragment
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.FormDataFactory
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.main.itemmaster.adapter.SQPagerAdapter
import com.ziperp.dms.main.visitcustomer.viewmodel.VisitCustomerViewModel
import com.ziperp.dms.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_tracking_staff.*
import kotlinx.android.synthetic.main.search_box.*
import java.util.concurrent.TimeUnit

class TrackingStaffActivity: BaseActivity() {

    private lateinit var staffListFragment: StaffListFragment
    private lateinit var mapFragment: StaffMapFragment
    private var currentFragment: Int = 0 // 0: List 1: Map

    override fun setLayoutId(): Int = R.layout.activity_tracking_staff

    private val viewModel by lazy {
        getViewModel { Injection.provideTrackingStaffViewModel() }
    }

    override fun initView() {
        setToolbar(R.string.str_tracking_staff_location.getString(), true)
        initPager()
        initSearchBox()
        btn_sync.setOnClickListener {
            //TODO Sync
        }

        viewModel.getTrackingStaffList()
        viewModel.getMapStaffList()

    }

    private fun initPager() {
        staffListFragment = StaffListFragment()
        mapFragment = StaffMapFragment()

        val pagerAdapter = SQPagerAdapter(this, listFragments = listOf(staffListFragment, mapFragment))
        viewpager.adapter = pagerAdapter
        viewpager.offscreenPageLimit = 2
        viewpager.isUserInputEnabled = false
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
                viewModel.updateStaffState.postValue(VisitCustomerViewModel.CLEAR_DATA_STATE)// clear data
                viewModel.pagingParam.clear()
                if (it.isNotBlank()) {
                    viewModel.requestBody.trackSearchInfo = it
                } else {
                    viewModel.requestBody.trackSearchInfo = ""
                }
                viewModel.getTrackingStaffList()
            }, {
                LogUtils.e(it.toString())
            })
    }

    private fun switchFragment(index: Int){
        currentFragment = index
        viewpager.setCurrentItem(index, true)
        invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        menu.findItem(R.id.action_filter)?.isVisible = true
        menu.findItem(R.id.action_location)?.isVisible = currentFragment == 0
        menu.findItem(R.id.action_report)?.isVisible = currentFragment == 1
        return true
    }

    private var filterDialog: FilterDialogFragment? = null
    private fun showFilterDialog() {
        val data = viewModel.latestItemControls ?: FormDataFactory.get(Form.FORM_ID_TRACKING_STAFF)

        filterDialog = FilterDialogFragment.newInstance(data)
        filterDialog?.show(supportFragmentManager, null)
        filterDialog?.onResult = { itemControls ->
            viewModel.updateStaffState.postValue(1)// clear data
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
                switchFragment(1)
            }
            R.id.action_report -> {
                switchFragment(0)
            }
        }
        return true
    }



}