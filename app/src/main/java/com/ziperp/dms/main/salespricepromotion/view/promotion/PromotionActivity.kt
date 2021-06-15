package com.ziperp.dms.main.salespricepromotion.view.promotion

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
import com.ziperp.dms.core.filter.FilterDialogFragment
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.FormDataFactory
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_item_master.*
import kotlinx.android.synthetic.main.search_box.*
import java.util.concurrent.TimeUnit


class PromotionActivity : BaseActivity() {

    private var filterDialog: FilterDialogFragment? = null
    lateinit var adapter: PromotionAdapter

    private val viewModel by lazy { getViewModel { Injection.providePromotionViewModel() } }

    override fun setLayoutId(): Int = R.layout.activity_item_master

    @SuppressLint("CheckResult")
    override fun initView() {

        setToolbar(R.string.str_sales_price_list.getString(), true)

        viewModel.getPromotionList()
        viewModel.promotionListData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        if(swipe_refresh.isRefreshing){
                            adapter.updateData(response.data)
                        } else {
                            adapter.addData(response.data)
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
                    loading_progressbar.visibility = View.GONE
                    if (swipe_refresh.isRefreshing) swipe_refresh.isRefreshing = false
                }
            }

        })

        initList()

        search_box.textChanges()
            .skip(1)
            .map { it.toString() }
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                adapter.removeAll()
                if (it.isNotBlank()) {
                    viewModel.requestBody.searchInfo = it
                } else {
                    viewModel.requestBody.searchInfo = ""
                }
                viewModel.getPromotionList()
            }, {
                LogUtils.e(it.toString())
            })

        swipe_refresh.setOnRefreshListener {
            viewModel.getPromotionList()
        }
    }

    private fun initList() {
        adapter = PromotionAdapter()

        adapter.onClickListener = { position ->
            val intent = Intent(this, PromotionInfoActivity::class.java)
            intent.putExtra(EXTRA_PROMOTION_CODE, adapter.data[position].promotionNo)
            startActivity(intent)
        }
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
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
        val data = viewModel.latestItemControls ?: FormDataFactory.get(Form.FORM_ID_SALES_PRICE)

        filterDialog = FilterDialogFragment.newInstance(data)
        filterDialog?.show(supportFragmentManager, null)
        filterDialog?.onResult = { itemControls ->
            adapter.removeAll()
            viewModel.applyFilter(itemControls)
        }
    }

    companion object {
        const val EXTRA_PROMOTION_CODE = "EXTRA_PROMOTION_CODE"
    }
}