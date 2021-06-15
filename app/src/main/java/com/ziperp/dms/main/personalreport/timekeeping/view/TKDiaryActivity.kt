package com.ziperp.dms.main.personalreport.timekeeping.view

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import kotlinx.android.synthetic.main.activity_time_keeping_diary.*
import java.text.SimpleDateFormat
import java.util.*

class TKDiaryActivity: BaseActivity() {

    private val viewModel by lazy {
        getViewModel { Injection.provideTKDiaryViewModel() }
    }

    lateinit var adapter: TKDiaryAdapter

    override fun setLayoutId(): Int = R.layout.activity_time_keeping_diary

    override fun initView() {
        val month = SimpleDateFormat("MM/yyyy").format(Date())
        setToolbar("${R.string.str_timekeeping_diary.getString()} - $month", true)

        adapter = TKDiaryAdapter()
        val layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)

        viewModel.getTimeKeepingDiary()
        dataObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun dataObserver() {
        viewModel.timeKeepingDiaryData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        adapter.updateData(response.data)
                        if (!response.summary.isNullOrEmpty()) {
                            response.summary[0].apply {
                                total_time.text = this.totWorkTime
                                late_times.text = this.numbLateTimes
                                early_times.text = this.numbLeaveEarly
                            }
                        }
                    }
                }
                Status.LOADING -> { loading_progressbar.visibility = View.VISIBLE }
                Status.ERROR -> { loading_progressbar.visibility = View.GONE }
            }
        })
    }
}