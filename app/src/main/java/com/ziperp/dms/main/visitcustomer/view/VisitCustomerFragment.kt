package com.ziperp.dms.main.visitcustomer.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.AppConfiguration
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseCUDActivity
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.common.paging.OnLoadMoreListener
import com.ziperp.dms.common.paging.RecyclerViewLoadMoreScroll
import com.ziperp.dms.core.filter.FilterDialogFragment
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.FormDataFactory
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.core.tracking.LocationTrackingManager
import com.ziperp.dms.core.tracking.ServiceUtils
import com.ziperp.dms.extensions.toast
import com.ziperp.dms.main.customer.customerdetail.view.CustomerDetailActivity
import com.ziperp.dms.main.saleorder.modify.SalesOrderModifyActivity
import com.ziperp.dms.main.trackingreports.reportsummation.view.ReportSummationActivity
import com.ziperp.dms.main.trackingreports.reportsummation.view.ReportSummationActivity.Companion.EXTRA_CUSTOMER
import com.ziperp.dms.main.visitcustomer.adapter.VisitedCustomerAdapter
import com.ziperp.dms.main.visitcustomer.model.VisitCustomerItem
import com.ziperp.dms.main.visitcustomer.viewmodel.VisitCustomerViewModel
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.DataConvertUtils
import com.ziperp.dms.utils.DistanceWrongValueDialog
import com.ziperp.dms.utils.DistanceWrongValueDialog.Companion.TYPE_CHECK_IN
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_list_visit_customer.*


class VisitCustomerFragment : BaseFragment(), VisitedCustomerAdapter.OnVisitCustomerClick {

    lateinit var adapter: VisitedCustomerAdapter

    val visitActivity by lazy { activity as VisitedCustomerActivity }
    val viewModel: VisitCustomerViewModel by activityViewModels()

    override fun setLayoutId(): Int = R.layout.fragment_list_visit_customer
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll

    private var filterDialog: FilterDialogFragment? = null
    var isRefreshing: Boolean = false
        get() {
            return swipe_refresh.isRefreshing
        }


    @SuppressLint("CheckResult")
    override fun initView() {
        swipe_refresh.setOnRefreshListener {
            LogUtils.d("Refresh")
            refreshData()
        }

        viewModel.visitCustomerLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    scrollListener.setLoaded()
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        if (swipe_refresh.isRefreshing) {
                            adapter.updateData(response.data)
                        } else {
                            adapter.addData(response.data)
                        }
                        visitActivity.setToolbar(
                            "${response.record[0].totalRecords.toInt()} Customers",
                            true
                        )
                    }
                    if (swipe_refresh.isRefreshing) swipe_refresh.isRefreshing = false
                }
                Status.LOADING -> {
                    if (!swipe_refresh.isRefreshing) {
                        loading_progressbar.visibility = View.VISIBLE
                    }
                }
                Status.ERROR -> {
                    scrollListener.setLoaded()
                    loading_progressbar.visibility = View.GONE
                    if (swipe_refresh.isRefreshing) swipe_refresh.isRefreshing = false
                }
            }
        })

        adapter = VisitedCustomerAdapter(requireContext(), arrayListOf())
        adapter.onVisitCustomerClick = this

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                if (viewModel.pagingParam.hasNext()) {
                    viewModel.getVisitCustomer(true)
                }
            }
        })

        recyclerView.addOnScrollListener(scrollListener)


        viewModel.updateVisitCustomerState.observe(this, Observer {
            when (it) {
                1 -> adapter.removeAll()
            }
        })
    }

    fun showFilterDialog() {
        val data = viewModel.latestItemControls ?: FormDataFactory.get(Form.FORM_ID_VISIT_CUSTOMER)

        LogUtils.d("Data size ${data.size}")
        filterDialog = FilterDialogFragment.newInstance(data)
        filterDialog?.show(requireActivity().supportFragmentManager, null)
        filterDialog?.onResult = { itemControls ->
            adapter.removeAll()
            viewModel.pagingParam.clear()
            viewModel.applyFilter(itemControls)
        }
    }

    fun refreshData() {
        viewModel.pagingParam.clear()
        viewModel.getVisitCustomer(false)
    }

    override fun onItemClick(data: VisitCustomerItem) {
        val intent = Intent(activity, CustomerDetailActivity::class.java)
        intent.putExtra(Constants.EXTRA_CUSTOMER_CODE, data.cstCd)
        startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    override fun onCheckInClick(data: VisitCustomerItem) {
        if (ServiceUtils.isGPSEnable(requireContext())) {
            val location = LocationTrackingManager.lastLocation
            var distanceValue = -1f
            if (location != null) {
                if (data.posLat.isNotEmpty() && data.posLng.isNotEmpty()) {
                    distanceValue = AppUtil.distanceBetweenTwoPointInMeter(
                        location.latitude,
                        location.longitude,
                        data.posLat.toDouble(),
                        data.posLng.toDouble()
                    )
                }
                LogUtils.d("Distance $distanceValue")
                if (distanceValue != -1f && distanceValue < AppConfiguration.MAX_CHECK_IN_DISTANCE_ACCEPT) {
                    startActivityForResult(
                        Intent(
                            CheckinCheckoutActivity.newInstance(
                                data,
                                context,
                                true
                            )
                        ), 100
                    )
                } else {
                    val distanceWrongValueDialog = DistanceWrongValueDialog.newInstance(
                        TYPE_CHECK_IN,
                        data,
                        distanceValue.toDouble(),
                        location.latitude,
                        location.longitude
                    ) {
                        startActivityForResult(
                            CheckinCheckoutActivity.newInstance(
                                data,
                                context,
                                false
                            ), 100
                        )
                    }
                    if (activity != null) {
                        distanceWrongValueDialog.show(
                            requireActivity().supportFragmentManager,
                            null
                        )
                    }
                }
            } else {
                "Could not calculate distance between current location and customer".toast(
                    requireContext()
                )
            }
        } else {
            ServiceUtils.showSettingsAlert(requireContext())
        }
    }

    override fun onCreateSale(data: VisitCustomerItem) {
        startActivity(
            SalesOrderModifyActivity.newInstance(
                DataConvertUtils.convertTitle(data.txtCstNm), data.cstCd, data.txtPhone,
                BaseCUDActivity.CREATE_MODE, requireContext()
            )
        )
    }

    override fun onReportClick(data: VisitCustomerItem) {
        val options = ActivityOptions.makeSceneTransitionAnimation(requireActivity())
        val intent = Intent(requireContext(), ReportSummationActivity::class.java)
        intent.putExtra(EXTRA_CUSTOMER, data.cstCd)
        startActivity(intent, options.toBundle())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            swipe_refresh.isRefreshing = true
            refreshData()
        }
    }

}