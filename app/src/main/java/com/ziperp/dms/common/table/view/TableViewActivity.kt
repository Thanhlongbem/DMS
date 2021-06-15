package com.ziperp.dms.common.table.view

import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getViewModel
import kotlinx.android.synthetic.main.activity_main.*

class TableViewActivity : BaseActivity() {

    override fun setLayoutId() = R.layout.activity_main
    private val viewModel by lazy {
        getViewModel {Injection.provideTableAllViewModel()}
    }

    override fun initView() {
        viewModel.getAllTableData()
        viewModel.tableLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.getSaleReport()
        viewModel.saleReportData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}