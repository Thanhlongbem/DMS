package com.ziperp.dms.main.personalreport.salesresult.view

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.main.personalreport.salesresult.viewmodel.YourSalesResultViewModel
import kotlinx.android.synthetic.main.fragment_your_sales_report.*

class YourSalesResultFragment(val mode: Int, private val headerTitle: List<String>): BaseFragment() {

    val viewModel: YourSalesResultViewModel by activityViewModels()

    lateinit var adapter: YourSalesResultAdapter

    override fun setLayoutId(): Int = R.layout.fragment_your_sales_report

    override fun initView() {

        tv_0.text = headerTitle[0]
        tv_1.text = headerTitle[1]
        tv_2.text = headerTitle[2]
        tv_3.text = headerTitle[3]

        adapter = YourSalesResultAdapter()
        val layoutManager = LinearLayoutManager(activity)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)

        dataObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun dataObserver() {
        if (mode == 0) {
            viewModel.customerResultData.observe(this, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        loading_progressbar.visibility = View.GONE
                        it.data?.let { response ->
                            adapter.updateData(response.data)
                            tv_total.text = "${R.string.str_total.getString()} ${response.record[0].totalRecords} ${R.string.str_customer.getString()}"
                            tv_quantity.text = if(response.data.isEmpty()) "0" else ((response.data.map { item -> item.quantity }).reduce { sum, e -> sum + e }).format()
                            tv_amount.text = if(response.data.isEmpty()) "0" else ((response.data.map { item -> item.totAmount }).reduce { sum, e -> sum + e }).format()
                        }
                    }
                    Status.LOADING -> { loading_progressbar.visibility = View.VISIBLE }
                    Status.ERROR -> { loading_progressbar.visibility = View.GONE }
                }
            })
        } else {
            viewModel.itemResultData.observe(this, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        loading_progressbar.visibility = View.GONE
                        it.data?.let { response ->
                            adapter.updateData(response.data)
                            tv_total.text = "${R.string.str_total.getString()} ${response.record[0].totalRecords}"
                            tv_quantity.text = if(response.data.isEmpty()) "0" else ((response.data.map { item -> item.quantity }).reduce { sum, e -> sum + e }).format()
                            tv_amount.text = if(response.data.isEmpty()) "0" else ((response.data.map { item -> item.totAmount }).reduce { sum, e -> sum + e }).format()
                        }
                    }
                    Status.LOADING -> { loading_progressbar.visibility = View.VISIBLE }
                    Status.ERROR -> { loading_progressbar.visibility = View.GONE }
                }
            })
        }
    }
}