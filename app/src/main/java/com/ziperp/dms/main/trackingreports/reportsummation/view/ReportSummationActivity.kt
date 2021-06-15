package com.ziperp.dms.main.trackingreports.reportsummation.view

import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.animation.DecelerateInterpolator
import androidx.lifecycle.Observer
import androidx.transition.Slide
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.base.PagerAdapter
import com.ziperp.dms.core.filter.FilterDialogFragment
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.FormDataFactory
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.main.trackingreports.reportsummation.model.ReportSummationRequest
import com.ziperp.dms.main.trackingreports.reportsummation.model.ReportSummationResponse
import kotlinx.android.synthetic.main.activity_report_summation.*

class ReportSummationActivity: BaseActivity() {

    lateinit var orderFragment: OrderSummationFragment
    lateinit var debtFragment: DebtSummationFragment
    lateinit var stockFragment: StockSummationFragment

    private var filterDialog: FilterDialogFragment? = null

    private val viewModel by lazy {
        getViewModel { Injection.provideReportSummationViewModel() }
    }

    override fun setLayoutId(): Int = R.layout.activity_report_summation

    override fun initView() {
        setToolbar(R.string.str_summation_report.getString(), true)
        dataObserver()
        initPager()
        val customerCode = intent.getStringExtra(EXTRA_CUSTOMER)
        if (customerCode.isNullOrEmpty()) {
            showFilterDialog()
        } else {
            val request = ReportSummationRequest(
                cstCd = customerCode,
                startDate = "20210101",
                endDate = "20211231"
            )
            viewModel.apply {
                reportSummationRequest = request
                orderRequest = request
                debtRequest = request
                stockRequest = request

                getReportSummation()
                getOrderReport()
                getDebtReport()
                getStockReport()
            }
        }
    }

    private fun dataObserver() {
        viewModel.reportSummationData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        if (it.data.isNotEmpty()) updateData(it.data[0])
                    }
                }
                Status.LOADING -> { }
                Status.ERROR -> { }
            }
        })
    }

    private fun initPager() {
        orderFragment = OrderSummationFragment()
        debtFragment = DebtSummationFragment()
        stockFragment = StockSummationFragment()

        val pagerAdapter = PagerAdapter(supportFragmentManager, listFragments = listOf(orderFragment, debtFragment, stockFragment))
        pagerAdapter.listTitles = listOf(R.string.str_order.getString(), R.string.str_debt.getString(), R.string.str_stock.getString())

        viewpager.adapter = pagerAdapter
        viewpager.offscreenPageLimit = 2
        tab.setupWithViewPager(viewpager)
    }

    private fun updateData(data: ReportSummationResponse.ReportSummation) {
        data.apply {
            tv_customer_name.text = customerName
            tv_visit_begin.text = visitBegin.toString()
            tv_visit_period.text = visitInPeriod.toString()
            tv_visit_end.text = visitEnd.toString()
            tv_sales_begin.text = orderBegin.toString()
            tv_sales_period.text = orderInPeriod.toString()
            tv_sales_end.text = orderEnd.toString()
            tv_quantity_begin.text = qtyBegin.format()
            tv_quantity_period.text = qtyInPeriod.format()
            tv_quantity_end.text = qtyEnd.format()
            tv_amount_begin.text = amountBegin.format()
            tv_amount_period.text = amountInPeriod.format()
            tv_amount_end.text = amountEnd.format()
            tv_uncollected_begin.text = debtBegin.format()
            tv_uncollected_period.text = debtInPeriod.format()
            tv_uncollected_end.text = debtEnd.format()
        }
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
        val data = viewModel.latestItemControls ?: FormDataFactory.get(Form.FORM_ID_REPORT_SUMMARY)

        filterDialog = FilterDialogFragment.newInstance(data)
        filterDialog?.show(supportFragmentManager, null)
        filterDialog?.onResult = { itemControls ->
            viewModel.applyFilter(itemControls)
        }
    }

    companion object {
        const val EXTRA_CUSTOMER = "EXTRA_CUSTOMER"
    }
}