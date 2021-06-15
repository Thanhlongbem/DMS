package com.ziperp.dms.main.customer.customerroute

import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ziperp.dms.Injection
import com.ziperp.dms.NotificationType
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.core.form.dialogpopup.view.DialogPopupActivity
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.form.model.ItemControlSelection
import com.ziperp.dms.core.rest.*
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.toJsonString
import com.ziperp.dms.main.customer.customerroute.model.CustomerRoute
import com.ziperp.dms.main.customer.customerroute.model.RouteItem
import com.ziperp.dms.showNotificationBanner
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.activity_customer_route.*
import kotlinx.android.synthetic.main.activity_customer_route.loading_progressbar
import kotlinx.android.synthetic.main.fragment_items.*

class CustomerRouteActivity: BaseActivity() {

    lateinit var customerCode: String
    lateinit var adapter: CustomerRouteAdapter
    private val viewModel by lazy { getViewModel { Injection.provideCustomerRouteViewModel() } }

    val itemControlForm = ItemControlForm(
        formId = Form.FORM_ID_VISIT_CUSTOMER,
        controlId = "txtRoute",
        controlName = "Route",
        lookUpCode = "ROUT",
        lookUpType = Form.LookUpType.DIALOG_MUL,
        findType = Form.FindType.NAME
    ).apply {
        param.item1 = "1"
    }

    override fun setLayoutId(): Int = R.layout.activity_customer_route

    override fun initView() {
        setToolbar(R.string.str_route.getString(), true)

        customerCode = intent.getStringExtra(Constants.EXTRA_CUSTOMER_CODE)
        val customerName = intent.getStringExtra(Constants.EXTRA_CUSTOMER_NAME)

        tv_title.text = String.format(R.string.str_route_of.getString(), customerName)

        adapter = CustomerRouteAdapter()
        rv_routes.layoutManager = LinearLayoutManager(this)
        rv_routes.adapter = adapter
        rv_routes.setHasFixedSize(true)

        dataObserver()
        viewModel.getListRoute2(customerCode)

        adapter.deleteFunction = { position ->
            val item = adapter.getItem(position)
            viewModel.deleteRoute(item, position)
        }

    }


    private fun dataObserver() {
        viewModel.routeListData2.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.let {
                        adapter.updateData(it)
                        tv_numbs_route.text = it.size.toString() + " " + R.string.str_route.getString()
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

        viewModel.addRouteStatus.observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        showNotificationBanner(NotificationType.SUCCESS, it.message)
                        viewModel.getListRoute2(customerCode)
                    }
                }
            }
        })

        viewModel.deleteRouteStatus.observe(this, Observer { response ->
            LogUtils.d("Result " + response.toJsonString())
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {cudData ->
                        when (cudData.status) {
                            CUD_SUCCESS -> {
                                showNotificationBanner(NotificationType.SUCCESS, cudData.message)
                                val position = cudData.data as Int
                                adapter.removeItem(position)
                                tv_numbs_route.text = adapter.data.size.toString() + " " + R.string.str_route.getString()
                            }
                            CUD_CONFLICT -> {
                                showNotificationBanner(NotificationType.ERROR, cudData.message)
                            }
                            CUD_OFFLINE -> {
                                showNotificationBanner(NotificationType.INFO, cudData.message)
                            }
                            CUD_ERROR -> {
                                showNotificationBanner(NotificationType.ERROR, cudData.message)
                            }
                        }
                    }
                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        menu?.findItem(R.id.action_add)?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> selectRoute()
            else -> {
            }
        }
        return true
    }

    private fun selectRoute() {
        val intent = Intent(this, DialogPopupActivity::class.java)
        intent.putExtra("itemControl", itemControlForm)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            data?.let {
                val result: ItemControlSelection = it.getSerializableExtra("result") as ItemControlSelection
//                itemControlForm.controlValue = result.keyValues //Ignore keeping setting
                if(result.keyValues.isNotEmpty()){
                    val listCustomerRoutes = result.keyValues
                        .map { Gson().fromJson(it.jsonData, RouteItem::class.java) }
                        .map {
                            val customerRoute = CustomerRoute()
                            customerRoute.apply {
                                routeId = it.routeId
                                cstCd = customerCode
                                routeNo = it.routeNo
                                routeNm = it.routeNm
                                startDate = it.startDate
                                endDate = it.endDate
                                routeStaffNm = it.routeStaffNm
                                routeDeptNm = it.routeDeptNm
                            }
                            return@map customerRoute
                        }
                    viewModel.addRoutes2(listCustomerRoutes)
                }
            }
        }
    }
}