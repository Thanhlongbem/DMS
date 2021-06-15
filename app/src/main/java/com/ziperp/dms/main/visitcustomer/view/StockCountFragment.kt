package com.ziperp.dms.main.visitcustomer.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.Injection
import com.ziperp.dms.NotificationType
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseCUDActivity.Companion.EXTRA_CUD_MODE
import com.ziperp.dms.base.BaseCUDActivity.Companion.EXTRA_CUD_OBJECT
import com.ziperp.dms.base.BaseCUDActivity.Companion.UPDATE_MODE
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.main.visitcustomer.model.StockCountRequest
import com.ziperp.dms.showNotificationBanner
import kotlinx.android.synthetic.main.fragment_stock_count.*

class StockCountFragment: BaseFragment() {

    lateinit var adapter: StockCountAdapter

    companion object{
        const val KEY_NO = "KEY_NO"
        fun newInstance(keyNo: String): StockCountFragment {
            val fr = StockCountFragment()
            val bundle = Bundle()
            bundle.putString(KEY_NO, keyNo)
            fr.arguments = bundle
            return fr
        }
    }

    var cstNo = ""
    private val viewModel by lazy { getViewModel { Injection.provideStockCountViewModel() } }

    override fun setLayoutId(): Int = R.layout.fragment_stock_count

    override fun initView() {
        arguments?.getString(KEY_NO)?.let {
            if(it.isNotBlank()){
                cstNo = it
            }
        }

        setUpRecyclerView()
        viewModel.stockCountList.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        adapter.data.clear()
                        adapter.addData(it.data)
                        tv_empty.visibility = if (it.data.isEmpty()) View.VISIBLE else View.GONE
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                }
            }
        })

        viewModel.cudRequestStatus.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    response.data?.data?.get(0)?.apply {
                        if (status == "OK") {
                            showNotificationBanner(NotificationType.SUCCESS, errMsg)
                        } else {
                            showNotificationBanner(NotificationType.ERROR, errMsg)
                        }
                        viewModel.getStockCountList(cstNo)
                    }
                }
                Status.ERROR -> {
                    showNotificationBanner(NotificationType.ERROR, "Something went wrong!")
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getStockCountList(cstNo)
    }

    private fun setUpRecyclerView() {
        adapter = StockCountAdapter()
        val layoutManager = LinearLayoutManager(activity)
        rv_listStockCount.layoutManager = layoutManager
        rv_listStockCount.adapter = adapter
        rv_listStockCount.setHasFixedSize(true)

        adapter.onClickListener = { position ->
            val intent = Intent(activity, StockCountModifyActivity::class.java)
            intent.putExtra(EXTRA_CUD_MODE, UPDATE_MODE)
            intent.putExtra(EXTRA_CUD_OBJECT, adapter.data[position])
            startActivity(intent)
        }

        adapter.deleteFunction ={position ->
            val item = adapter.getItem(position)
            val request = StockCountRequest(cstVisitNo = item.cstVisitNo, itemCd = item.itemCd, cstVisitLineNo = item.cstVisitLineNo)
            viewModel.cudItemCount(Constants.CUD.TYPE_DELETE, request)
        }
    }

}