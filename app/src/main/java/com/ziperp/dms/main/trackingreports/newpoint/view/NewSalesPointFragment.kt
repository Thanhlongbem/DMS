package com.ziperp.dms.main.trackingreports.newpoint.view

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.main.trackingreports.newpoint.viewmodel.NewSalesPointViewModel
import kotlinx.android.synthetic.main.fragment_report_4.*

class NewSalesPointFragment: BaseFragment() {

    val viewModel: NewSalesPointViewModel by activityViewModels()

    lateinit var adapter: NewSalesPointAdapter

    override fun setLayoutId(): Int = R.layout.fragment_report_4

    override fun initView() {

        tv_0.text = R.string.str_dept_staff.getString()
        tv_1.text = R.string.str_begin_month.getString()
        tv_2.text = R.string.str_new_open.getString()
        tv_3.text = R.string.str_end_month.getString()

        adapter = NewSalesPointAdapter()
        val layoutManager = LinearLayoutManager(activity)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)

        dataObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun dataObserver() {
        viewModel.newSalesPointData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        adapter.updateData(response.data)
                    }
                }
                Status.LOADING -> { loading_progressbar.visibility = View.VISIBLE }
                Status.ERROR -> { loading_progressbar.visibility = View.GONE }
            }
        })
    }
}