package com.ziperp.dms.main.personalreport.visitdetail.view

import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import kotlinx.android.synthetic.main.search_box.*

class VisitDetailActivity: BaseActivity() {

    lateinit var visitedFragment: VisitedFragment
    lateinit var notVisitedFragment: NotVisitedFragment

    private var filterDialog: FilterDialogFragment? = null
    private val viewModel by lazy { getViewModel { Injection.provideVisitDetailViewModel() } }

    override fun setLayoutId(): Int = R.layout.activity_sales_result

    override fun initView() {
        setToolbar(R.string.str_visit_customer_detail.getString(), true)
        initPager()
        search_box.visibility = View.GONE
        viewModel.getVisitedDetail()
        viewModel.getNotVisited()
    }

    private fun initPager() {
        visitedFragment = VisitedFragment()
        notVisitedFragment = NotVisitedFragment()

        val pagerAdapter = PagerAdapter(supportFragmentManager, listFragments = listOf(visitedFragment, notVisitedFragment))
        pagerAdapter.listTitles = listOf(R.string.str_visited.getString(), R.string.str_not_visited.getString())

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
        val data = viewModel.latestItemControls ?: FormDataFactory.get(Form.FORM_ID_VISIT_RESULT)

        filterDialog = FilterDialogFragment.newInstance(data)
        filterDialog?.show(supportFragmentManager, null)
        filterDialog?.onResult = { itemControls ->
            visitedFragment.adapter.data.clear()
            notVisitedFragment.adapter.data.clear()
            viewModel.visitedPagingParam.clear()
            viewModel.notVisitedPagingParam.clear()
            viewModel.applyFilter(itemControls)
        }
    }
}