package com.ziperp.dms.main.trackingreports.salesonroute.view

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

class SalesOnRouteActivity: BaseActivity() {

    private val viewModel by lazy {
        getViewModel { Injection.provideSalesOnRouteViewModel() }
    }

    lateinit var adapter: SalesOnRouteAdapter

    override fun setLayoutId(): Int = R.layout.activity_sales_on_route

    override fun initView() {
        setToolbar("${R.string.str_sales_effective.getString()}", true)

        adapter = SalesOnRouteAdapter()
        val layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)

        viewModel.getSalesOnRoute()
        dataObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun dataObserver() {
        viewModel.salesOnRouteData.observe(this, Observer {
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