package com.ziperp.dms.main.saleorder.modify

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.ziperp.dms.Injection
import com.ziperp.dms.NotificationType
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.base.BaseCUDActivity.Companion.CREATE_MODE
import com.ziperp.dms.base.BaseCUDActivity.Companion.EXTRA_CUD_MODE
import com.ziperp.dms.base.BaseCUDActivity.Companion.EXTRA_CUD_OBJECT
import com.ziperp.dms.base.BaseCUDActivity.Companion.UPDATE_MODE
import com.ziperp.dms.base.PagerAdapter
import com.ziperp.dms.core.form.dialogpopup.view.DialogPopupActivity
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.form.model.ItemControlSelection
import com.ziperp.dms.core.rest.Constants.CUD.TYPE_CREATE
import com.ziperp.dms.core.rest.Constants.CUD.TYPE_DELETE
import com.ziperp.dms.core.rest.Constants.CUD.TYPE_UPDATE
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.toast
import com.ziperp.dms.main.saleorder.model.CUDSalesOrderRequest
import com.ziperp.dms.main.saleorder.model.SaleOrderDetailResponse
import com.ziperp.dms.showNotificationBanner
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.activity_sales_order_modify.*
import kotlinx.android.synthetic.main.layout_edit_view.view.*

class SalesOrderModifyActivity : BaseActivity() {

    var mode: Int = CREATE_MODE
    var orderNo: String = ""
    var orderLineNo: String = ""
    var customerCode: String = ""
    var customerPhone: String = ""
    var saveAndBack: Boolean = false
    var isDeleteAction = false

    lateinit var item: SaleOrderDetailResponse
    lateinit var pagerAdapter: PagerAdapter
    lateinit var customerItemControl: ItemControlForm
    private val viewModel by lazy { getViewModel { Injection.provideSaleOrderViewModel() } }
    private val commonInfoFragment by lazy { CommonInfoFragment(mode) }
    private val itemFragment by lazy { ItemFragment() }

    override fun setLayoutId(): Int = R.layout.activity_sales_order_modify

    override fun initView() {

        mode = intent.getIntExtra(EXTRA_CUD_MODE, CREATE_MODE)

        customer_name.tv_edit_content.isEnabled = false
        if (mode == CREATE_MODE) {
            setToolbar(R.string.str_new_sales_order.getString(), true)
            customerCode = intent.getStringExtra(EXTRA_CST_CODE) ?: ""
            val customerName = intent.getStringExtra(EXTRA_CST_NAME) ?: ""
            customerPhone = intent.getStringExtra(EXTRA_CST_PHONE) ?: ""

            if (customerCode.isNotEmpty()) { //Add new Sales Order for a customer
                customer_name.setValue(customerName)
            } else { // Create a new sales order without any customer selected
                customer_name.img_option.setImageResource(R.drawable.ic_search_black)
                customer_name.layout_content.setOnClickListener { chooseCustomer() }
            }
        } else { // UPDATE_MODE
            item = intent.getSerializableExtra(EXTRA_CUD_OBJECT) as SaleOrderDetailResponse
            item.commonInfo[0].also {info ->
                customerCode = info.cstCd
                orderNo = info.orderNo
                customer_name.setValue(info.cstNm)
                setToolbar(info.orderMngtNo, true)
                tv_total_amount.text = "${info.totalAmount.format()} ${info.currencyCd}"
            }
        }

        initPager()

        viewModel.cudSaleOrderLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> if (saveAndBack) showLoading(true)
                Status.SUCCESS -> {
                    showLoading(false)
                    it.data?.let { cudData ->
                        if(cudData.isSuccess()){
                            orderNo = cudData.getOrderNo()
                            showNotificationBanner(NotificationType.SUCCESS, cudData.message())
                            if (saveAndBack) {
                                if (isDeleteAction) setResult(102) else setResult(Activity.RESULT_OK)
                                finish()
                            }
                        }else{
                            showNotificationBanner(NotificationType.ERROR, cudData.message())
                        }
                    }
                }
                Status.ERROR -> if (saveAndBack) "Something wrong happened!".toast(this)
            }
        })

        viewModel.saleOrderInfoLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        //update data
                        response.commonInfo[0].apply {
                            tv_total_amount.text = "${totalAmount.format()} $currencyCd"
                        }
                        itemFragment.updateItems(response.orderItems)
                    }
                }
                Status.LOADING -> loading_progressbar.visibility = View.VISIBLE
                Status.ERROR -> loading_progressbar.visibility = View.GONE
            }
        })
    }

    private fun initPager() {
        if (mode == UPDATE_MODE) {
            commonInfoFragment.item = item.commonInfo[0]
            itemFragment.items = item.orderItems
            itemFragment.orderNo = orderNo
        }else{
            commonInfoFragment.customerPhone = customerPhone
        }

        pagerAdapter = PagerAdapter(supportFragmentManager, mode, listOf(commonInfoFragment, itemFragment))
        pagerAdapter.listTitles = listOf(R.string.str_common_info.getString(), R.string.str_items.getString())

        viewpager.adapter = pagerAdapter
        viewpager.offscreenPageLimit = 2
        tab.setupWithViewPager(viewpager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        menu?.findItem(R.id.action_save)?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                customer_name.isNeedToCheckValid = true
                if (customer_name.checkValid()) {
                    requestCUD(true)
                }
            }
        }
        return true
    }

    fun deleteSalesOrder() {
        this.saveAndBack = true
        isDeleteAction = true
        val request = getCUDInfo()
        viewModel.cudSalesOrder(TYPE_DELETE, request)
    }

    private fun requestCUD(saveAndBack: Boolean) {
        this.saveAndBack = saveAndBack
        if (commonInfoFragment.checkMandatoryField()) {
            val request = getCUDInfo()
            val cstVisitNo = intent.getStringExtra(EXTRA_VISIT_CST_NO)?: ""
            if (cstVisitNo.isNotEmpty()) {
                request.docNo = cstVisitNo
                request.srcType = "V"
            }
            var typeRequest = ""
            if (orderNo.isEmpty()) {
                typeRequest = TYPE_CREATE
            } else {
                typeRequest = TYPE_UPDATE
                request.orderNo = orderNo
            }
            viewModel.cudSalesOrder(typeRequest, request)
        }
    }

    fun createNewSaleOrderIfNeed(): Boolean {
        if (commonInfoFragment.checkMandatoryField()) {
            if (orderNo.isEmpty()) {
                requestCUD(false)
            }
            return true
        }
        return false
    }

    private fun chooseCustomer() {
        customerItemControl = ItemControlForm(
            formId = Form.FORM_ID_SALE_ORDER_MODIFY,
            controlId = "txtCustomer",
            controlName = "Customer Name",
            lookUpCode = "CSTM",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.NAME
        )
        val intent = Intent(this, DialogPopupActivity::class.java)
        intent.putExtra("itemControl", customerItemControl)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode){
                100 ->{
                    data?.let {
                        val result: ItemControlSelection =
                            it.getSerializableExtra("result") as ItemControlSelection
                        customerItemControl.controlValue = result.keyValues
                        customer_name.setValue(customerItemControl.getDisplayText())
                        customerCode = customerItemControl.getFilterValue()
                        if (customerItemControl.controlValue.isNotEmpty()) {
                            val jsonData = customerItemControl.controlValue[0].jsonData
                            val jsonObject = JsonParser().parse(jsonData) as? JsonObject
                            if (jsonObject != null) {
                                customerPhone =  jsonObject.get("Phone").asString
                                commonInfoFragment.updatePhone(customerPhone)
                            }
                        }
                        commonInfoFragment.updatePriceListNo()
                    }
                }
                101 -> {// items, accessory
                    viewModel.getSaleOrderInfo(orderNo)
                }
            }

        }
    }

    fun getCUDInfo(): CUDSalesOrderRequest {
        return commonInfoFragment.getCUDInfo().apply {
            cstCd = customerCode
        }
    }

    override fun onBackPressed() {
        AppUtil.showAlertDialog(this, "Do you want to discard this form?", {
            setResult(Activity.RESULT_OK)
            finish()

        }, {LogUtils.d("Cancel")})
    }

    companion object {
        const val EXTRA_CST_NAME = "EXTRA_CST_NAME"
        const val EXTRA_CST_CODE = "EXTRA_CST_CODE"
        const val EXTRA_CST_PHONE = "EXTRA_CST_PHONE"
        const val EXTRA_VISIT_CST_NO = "EXTRA_VISIT_CST_NO"

        fun newInstance(
            cstNm: String = "",
            cstCd: String = "",
            cstPhone: String = "",
            mode: Int,
            context: Context
        ): Intent {
            val intent = Intent(context, SalesOrderModifyActivity::class.java)
            intent.putExtra(EXTRA_CUD_MODE, mode)
            intent.putExtra(EXTRA_CST_NAME, cstNm)
            intent.putExtra(EXTRA_CST_CODE, cstCd)
            intent.putExtra(EXTRA_CST_PHONE, cstPhone)
            return intent
        }
    }
}