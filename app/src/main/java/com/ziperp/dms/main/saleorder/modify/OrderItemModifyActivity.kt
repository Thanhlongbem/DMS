package com.ziperp.dms.main.saleorder.modify

import android.app.Activity
import android.view.View
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.ziperp.dms.Injection
import com.ziperp.dms.NotificationType
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseCUDActivity
import com.ziperp.dms.common.model.EditView
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.form.model.KeyValue
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.ifLet
import com.ziperp.dms.main.saleorder.model.*
import com.ziperp.dms.showNotificationBanner
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.activity_order_item_modify.*
import kotlinx.android.synthetic.main.activity_order_item_modify.btn_delete
import kotlinx.android.synthetic.main.activity_order_item_modify.rb_group
import kotlinx.android.synthetic.main.fragment_common_info_modify.*
import kotlinx.android.synthetic.main.item_detail.view.*


class OrderItemModifyActivity: BaseCUDActivity<SaleOrderItem>() {
    var salesOrderNo: String = ""
    var currentItem: SaleOrderItem? = null
    var saleOrderRequest: CUDSalesOrderRequest? = null
    var unitPriceItem: UnitPriceResponse.UnitPrice? = null
    private val viewModel by lazy { getViewModel { Injection.provideSaleOrderViewModel() } }

    override fun setLayoutId(): Int = R.layout.activity_order_item_modify

    override fun initView() {

        salesOrderNo = intent.getStringExtra(EXTRA_ORDER_NO) ?: ""
        saleOrderRequest = intent.getSerializableExtra(EXTRA_SALE_ORDER_INFO) as CUDSalesOrderRequest?
        warehouse_unit.tv_title.text = R.string.str_quantity_wu.getString()
        available_stock.tv_title.text = R.string.str_available_stock.getString()

        setOnUpdatedValueListener(
            sale_unit,
            sale_quantity,
            unit_price,
            discount_rate,
            discount_amount
        )

        viewModel.cudLinesStatus.observe(this, Observer {response ->
            when (response.status) {
                Status.SUCCESS -> {
                    showLoading(false)
                    setResult(Activity.RESULT_OK)
                    finish()
                    response.data?.let {
                        if(it.isSuccess()){
                            showNotificationBanner(NotificationType.SUCCESS, it.message())
                        }else{
                            showNotificationBanner(NotificationType.ERROR, it.message())
                        }
                    }

                }
                Status.LOADING -> showLoading(true)
                Status.ERROR -> {
                    showLoading(false)
                    showNotificationBanner(NotificationType.ERROR, response.data?.data?.get(0)?.errMsg?: "Something went wrong!")
                }
            }
        })

        viewModel.itemPriceLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.unitPrices?.get(0)?.let { unitPrice ->
                        this.unitPriceItem = unitPrice
                        if(DmsUserManager.userInfo.salesPriceRule != 3){
                            unit_price.setValue(unitPrice.price.toString())
                        }else{
                            // Do nothing
                        }
                    }
                }
            }
        })

        viewModel.availableStockLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.quantities?.get(0)?.let { quantity ->
                        available_stock.tv_content.text = "${quantity.availStkQty.format()} ${currentItem?.wHUnitNm ?: ""}"
                    }
                }
            }
        })

        val stateChanged: (Boolean) -> Unit = { isDiscountRate ->
            discount_rate.isEnable = isDiscountRate
            discount_amount.isEnable = !isDiscountRate
        }
        rb_group.setOnCheckedChangeListener { radioGroup: RadioGroup, radioId: Int ->
            stateChanged(radioId == R.id.rb_discount_rate)
        }
        // default discount state
        stateChanged(true)

    }

    override fun createNewForm() {
        setToolbar(R.string.str_create_sale_item.getString(), true)

        item = SaleOrderItem()

        updateState()
        //Fill purchase list
        fillPurchaseListInfo()
    }

    override fun fillCurrentInfo() {
        item = intent.getSerializableExtra(EXTRA_CUD_OBJECT) as SaleOrderItem
        setToolbar(R.string.str_edit_sale_item.getString(), true)
        currentItem = item
        item.apply {
            //update discount first
            if (discType == "0") rb_group.check(R.id.rb_discount_rate) else rb_group.check(R.id.rb_discount_amount)
            discount_rate.setValue(discount.format(), false)
            discount_amount.setValue(discountAmt.format(), false)

            item_sales.setValue("$itemNo - $itemNm")
            listOption[0].second.controlValue = arrayListOf(
                KeyValue(
                    valueCode = itemCd,
                    valueName = itemNm
                )
            )

            sale_unit.setValue(uoMNm, false)
            listOption[1].second.controlValue = arrayListOf(
                KeyValue(
                    valueCode = uoMCd,
                    valueName = uoMNm
                )
            )
            listOption[1].second.param.item1 = itemCd

            sale_quantity.setValue(orderQty.format())
            warehouse_unit.tv_content.text = wHUnitNm
            warehouse.setValue(whNm)
            listOption[2].second.controlValue = arrayListOf(
                KeyValue(
                    valueCode = whCd,
                    valueName = whNm
                )
            )
            available_stock.tv_content.text = availStk.format()

            unit_price.setValue(unitPrice.format())
            tv_remark.setText(remark)

            //Load data if need
            updateQuantity()
            checkWareHouse()
        }

        updateState()

        btn_delete.visibility = View.VISIBLE
        btn_delete.setOnClickListener {
            AppUtil.showAlertDialog(this, R.string.str_question_delete.getString(), {
                viewModel.cudSalesOrderLines(Constants.CUD.TYPE_DELETE, CUDLinesRequest(orderNo = salesOrderNo, orderLineNo = item.serl))
            })
        }
        //Fill purchase list
        fillPurchaseListInfo()
    }

    fun fillPurchaseListInfo(){
        when (DmsUserManager.userInfo.salesPriceRule) {
            0, 3 -> {
             // Do nothing
            }
            1, 2 -> {
                saleOrderRequest?.apply {
                    if(poNo.isNotBlank()){
                        listOption[0].second.param.apply {
                            item5 = "1"
                            item9 = poNo
                        }
                    }
                }
            }
        }

    }

    private fun updateState(){
        setEnableViews(item_sales.content.isNotBlank())
    }

    private fun setEnableViews(enable: Boolean) {
        sale_unit.isEnable = enable
        sale_quantity.isEnable = enable
        warehouse.isEnable = enable
        unit_price.isEnable = enable
        discount_rate.isEnable = enable
        discount_amount.isEnable = enable
        total_amount.isEnable = enable
        tv_remark.isEnabled = enable
    }

    override fun setListMandatoryView(): ArrayList<EditView> = arrayListOf(
        item_sales,
        sale_unit,
        sale_quantity,
        warehouse,
        unit_price
    )

    override fun setListOption(): ArrayList<Pair<EditView, ItemControlForm>> = arrayListOf(
        item_sales to ItemControlForm(
            formId = Form.FORM_ID_SALE_ORDER_MODIFY,
            controlId = "txtItemNm",
            controlName = "Item Sales",
            lookUpCode = "ITNM",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.NAME
        ),
        sale_unit to ItemControlForm(
            formId = Form.FORM_ID_SALE_ORDER_MODIFY,
            controlId = "txtSalesUoM",
            controlName = "Item Unit",
            lookUpCode = "0092",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.CODE
        ),
        warehouse to ItemControlForm(
            formId = Form.FORM_ID_SALE_ORDER_MODIFY,
            controlId = "cmbWareHouse",
            controlName = "Warehouse",
            lookUpCode = "0082",
            lookUpType = Form.LookUpType.COMBO,
            findType = Form.FindType.CODE
        )
    )

    override fun onUpdateItemControl(index: Int){
        if(index == 0){//item_sales
            listOption[0].second.controlValue.apply {
                if(isNotEmpty()) {
                    currentItem = Gson().fromJson(
                        listOption[0].second.controlValue[0].jsonData,
                        SaleOrderItem::class.java
                    )
                    currentItem?.apply {
                        val keyValue = KeyValue(
                            keyCode = "UoMCd",
                            valueCode = uoMCd,
                            keyName = "UoMNm",
                            valueName = uoMNm
                        )
                        listOption[1].second.controlValue = arrayListOf(keyValue)
                        listOption[1].second.param.item1 = itemCd
                        listOption[1].first.setValue(uoMNm)
                        checkItemPrice()

                        val keyValueWh = KeyValue(
                            valueCode = whCd,
                            valueName = whNm
                        )
                        listOption[2].second.controlValue = arrayListOf(keyValueWh)
                        listOption[2].first.setValue(whNm)
                        checkWareHouse()
                    }
                } else {
                    listOption[1].second.controlValue.clear()
                    listOption[1].first.setValue("")
                }
            }
            updateState()

        }else if(index == 1){
            listOption[1].second.controlValue.apply {
                if(isNotEmpty()) {
                    Gson().fromJson(get(0).jsonData, StockItemUnit::class.java)?.apply {
                        currentItem?.let {
                            it.uoMCd = uoMCd
                            it.uoMNm = uoMNm
                            it.convQty2 = convQty2
                            it.convQty1 = convQty1
                            it.convStkQty = convStkQty
                            updateQuantity()
                        }
                    }
                }else {
                    listOption[1].second.controlValue.clear()
                    listOption[1].first.setValue("")
                }
            }
        }else if(index == 2){
            listOption[2].second.controlValue.apply {
                if(isNotEmpty()) {
                    currentItem?.apply {
                        whCd =  listOption[2].second.getFilterValue()
                        whNm = listOption[2].second.getDisplayText()
                    }
                    checkWareHouse()
                }else{
                    available_stock.tv_content.text = ""
                    listOption[2].second.controlValue.clear()
                    listOption[2].first.setValue("")
                }
            }
        }
    }

    override fun updatedValueListener(content: String, editView: EditView) {
        LogUtils.d("Updated value $content")
        when (editView) {
            sale_quantity -> updateQuantity()
            unit_price -> updatePrice()
            discount_rate -> updatePrice()
            discount_amount -> updatePrice()
        }
    }

    private fun checkItemPrice() {
        ifLet(currentItem, saleOrderRequest){ item, saleOrder ->
            val uoMcd = listOption[1].second.getFilterValue()
            if(uoMcd.isNotBlank()){
                viewModel.getUnitPriceOfItem(item, saleOrder, uoMcd, "1")
            }
        }
    }

    private fun updateQuantity() {
        ifLet(currentItem, saleOrderRequest){ item, _ ->
            val wareHouseQuantity = sale_quantity.intValue * item.convQty1/ item.convQty2
            warehouse_unit.tv_content.text = if(item.wHUnitNm.isNotBlank()) "${wareHouseQuantity.format()}  ${item.wHUnitNm}" else "$wareHouseQuantity"
            updatePrice()
        }
    }

    private fun updatePrice() {
        currentItem?.let { item ->
            if(unit_price.doubleValue > 0){
                if(sale_quantity.intValue > 0){
                    val extendedPrice = unit_price.doubleValue * sale_quantity.intValue
                    val discountRate = discount_rate.doubleValue

                    var discountAmount = if (rb_group.checkedRadioButtonId == R.id.rb_discount_rate){
                        val value = extendedPrice*discountRate/100.0
                        discount_amount.setValue(value.format(), true)
                        value
                    }else{
                        val value =  discount_amount.doubleValue
                        if(value > 0){
                            val rate = (value * 100)/ extendedPrice
                            discount_rate.setValue(rate.format(), true)
                        }
                        value
                    }
                    val subTotalAmount = extendedPrice - discountAmount
                    val totalAmount = if(item.sellVATinPriceYn == 0){
                        subTotalAmount*(1 + item.vatRate/ 100.0)
                    }else{
                        subTotalAmount
                    }
                    total_amount.setValue(totalAmount.format())
                } else{
                    total_amount.setValue(0.0.format())
                }
            }
        }

    }

    private fun checkWareHouse() {
        ifLet(currentItem, saleOrderRequest){ item, saleOrder ->
            viewModel.getAvailableStockOfItem(item, saleOrder)
        }
    }

    override fun requestUpdate() {
        val request = CUDLinesRequest()
        request.apply {
            orderNo = salesOrderNo
            orderLineNo = if (mode == UPDATE_MODE) item.serl else ""
            itemCd = listOption[0].second.getFilterValue()
            uoMCd = listOption[1].second.getFilterValue()
            whCd = listOption[2].second.getFilterValue()
            orderQty = sale_quantity.intValue
            unitPrice = unit_price.doubleValue
            discType = if (rb_group.checkedRadioButtonId == R.id.rb_discount_rate) 0 else 1
            discount = discount_rate.intValue
            discountAmt = discount_amount.intValue
            priceListCd = unitPriceItem?.priceListCd ?: ""
            priceListLine = unitPriceItem?.priceListLine ?: ""
            priceChk = "G"
            remark = tv_remark.text.toString()
        }
        viewModel.cudSalesOrderLines(
            if (mode == CREATE_MODE) Constants.CUD.TYPE_CREATE else Constants.CUD.TYPE_UPDATE,
            request
        )
    }



    companion object{
        const val EXTRA_ORDER_NO = "EXTRA_ORDER_NO"
        const val EXTRA_SALE_ORDER_INFO = "EXTRA_SALE_ORDER_INFO"
    }

}