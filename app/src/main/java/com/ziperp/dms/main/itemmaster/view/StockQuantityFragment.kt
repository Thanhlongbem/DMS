package com.ziperp.dms.main.itemmaster.view

import android.view.View
import androidx.lifecycle.Observer
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.common.adapter.TableViewAdapter
import com.ziperp.dms.common.model.TableData
import com.ziperp.dms.common.model.TableViewModel
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.init
import com.ziperp.dms.main.itemmaster.model.StockQtyResponse
import kotlinx.android.synthetic.main.fragment_stock_qty.*

class StockQuantityFragment(var itemCode: String) : BaseFragment() {
    private var sum: Double = 0.0

    private val viewModel by lazy {
        getViewModel { Injection.provideItemMasterViewModel() }
    }

    override fun setLayoutId(): Int = R.layout.fragment_stock_qty

    override fun initView() {
        initTableViewDataForStockQty(itemCode)
    }

    private fun initTableViewDataForStockQty(itemCode: String) {
        viewModel.getStockQtyData(itemCode)
        viewModel.stockQtyData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {response ->
                        if (response.listItems.isNotEmpty()) {
                            tbViewContent.visibility = View.VISIBLE
                            response.listItems.forEach { item -> sum += item.fltQty }
                            (activity as StockQuantityActivity).setSum(sum)
                            val tableViewModel = TableViewModel(initStockQtyData(response.listItems))
                            tbViewContent.init(TableViewAdapter(), tableViewModel)
                        }
                    }
                    loading_progressbar.visibility = View.GONE
                }

                Status.LOADING -> loading_progressbar.visibility = View.VISIBLE
                Status.ERROR -> loading_progressbar.visibility = View.GONE
            }
        })
    }

    private fun initStockQtyData(stockQuantity: List<StockQtyResponse.Item>): List<TableData> {
        val stockQtyData: MutableList<TableData> = ArrayList()
        val data = mutableMapOf<String, String>()

        for (item in stockQuantity) {
            data["Business Unit"] = item.txtBizUnit ?: ""
            data["Warehouse"] = item.txtLocation ?: ""
            data["Quantity"] = item.fltQty.toInt().toString()
            stockQtyData.add(TableData(data))
        }
        return stockQtyData
    }

}