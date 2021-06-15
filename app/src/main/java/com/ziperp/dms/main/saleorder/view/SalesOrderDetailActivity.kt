package com.ziperp.dms.main.saleorder.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.ziperp.dms.Injection
import com.ziperp.dms.NotificationType
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.base.BaseCUDActivity.Companion.EXTRA_CUD_MODE
import com.ziperp.dms.base.BaseCUDActivity.Companion.EXTRA_CUD_OBJECT
import com.ziperp.dms.base.BaseCUDActivity.Companion.UPDATE_MODE
import com.ziperp.dms.core.rest.Constants.EXTRA_SALE_ORDER_DETAIL
import com.ziperp.dms.core.rest.Constants.EXTRA_SALE_ORDER_VISIT
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.*
import com.ziperp.dms.main.saleorder.adapter.PagerAdapter
import com.ziperp.dms.main.saleorder.model.ApplyPromotionRequest
import com.ziperp.dms.main.saleorder.model.ConfirmSaleOderRequest
import com.ziperp.dms.main.saleorder.model.SaleOrderDetailResponse
import com.ziperp.dms.main.saleorder.model.SalesOrderResponse
import com.ziperp.dms.main.saleorder.modify.SalesOrderModifyActivity
import com.ziperp.dms.main.visitcustomer.model.SaleOrderVisit
import com.ziperp.dms.showNotificationBanner
import com.ziperp.dms.utils.DataConvertUtils
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.activity_sales_order_detail.*


class SalesOrderDetailActivity : BaseActivity() {
    private var saleOrder: SalesOrderResponse.SaleOrder? = null
    private var saleOrderVisit: SaleOrderVisit? = null
    private var saleOrderNo: String = ""

    private var salesOrderDetail: SaleOrderDetailResponse? = null
    private lateinit var commonInfoFragment: CommonInfoFragment
    private lateinit var orderItemsFragment: OrderItemsFragment
    private lateinit var giftsFragment: GiftsFragment
    private lateinit var pagerAdapter: PagerAdapter

    private var saleOrderCommonInfo: SaleOrderDetailResponse.SaleOrderCommonInfo? = null
        get() = salesOrderDetail?.commonInfo?.let {
            if(it.isNotEmpty()) it[0] else null
        }

    companion object {
        fun newInstance(saleOrderData: SalesOrderResponse.SaleOrder, context: Context?): Intent? {
            val intent = Intent(context, SalesOrderDetailActivity::class.java)
            intent.putExtra(EXTRA_SALE_ORDER_DETAIL, saleOrderData)
            return intent
        }

        fun newInstance(saleOrder: SaleOrderVisit, context: Context?): Intent? {
            val intent = Intent(context, SalesOrderDetailActivity::class.java)
            intent.putExtra(EXTRA_SALE_ORDER_VISIT, saleOrder)
            return intent
        }
    }

    private val viewModel by lazy {
        getViewModel { Injection.provideSaleOrderViewModel() }
    }

    override fun setLayoutId(): Int = R.layout.activity_sales_order_detail

    override fun initView() {
        setOnRegisterPhoneContextMenu(tv_phoneNoOfSaleOrderDetail)

        saleOrder = intent.getSerializableExtra(EXTRA_SALE_ORDER_DETAIL) as SalesOrderResponse.SaleOrder?
        saleOrderVisit = intent.getSerializableExtra(EXTRA_SALE_ORDER_VISIT) as SaleOrderVisit?

        saleOrder?.apply {
            setToolbar(DataConvertUtils.convertTitle(orderNo), true)
            tv_nameSaleOrderDetail.text = txtCustNm
            tv_salesmanName.text = "$txtSalesman - $txtBizUnit"
            tv_lastPriceValue.text = "${fltTotalAmt.format()} $txtCurrency"
            tv_date.text = orderDate.reformatDate()
            tv_phoneNoOfSaleOrderDetail.text = if(txtCstPhone.isNullOrEmpty()) "N/A" else txtCstPhone

            val orderNo: String = orderNo.split("/").last()

            if(orderStatus != 1){
                img_confirm.setImageResource(R.drawable.icon_confirm)
                view_confirm.backgroundTintList =  ColorStateList.valueOf(R.color.color_confirm.getColor())
                tv_confirm.text = "Confirm Order"
            } else {
                img_confirm.setImageResource(R.drawable.icon_x)
                view_confirm.backgroundTintList =  ColorStateList.valueOf(R.color.color_unconfirm.getColor())
                tv_confirm.text = "Unconfirm"
            }

            viewModel.getSaleOrderInfo(orderNo)
            saleOrderNo = orderNo
        }

        saleOrderVisit?.apply {
            setToolbar(orderMngtNo, true)
            tv_nameSaleOrderDetail.text = txtCustNm
            tv_lastPriceValue.text = "${fltTotalAmt.format()}"
            tv_date.text = orderDate.reformatDate()
            tv_phoneNoOfSaleOrderDetail.text = ""

            if(orderStatus != 1){
                img_confirm.setImageResource(R.drawable.icon_confirm)
                view_confirm.backgroundTintList =  ColorStateList.valueOf(R.color.color_confirm.getColor())
                tv_confirm.text = "Confirm Order"
            } else {
                img_confirm.setImageResource(R.drawable.icon_x)
                view_confirm.backgroundTintList =  ColorStateList.valueOf(R.color.color_unconfirm.getColor())
                tv_confirm.text = "Unconfirm"
            }

            viewModel.getSaleOrderInfo(orderNo)
            saleOrderNo = orderNo
        }

        view_confirm.setOnClickNetworkListener { confirmSaleOrder() }
        view_promotion.setOnClickNetworkListener { applyPromotion() }

        viewModel.saleOrderInfoLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        if(salesOrderDetail == null){
                            salesOrderDetail = response
                            initPager(response)
                        } else {
                            salesOrderDetail = response
                            updatePager(response)
                        }
                        updateUI()
                    }
                }
                Status.LOADING -> loading_progressbar.visibility = View.VISIBLE
                Status.ERROR -> loading_progressbar.visibility = View.GONE

            }
        })

        viewModel.confirmSaleOrderLiveData.observe(this, Observer {
            LogUtils.d("it.status ${it.status}")
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    ifLet(it.data?.confirms?.get(0), saleOrderCommonInfo){response, commonInfo ->

                        if(response.status == "OK"){
                            commonInfo.confirm = if(commonInfo.confirm == 0) 1 else 0
                            showNotificationBanner(NotificationType.SUCCESS, response.errMsg)
                            updateUI()
                        } else {
                            showNotificationBanner(NotificationType.ERROR, response.errMsg)
                        }
                    }
                }
                Status.LOADING -> loading_progressbar.visibility = View.VISIBLE
                Status.ERROR -> loading_progressbar.visibility = View.GONE

            }
        })

        viewModel.applyPromotionLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    ifLet(it.data?.promotion?.get(0), saleOrderCommonInfo){response, commonInfo ->
                        if(response.status == "OK"){
                            commonInfo.chkApplyPromotionYn = if(commonInfo.chkApplyPromotionYn == 0) 1 else 0
                            showNotificationBanner(NotificationType.SUCCESS, response.errMsg)
                            updateUI()
                        } else {
                            showNotificationBanner(NotificationType.ERROR, response.errMsg)
                        }
                    }
                }
                Status.LOADING -> loading_progressbar.visibility = View.VISIBLE
                Status.ERROR -> {
                    loading_progressbar.visibility = View.GONE
                    showNotificationBanner(NotificationType.ERROR, "Something went wrong")
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        menu?.findItem(R.id.action_edit)?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {
                if (salesOrderDetail != null) {
                    val intent = Intent(this, SalesOrderModifyActivity::class.java)
                    intent.putExtra(EXTRA_CUD_MODE, UPDATE_MODE)
                    intent.putExtra(EXTRA_CUD_OBJECT, salesOrderDetail)
                    startActivityForResult(intent, 100)
                }
            }
        }
        return true
    }

    private fun updateUI() {
        saleOrderCommonInfo?.apply {
            tv_lastPriceValue.text = "${totalAmount.format()} $currencyCd"
            if(chkApplyPromotionYn == 0){
                img_promotion.setImageResource(R.drawable.icon_apply_promo)
                tv_promotion.text = "Apply Promotion"
            } else {
                img_promotion.setImageResource(R.drawable.icon_unapply_promo)
                tv_promotion.text = "Unapply Promotion"
            }

            if(confirm == 0){
                img_confirm.setImageResource(R.drawable.icon_confirm)//FC4D50
                view_confirm.backgroundTintList =  ColorStateList.valueOf(R.color.color_confirm.getColor())
                tv_confirm.text = "Confirm Order"
            }else{
                img_confirm.setImageResource(R.drawable.icon_x)
                view_confirm.backgroundTintList =  ColorStateList.valueOf(R.color.color_unconfirm.getColor())
                tv_confirm.text = "Unconfirm"
            }

            tv_salesmanName.text = "$staffNm - $masterLocNm"
            tv_lastPriceValue.text = "${totalAmount.format()} $currencyCd"
            tv_date.text = orderDate.reformatDate()
            tv_phoneNoOfSaleOrderDetail.text = if(cstPhone.isNullOrEmpty()) "N/A" else cstPhone
        }
    }

    private fun applyPromotion() {
        saleOrderCommonInfo?.apply {
            val orderNoValue: String = orderNo.split("/").last()
            val request = ApplyPromotionRequest(
                orderNo = orderNoValue,
                applyYn = if (chkApplyPromotionYn == 0) 1 else 0
            )
            viewModel.applyPromotion(request)
        } ?:run {}

    }

    private fun confirmSaleOrder() {
        saleOrderCommonInfo?.apply  {
            val orderNoValue: String = orderNo.split("/").last()
            val checkVal = if(confirm == 0) 1 else 0
            val request = ConfirmSaleOderRequest(
                gridKey = orderNoValue,
                controlId = "btnCfmSO",
                checkVal = checkVal
            )
            viewModel.confirmSalesOrder(request)
        }
    }

    private fun updatePager(response: SaleOrderDetailResponse) {
        commonInfoFragment.update(response)
        orderItemsFragment.update(response)
        giftsFragment.update(response)
        pagerAdapter.listTitles = listOf(
            R.string.str_common_info.getString(),
            R.string.str_order_items.getString() + "(" + response.orderItems.size + ")",
            R.string.str_gifts.getString() + "(" + response.orderItems.size + ")"
        )
        pagerAdapter.notifyDataSetChanged()
    }

    private fun initPager(saleOrderDetail: SaleOrderDetailResponse) {
        commonInfoFragment = CommonInfoFragment(saleOrderDetail)
        orderItemsFragment = OrderItemsFragment(saleOrderDetail)
        giftsFragment = GiftsFragment(saleOrderDetail)
        pagerAdapter = PagerAdapter(
            supportFragmentManager,
            listFragments = listOf(commonInfoFragment, orderItemsFragment, giftsFragment)
        )
        pagerAdapter.listTitles = listOf(
            R.string.str_common_info.getString(),
            R.string.str_order_items.getString() + "(" + saleOrderDetail.orderItems.size + ")",
            R.string.str_gifts.getString() + "(" + saleOrderDetail.orderItems.size + ")"
        )
        viewpager.adapter = pagerAdapter
        viewpager.offscreenPageLimit = 3
        tab.setupWithViewPager(viewpager)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 ){
            if (resultCode == Activity.RESULT_OK) {
                viewModel.getSaleOrderInfo(saleOrderNo)
            }
            if (resultCode == 102) { //Deleted
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}