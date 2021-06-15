package com.ziperp.dms.main.personalreport.datasummary.view

import android.view.View
import androidx.lifecycle.Observer
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.core.filter.FilterDialogFragment
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.main.personalreport.datasummary.model.DataSummaryResponse
import kotlinx.android.synthetic.main.activity_data_summary.*

class DataSummaryActivity: BaseActivity() {

    private var filterDialog: FilterDialogFragment? = null

    private val viewModel by lazy {
        getViewModel { Injection.provideDataSummaryViewModel() }
    }

    override fun setLayoutId(): Int = R.layout.activity_data_summary

    override fun initView() {
        setToolbar(R.string.str_your_performance.getString(), true)

        viewModel.getDataSummary()

        viewModel.summaryData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        if (!response.data.isNullOrEmpty()) {
                            updateData(response.data[0])
                        }
                    }
                }
                Status.LOADING -> loading_progressbar.visibility = View.VISIBLE
                Status.ERROR -> loading_progressbar.visibility = View.GONE

            }
        })
    }

    private fun updateData(data: DataSummaryResponse.DataSummary) {
        data.apply {
            tv_begin_month.text = newCstBeginMonth.toString()
            tv_new_open.text = newCstInPeriod.toString()
            tv_end_month.text = newCstEndMonth.toString()

            tv_numb_of_SO.text = numSalesOrder.toString()
            tv_total_quantity.text = totalQuantity.format()
            tv_total_amount.text = totalAmount.format()

            tv_customer_on_route.text = numbCustomerOnRoute.toString()
            tv_customer_visited.text = numbCustomerVisited.toString()
            tv_customer_not_visited.text = numbCustomerNotVisit.toString()
            tv_visit_times.text = numbVisitTimes.toString()
            tv_right_route.text = visitRightRoute.toString()
            tv_wrong_route.text = visitWrongRoute.toString()
            tv_right_location.text = visitRightLoc.toString()
            tv_wrong_location.text = visitWrongLoc.toString()
            tv_morning_visit.text = morningVisit.toString()
            tv_afternoon_visit.text = afternoonVisit.toString()
            tv_with_order.text = visitWithOrder.toString()
            tv_total_distance.text = moveDistance.format()

        }
    }
}