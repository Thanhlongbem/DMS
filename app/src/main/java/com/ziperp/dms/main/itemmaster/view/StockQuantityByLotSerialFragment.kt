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
import com.ziperp.dms.main.itemmaster.model.StockQtyByLotSerialResponse
import com.ziperp.dms.utils.AppUtil
import kotlinx.android.synthetic.main.fragment_stock_qty.*

class StockQuantityByLotSerialFragment(var itemCode: String) : BaseFragment() {
    private var sum: Double = 0.0

    private val viewModel by lazy {
        getViewModel { Injection.provideItemMasterViewModel() }
    }

    override fun setLayoutId(): Int = R.layout.fragment_stock_qty

    override fun initView() {
        initTableViewDataForStockQtyByLotSerial(itemCode)
    }

    private fun initTableViewDataForStockQtyByLotSerial(itemCode: String) {
        viewModel.getStockQtyByLotSerial(itemCode)
        viewModel.stockQtyByLotSerialData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        if (response.listItems.isNotEmpty()) {
                            tbViewContent.visibility = View.VISIBLE
                            response.listItems.forEach { item -> sum += item.fltBeginStkQty }
                            (activity as StockQuantityActivity).setSum(sum)
                            val tableViewModel = TableViewModel(initStockQtyByLotSerialData(response.listItems))
                            tbViewContent.init(TableViewAdapter(), tableViewModel)
                        }
                    }
                }
                Status.LOADING -> loading_progressbar.visibility = View.VISIBLE
                Status.ERROR -> loading_progressbar.visibility = View.GONE
            }
        })
    }

    private fun initStockQtyByLotSerialData(stockQuantity: List<StockQtyByLotSerialResponse.Item>): List<TableData> {
        val stockQtyByLotSerialData: MutableList<TableData> = ArrayList()
        val data = mutableMapOf<String, String>()

        for (item in stockQuantity) {
            data["Warehouse"] = item.txtBizAndWh ?: ""
            data["Lot/Serial No."] = item.txtLotSeriNo ?: ""
            data["Expiry Date"] = AppUtil.formatDateString(item.dtExpiryDate ?: "")
            data["Qty."] = item.fltBeginStkQty.toInt().toString()
            stockQtyByLotSerialData.add(TableData(data))
        }

        return stockQtyByLotSerialData
    }

}