package com.ziperp.dms.main.trackingreports.dailyperformance.view

import android.view.Menu
import android.view.MenuItem
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.base.PagerAdapter
import com.ziperp.dms.core.filter.FilterDialogFragment
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.FormDataFactory
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.toSlashDateString
import kotlinx.android.synthetic.main.activity_item_master_detail.*
import java.util.*

class DailyPerformanceActivity: BaseActivity() {

    lateinit var salesOrderFragment: DailyReportFragment
    lateinit var visitCustomerFragment: DailyReportFragment

    private var filterDialog: FilterDialogFragment? = null
    private val viewModel by lazy {
        getViewModel { Injection.provideDailyPerformanceViewModel() }
    }

    override fun setLayoutId(): Int = R.layout.activity_customer_not_order

    override fun initView() {
        val today = Date().toSlashDateString()
        setToolbar("${R.string.str_sales_and_visit.getString()} - $today", true)
        initPager()
        viewModel.getDailyReport()
    }

    private fun initPager() {
        salesOrderFragment = DailyReportFragment( MODE_SALES_ORDER,
            listOf(R.string.str_dept_staff.getString(), R.string.str_visit.getString(), R.string.str_order.getString(), R.string.str_quantity.getString()))
        visitCustomerFragment = DailyReportFragment( MODE_VISIT_CUSTOMER,
            listOf(R.string.str_dept_staff.getString(), R.string.str_morning.getString(), R.string.str_afternoon.getString(), R.string.str_km.getString()))

        val pagerAdapter = PagerAdapter(supportFragmentManager, listFragments = listOf(salesOrderFragment, visitCustomerFragment))
        pagerAdapter.listTitles = listOf(R.string.str_sales_order.getString(), R.string.str_visit_customer.getString())

        viewpager.adapter = pagerAdapter
        viewpager.offscreenPageLimit = 2
        tab.setupWithViewPager(viewpager)
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
        val data = viewModel.latestItemControls ?: FormDataFactory.get(Form.FORM_ID_DAILY_PERFORMANCE)

        filterDialog = FilterDialogFragment.newInstance(data)
        filterDialog?.show(supportFragmentManager, null)
        filterDialog?.onResult = { itemControls ->
            salesOrderFragment.adapter.data.clear()
            visitCustomerFragment.adapter.data.clear()
            viewModel.applyFilter(itemControls)
        }
    }

    companion object {
        const val MODE_SALES_ORDER = 0
        const val MODE_VISIT_CUSTOMER = 1
    }
}