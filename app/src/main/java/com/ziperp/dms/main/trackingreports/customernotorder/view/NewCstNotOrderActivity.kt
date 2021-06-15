package com.ziperp.dms.main.trackingreports.customernotorder.view

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
import com.ziperp.dms.main.trackingreports.customernotorder.model.CustomerNotOrderResponse
import com.ziperp.dms.main.trackingreports.customernotorder.model.NewCustomerResponse
import kotlinx.android.synthetic.main.activity_item_master_detail.*

class NewCstNotOrderActivity: BaseActivity() {

    lateinit var newCustomerFragment: NewCustomerFragment
    lateinit var customerNotOrderFragment: CustomerNotOrderFragment

    private var filterDialog: FilterDialogFragment? = null
    private val viewModel by lazy {
        getViewModel { Injection.provideCstNotOrderViewModel() }
    }

    override fun setLayoutId(): Int = R.layout.activity_customer_not_order

    override fun initView() {
        setToolbar(R.string.str_new_customer_not_order.getString(), true)
        initPager()
        viewModel.getNewCustomerList()
        viewModel.getCustomerNotOrder()
    }

    private fun initPager() {
        newCustomerFragment = NewCustomerFragment(NewCustomerResponse())
        customerNotOrderFragment = CustomerNotOrderFragment(CustomerNotOrderResponse())

        val pagerAdapter = PagerAdapter(supportFragmentManager, listFragments = listOf(newCustomerFragment, customerNotOrderFragment))
        pagerAdapter.listTitles = listOf(R.string.str_new_customer.getString(), R.string.str_customer_not_order.getString())

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
        val data = viewModel.latestItemControls ?: FormDataFactory.get(Form.FORM_ID_NEW_NOT_ORDER)

        filterDialog = FilterDialogFragment.newInstance(data)
        filterDialog?.show(supportFragmentManager, null)
        filterDialog?.onResult = { itemControls ->
            newCustomerFragment.adapter.data.clear()
            customerNotOrderFragment.adapter.data.clear()
            viewModel.newCustomerPagingParam.clear()
            viewModel.notOrderPagingParam.clear()
            viewModel.applyFilter(itemControls)
        }
    }
}