package com.ziperp.dms.main.visitcustomer.view

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.main.saleorder.view.SalesOrderDetailActivity
import com.ziperp.dms.main.visitcustomer.adapter.SaleOrderOfVisitInfoAdapter
import com.ziperp.dms.main.visitcustomer.view.CheckinCheckoutActivity.Companion.MODE_CHECK_IN
import com.ziperp.dms.main.visitcustomer.viewmodel.VisitCustomerViewModel
import kotlinx.android.synthetic.main.fragment_visit_info.*

class VisitInfoFragment(var cstVisitNo: String, val note: String = "", val mode: Int): BaseFragment() {


    lateinit var adapter: SaleOrderOfVisitInfoAdapter

    val viewModel: VisitCustomerViewModel by activityViewModels()

    override fun setLayoutId(): Int = R.layout.fragment_visit_info

    override fun onResume() {
        super.onResume()
        viewModel.getVisitCustomerOrder(cstVisitNo)
    }

    override fun initView() {
        adapter = SaleOrderOfVisitInfoAdapter()
        tv_visitRemarkContent.isEnabled = mode == MODE_CHECK_IN
        tv_visitRemarkContent.setText(note)

        val layoutManager = LinearLayoutManager(activity)
        rv_salesOrder.layoutManager = layoutManager
        rv_salesOrder.adapter = adapter
        rv_salesOrder.setHasFixedSize(true)


        adapter.onClickListener = {
            val item = adapter.getItem(position = it)
            startActivity(SalesOrderDetailActivity.newInstance(item, requireContext()))
        }
        dataObserver()

    }


    private fun dataObserver() {
        viewModel.salesOrderLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.let {
                        if (it.data.isNotEmpty()) {
                            adapter.updateData(it.data)
                            rv_salesOrder.visibility = View.VISIBLE
                            tv_noSaleOrder.visibility = View.GONE
                        } else {
                            tv_noSaleOrder.visibility = View.VISIBLE
                            rv_salesOrder.visibility = View.GONE
                        }
                    }
                }
                Status.LOADING -> loading_progressbar.visibility = View.VISIBLE
                Status.ERROR -> {
                    loading_progressbar.visibility = View.GONE
                    tv_noSaleOrder.visibility = View.VISIBLE
                    rv_salesOrder.visibility = View.GONE
                }
            }
        })
    }

    fun getRemark(): String = tv_visitRemarkContent.text.toString()
}