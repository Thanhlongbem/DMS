package com.ziperp.dms.main.itemmaster.view

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
import com.ziperp.dms.common.paging.OnLoadMoreListener
import com.ziperp.dms.common.paging.RecyclerViewLoadMoreScroll
import com.ziperp.dms.core.filter.FilterDialogFragment
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.FormDataFactory
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.main.itemmaster.adapter.ItemMasterAdapter
import com.ziperp.dms.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_sale_order.*
import kotlinx.android.synthetic.main.search_box.*
import java.util.concurrent.TimeUnit


class ItemMasterActivity : BaseActivity() {

    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private var filterDialog: FilterDialogFragment? = null
    lateinit var adapter: ItemMasterAdapter

    private val viewModel by lazy { getViewModel { Injection.provideItemMasterViewModel() } }

    override fun setLayoutId(): Int = R.layout.activity_item_master

    @SuppressLint("CheckResult")
    override fun initView() {

        viewModel.getItemMasterList()
        viewModel.itemMasterData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    scrollListener.setLoaded()
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        if(swipe_refresh.isRefreshing){
                            adapter.updateData(response.data)
                        } else{
                            adapter.addData(response.data)
                        }
                        setToolbar(
                            "${response.record[0].totalRecords.toInt()} ${R.string.str_items.getString()}",
                            true
                        )
                    }
                    if (swipe_refresh.isRefreshing) swipe_refresh.isRefreshing = false
                }
                Status.LOADING -> {
                    if (!swipe_refresh.isRefreshing){
                        loading_progressbar.visibility = View.VISIBLE
                    }
                }
                Status.ERROR -> {
                    scrollListener.setLoaded()
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
                viewModel.pagingParam.clear()
                if (it.isNotBlank()) {
                    viewModel.requestBody.itemInfo = it
                } else {
                    viewModel.requestBody.itemInfo = ""
                }
                viewModel.getItemMasterList()
            }, {
                LogUtils.e(it.toString())
            })

        swipe_refresh.setOnRefreshListener {
            LogUtils.d("Refresh")
            viewModel.pagingParam.clear()
            viewModel.getItemMasterList(false)
        }
    }

    private fun initList() {
        adapter = ItemMasterAdapter()
        adapter.onStockQTYClick = { item ->
            val intent = Intent(this, StockQuantityActivity::class.java)
            intent.putExtra(EXTRA_ITEM_CODE, item.itemCd)
            intent.putExtra(EXTRA_ITEM_NAME, "${item.txtItemNo} - ${item.txtItemNm}")
            intent.putExtra(EXTRA_ITEM_UNIT, item.txtWhUnitCd)
            intent.putExtra(EXTRA_ITEM_LS_CTR, item.txtLotSeriCtrl)
            startActivity(intent)
        }
        adapter.onClickListener = {position ->
            val intent = Intent(this, ItemMasterDetailActivity::class.java)
            intent.putExtra(EXTRA_ITEM_CODE, adapter.data[position].itemCd)
            startActivity(intent)
        }
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                if (viewModel.pagingParam.hasNext()) {
                    viewModel.getItemMasterList(true)
                }
            }
        })

        recyclerView.addOnScrollListener(scrollListener)
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
        val data = viewModel.latestItemControls ?: FormDataFactory.get(Form.FORM_ID_ITEM_MASTER)

        filterDialog = FilterDialogFragment.newInstance(data)
        filterDialog?.show(supportFragmentManager, null)
        filterDialog?.onResult = { itemControls ->
            adapter.removeAll()
            viewModel.pagingParam.clear()
            viewModel.applyFilter(itemControls)
        }
    }

    companion object {
        const val EXTRA_ITEM_CODE = "EXTRA_ITEM_CODE"
        const val EXTRA_ITEM_NAME = "EXTRA_ITEM_NAME"
        const val EXTRA_ITEM_UNIT = "EXTRA_ITEM_UNIT"
        const val EXTRA_ITEM_LS_CTR = "EXTRA_ITEM_LS_CTR"
    }
}