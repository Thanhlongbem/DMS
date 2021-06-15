package com.ziperp.dms.main.user.view

import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnTouchListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.core.rest.TrackingRequest
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.toast
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetail
import com.ziperp.dms.main.customer.customerdetail.view.CustomerDetailActivity
import com.ziperp.dms.main.timekeeping.model.TimeKeeping
import com.ziperp.dms.main.user.adapter.CachedDataAdapter
import com.ziperp.dms.main.user.viewmodel.CachedViewModel
import com.ziperp.dms.utils.NetWorkConnection
import kotlinx.android.synthetic.main.activity_cached_data.*
import kotlinx.android.synthetic.main.activity_sale_order.loading_progressbar
import kotlinx.android.synthetic.main.activity_sale_order.recyclerView


class CachedDataActivity : BaseActivity() {

    private val viewModel by lazy { getViewModel { CachedViewModel() } }
    lateinit var adapter: CachedDataAdapter

    override fun setLayoutId(): Int = R.layout.activity_cached_data
    var type = TIMEKEEPING_TYPE
    override fun initView() {
        type = intent.getIntExtra(EXTRA_TYPE, TIMEKEEPING_TYPE)

        adapter = CachedDataAdapter()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        recyclerView.setOnTouchListener(OnTouchListener { v, event ->
            findViewById<View>(R.id.child_scroll)?.parent?.requestDisallowInterceptTouchEvent(false)
            false
        })

        recyclerView

        adapter.onClickListener = {
            val item = adapter.getItem(it)
            when(item){
                is CustomerDetail -> {
                    val intent = Intent(this, CustomerDetailActivity::class.java)
                    intent.putExtra(Constants.EXTRA_CUSTOMER_DATA, item)
                    startActivity(intent)
                }
            }
        }

        adapter.syncFunction = {
            val item = adapter.getItem(it)
            when(item){
                is CustomerDetail -> {
                    if(NetWorkConnection.isNetworkAvailable()){
                        viewModel.syncCustomerDetail(item)
                    }else{
                        "No internet connection".toast()
                    }
                }
            }
        }

        adapter.deleteFunction = {
            viewModel.removeItem(adapter.getItem(it))
            adapter.removeAt(it)
            setToolbar(
                "${adapter.data.size} ${R.string.str_items.getString()}",
                true
            )
        }

        viewModel.cachedListLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { list ->
                        adapter.addData(list)
                        setToolbar(
                            "${list.size} ${R.string.str_items.getString()}",
                            true
                        )
                    }
                }
                Status.LOADING -> {
                    loading_progressbar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    loading_progressbar.visibility = View.GONE
                }
            }

        })

        viewModel.getDataCachedRequests(type)

        viewModel.synchronizedSuccessLiveData.observe(this, Observer {
            when (it) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    adapter.removeAll()
                    setToolbar(
                        "0 ${R.string.str_items.getString()}",
                        true
                    )
                    "Synchronize data successful".toast()
                }
                Status.LOADING -> {
                    loading_progressbar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    loading_progressbar.visibility = View.GONE
                }
            }
        })

        but_sync.setOnClickListener {
            if(adapter.data.isNotEmpty()){
                if(NetWorkConnection.isNetworkAvailable()){
                    syncData()
                }else{
                    "No internet connection".toast()
                }
            }else{
                "Empty list".toast()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        menu?.findItem(R.id.action_delete)?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_delete -> {
                viewModel.deleteAll(type)
                adapter.removeAll()
                setToolbar(
                    "0 ${R.string.str_items.getString()}",
                    true
                )
            }
        }
        return true
    }

    private fun syncData() {
        if(adapter.data.isNotEmpty()){
            when(type){
                TRACKING_TYPE ->{
                    viewModel.syncTrackingData(adapter.data as ArrayList<TrackingRequest>)
                }
            }
        }else{
            "No data need to be synchronized".toast()
        }
    }

    companion object {
        const val TIMEKEEPING_TYPE = 0
        const val TRACKING_TYPE = 1
        const val CONTROL_TYPE = 2
        const val CUSTOMER_TYPE = 3
        const val CUSTOMER_IMAGE_TYPE = 4
        const val CUSTOMER_ROUTE_TYPE = 5
        const val VISIT_CUSTOMER_TYPE = 6
        const val VISIT_IMAGE_TYPE = 8
        const val EXTRA_TYPE = "EXTRA_TYPE"

        fun newInstance(
            context: Context,
            type: Int
            ): Intent {
            val intent = Intent(context, CachedDataActivity::class.java)
            intent.putExtra(EXTRA_TYPE, type)
            return intent
        }
    }
}