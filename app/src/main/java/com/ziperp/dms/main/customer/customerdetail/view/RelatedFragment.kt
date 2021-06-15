package com.ziperp.dms.main.customer.customerdetail.view

import android.content.Intent
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.setOnClickNetworkListener
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetail
import com.ziperp.dms.main.customer.customerdetail.viewmodel.CustomerDetailViewModel
import com.ziperp.dms.main.customer.customerdetail.viewmodel.RelatedViewModel
import com.ziperp.dms.main.customer.customerimage.CustomerImageActivity
import com.ziperp.dms.main.customer.customernote.view.CustomerNoteActivity
import com.ziperp.dms.main.customer.customerroute.CustomerRouteActivity
import com.ziperp.dms.utils.NetWorkConnection
import kotlinx.android.synthetic.main.fragment_customer_related.*

class RelatedFragment : BaseFragment() {
    var data: CustomerDetail? = null
    private val viewModel by lazy {
        getViewModel { RelatedViewModel() }
    }

    val customerDetailActivity by lazy { activity as CustomerDetailActivity }
    val activityViewModel: CustomerDetailViewModel by activityViewModels()

    override fun setLayoutId(): Int = R.layout.fragment_customer_related

    override fun initView() {
        data?.let {
            updateData(it)
        }

        viewModel.relatedData.observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    val cache = response.data!!
                    data?.let {
                        if(NetWorkConnection.isNetworkAvailable()){
                            route.text = "${R.string.str_route.getString()} (${it.numRoutes + cache.first})"
                            images.text = "${R.string.str_images.getString()} (${it.numFiles + cache.second})"
                        }else{
                            route.text = "${R.string.str_route.getString()} (${cache.first})"
                            images.text = "${R.string.str_images.getString()} (${cache.second})"
                        }
                        notes.text = "${R.string.str_notes.getString()} (${it.numNotes})"
                    }
                }
            }
        })

    }

    fun updateData(data: CustomerDetail) {
        this.data = data
        data.let {
            route.text = "${R.string.str_route.getString()} (${it.numRoutes})"
            images.text = "${R.string.str_images.getString()} (${it.numFiles})"
            notes.text = "${R.string.str_notes.getString()} (${it.numNotes})"
        }

        layout_route.setOnClickNetworkListener {
            customerDetailActivity.data?.let {
                customerDetailActivity.validateBeforeOffline(it) {
                    val intent = Intent(context, CustomerRouteActivity::class.java)
                    intent.putExtra(Constants.EXTRA_CUSTOMER_CODE, data.cstCd)
                    intent.putExtra(Constants.EXTRA_CUSTOMER_NAME, data.cstNm)
                    startActivity(intent)
                }
            }
        }
        layout_images.setOnClickNetworkListener {
            customerDetailActivity.data?.let {
                customerDetailActivity.validateBeforeOffline(it){
                    val intent = Intent(context, CustomerImageActivity::class.java)
                    intent.putExtra(Constants.EXTRA_CUSTOMER_CODE, data.cstCd)
                    intent.putExtra(Constants.EXTRA_CUSTOMER_NAME, data.cstNm)
                    startActivity(intent)
                }
            }
        }
        layout_notes.setOnClickNetworkListener {
            customerDetailActivity.data?.let {
                customerDetailActivity.validateBeforeOffline(it){
                    val intent = Intent(context, CustomerNoteActivity::class.java)
                    intent.putExtra(Constants.EXTRA_CUSTOMER_CODE, data.cstCd)
                    intent.putExtra(Constants.EXTRA_CUSTOMER_NAME, data.cstNm)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        data?.let {
            viewModel.getImagesAndRoutes(it.cstCd)
        }
    }
}