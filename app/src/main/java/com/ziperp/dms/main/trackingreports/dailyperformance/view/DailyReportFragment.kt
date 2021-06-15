package com.ziperp.dms.main.trackingreports.dailyperformance.view

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.main.trackingreports.dailyperformance.viewmodel.DailyPerformanceViewModel
import kotlinx.android.synthetic.main.fragment_report_4.*

class DailyReportFragment(val mode: Int, private val headerTitle: List<String>): BaseFragment() {

    val viewModel: DailyPerformanceViewModel by activityViewModels()

    lateinit var adapter: DailyReportAdapter

    override fun setLayoutId(): Int = R.layout.fragment_report_4

    override fun initView() {

        tv_0.text = headerTitle[0]
        tv_1.text = headerTitle[1]
        tv_2.text = headerTitle[2]
        tv_3.text = headerTitle[3]

        adapter = DailyReportAdapter(this.mode)
        val layoutManager = LinearLayoutManager(activity)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)

        dataObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun dataObserver() {
        viewModel.dailyReportData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
//                        if (mode == MODE_SALES_ORDER) adapter.updateData(response.data.filter { item -> item.typeObjc == "S"})
//                        else adapter.updateData(response.data.filter { item -> item.typeObjc == "D"})
                        adapter.updateData(response.data)
                    }
                }
                Status.LOADING -> { loading_progressbar.visibility = View.VISIBLE }
                Status.ERROR -> { loading_progressbar.visibility = View.GONE }
            }
        })
    }
}