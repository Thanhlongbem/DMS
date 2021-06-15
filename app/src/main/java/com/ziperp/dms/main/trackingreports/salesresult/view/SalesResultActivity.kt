package com.ziperp.dms.main.trackingreports.salesresult.view

import android.annotation.SuppressLint
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding3.widget.textChanges
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.base.PagerAdapter
import com.ziperp.dms.core.filter.FilterDialogFragment
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.FormDataFactory
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_item_master_detail.*
import kotlinx.android.synthetic.main.search_box.*
import java.util.concurrent.TimeUnit

class SalesResultActivity: BaseActivity() {

    lateinit var summationFragment: SummationFragment
    lateinit var detailFragment: SalesDetailFragment

    private var filterDialog: FilterDialogFragment? = null
    private val viewModel by lazy { getViewModel { Injection.provideSalesResultViewModel() } }

    override fun setLayoutId(): Int = R.layout.activity_sales_result

    override fun initView() {
        setToolbar(R.string.str_staff_sales_result.getString(), true)
        initPager()
        setUpSearchBox()
        viewModel.getSummationResult()
        viewModel.getDetailResult()
    }

    private fun initPager() {
        summationFragment = SummationFragment()
        detailFragment = SalesDetailFragment()

        val pagerAdapter = PagerAdapter(supportFragmentManager, listFragments = listOf(summationFragment, detailFragment))
        pagerAdapter.listTitles = listOf(R.string.str_result_summation.getString(), R.string.str_result_detail.getString())

        viewpager.adapter = pagerAdapter
        viewpager.offscreenPageLimit = 2
        tab.setupWithViewPager(viewpager)
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
                summationFragment.adapter.removeAll()
                detailFragment.adapter.removeAll()
                viewModel.summationPagingParam.clear()
                viewModel.detailPagingParam.clear()
                if (it.isNotBlank()) {
                    viewModel.summationRequest.searchInfo = it
                    viewModel.detailRequest.searchInfo = it
                } else {
                    viewModel.summationRequest.searchInfo = ""
                    viewModel.detailRequest.searchInfo = ""
                }
                viewModel.getSummationResult()
                viewModel.getDetailResult()
            }, {
                LogUtils.e(it.toString())
            })
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
        val data = viewModel.latestItemControls ?: FormDataFactory.get(Form.FORM_ID_SALES_RESULT)

        filterDialog = FilterDialogFragment.newInstance(data)
        filterDialog?.show(supportFragmentManager, null)
        filterDialog?.onResult = { itemControls ->
            summationFragment.adapter.removeAll()
            detailFragment.adapter.removeAll()
            viewModel.summationPagingParam.clear()
            viewModel.detailPagingParam.clear()
            viewModel.applyFilter(itemControls)
        }
    }
}