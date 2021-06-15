package com.ziperp.dms.main.trackingreports.newpoint.view

import android.view.Menu
import android.view.MenuItem
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.core.filter.FilterDialogFragment
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.FormDataFactory
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.utils.AppUtil

class NewSalesPointActivity: BaseActivity() {

    private val viewModel by lazy {
        getViewModel { Injection.provideNewSalesPointViewModel() }
    }
    private var filterDialog: FilterDialogFragment? = null
    private lateinit var fragment: NewSalesPointFragment

    override fun setLayoutId(): Int = R.layout.activity_new_sales_point

    override fun initView() {
        setToolbar(R.string.str_point_of_sales.getString(), true)
        viewModel.getSalesPoint()
        fragment = NewSalesPointFragment()
        AppUtil.addFragmentToActivity(supportFragmentManager, fragment, R.id.layout_fragment, false, "")

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
        val data = viewModel.latestItemControls ?: FormDataFactory.get(Form.FORM_ID_NEW_SALES_POINT)

        filterDialog = FilterDialogFragment.newInstance(data)
        filterDialog?.show(supportFragmentManager, null)
        filterDialog?.onResult = { itemControls ->
            fragment.adapter.removeAll()
            viewModel.applyFilter(itemControls)
        }
    }
}