package com.ziperp.dms.main.personalreport.salesresult.view

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
import kotlinx.android.synthetic.main.activity_item_master_detail.*

class YourSalesResultActivity: BaseActivity() {

    lateinit var customerFragment: YourSalesResultFragment
    lateinit var itemFragment: YourSalesResultFragment

    private var filterDialog: FilterDialogFragment? = null
    private val viewModel by lazy {
        getViewModel { Injection.provideYourSalesResultViewModel() }
    }

    override fun setLayoutId(): Int = R.layout.activity_customer_not_order

    override fun initView() {
        setToolbar(R.string.str_your_sales_result.getString(), true)
        initPager()
        viewModel.getCustomerResult()
        viewModel.getItemResult()
    }

    private fun initPager() {
        customerFragment = YourSalesResultFragment( MODE_CUSTOMER,
            listOf(R.string.str_no_.getString(), R.string.str_customer.getString(), R.string.str_quantity.getString(), R.string.str_total_amount.getString()))
        itemFragment = YourSalesResultFragment( MODE_ITEM,
            listOf(R.string.str_no_.getString(), R.string.str_sale_item.getString(), R.string.str_quantity.getString(), R.string.str_total_amount.getString()))

        val pagerAdapter = PagerAdapter(supportFragmentManager, listFragments = listOf(customerFragment, itemFragment))
        pagerAdapter.listTitles = listOf(R.string.str_by_customer.getString(), R.string.str_by_item.getString())

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
        val data = viewModel.latestItemControls ?: FormDataFactory.get(Form.FORM_ID_YOUR_SALES_RESULT)

        filterDialog = FilterDialogFragment.newInstance(data)
        filterDialog?.show(supportFragmentManager, null)
        filterDialog?.onResult = { itemControls ->
            customerFragment.adapter.removeAll()
            itemFragment.adapter.removeAll()
            viewModel.applyFilter(itemControls)
        }
    }

    companion object {
        const val MODE_CUSTOMER = 0
        const val MODE_ITEM = 1
    }
}