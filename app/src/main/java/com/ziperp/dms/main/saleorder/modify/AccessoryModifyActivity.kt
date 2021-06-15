package com.ziperp.dms.main.saleorder.modify

import android.app.Activity
import android.view.View
import android.widget.EditText
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
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.ifLet
import com.ziperp.dms.main.saleorder.model.CUDLinesRequest
import com.ziperp.dms.main.saleorder.model.CUDSalesOrderRequest
import com.ziperp.dms.main.saleorder.model.SaleOrderItem
import com.ziperp.dms.main.saleorder.model.StockItemUnit
import com.ziperp.dms.showNotificationBanner
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.activity_accessory_modify.*
import kotlinx.android.synthetic.main.activity_accessory_modify.available_stock
import kotlinx.android.synthetic.main.activity_accessory_modify.btn_delete
import kotlinx.android.synthetic.main.activity_accessory_modify.tv_remark
import kotlinx.android.synthetic.main.activity_accessory_modify.warehouse
import kotlinx.android.synthetic.main.activity_order_item_modify.*
import kotlinx.android.synthetic.main.item_detail.view.*
import kotlinx.android.synthetic.main.item_detail_header.view.*

class AccessoryModifyActivity : BaseCUDActivity<SaleOrderItem>() {
    var salesOrderNo: String = ""
    var parentItem: SaleOrderItem? = null
    var currentAccessoryItem: SaleOrderItem? = null
    var saleOrderRequest: CUDSalesOrderRequest? = null

    private val viewModel by lazy { getViewModel { Injection.provideSaleOrderViewModel() } }

    override fun setLayoutId(): Int = R.layout.activity_accessory_modify

    override fun initView() {
        salesOrderNo = intent.getStringExtra(EXTRA_ORDER_NO) ?: ""
        saleOrderRequest =
            intent.getSerializableExtra(OrderItemModifyActivity.EXTRA_SALE_ORDER_INFO) as CUDSalesOrderRequest?
        item_parent_header.tv_header.text = R.string.str_item_parent.getString()
        item_accessory_header.tv_header.text = R.string.str_item_accessory.getString()
        quantity_wu.tv_title.text = R.string.str_quantity_wu.getString()
        available_stock.tv_title.text = R.string.str_available_stock.getString()

        setOnUpdatedValueListener(accessory_unit, quantity)

        viewModel.cudLinesStatus.observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    showLoading(false)
                    setResult(Activity.RESULT_OK)
                    finish()
                    if (response.data?.data?.get(0)?.status == "OK") {
                        showNotificationBanner(NotificationType.SUCCESS, response.data.data[0].errMsg)
                    } else {
                        showNotificationBanner(NotificationType.ERROR, response.data?.data?.get(0)?.errMsg?: "Something went wrong!")
                    }
                }
                Status.LOADING -> showLoading(true)
                Status.ERROR -> {
                    showLoading(false)
                    showNotificationBanner(NotificationType.ERROR, response.data?.data?.get(0)?.errMsg?: "Something went wrong!")
                }
            }
        })

        viewModel.availableStockLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.quantities?.get(0)?.let { quantity ->
                        available_stock.tv_content.text ="${quantity.availStkQty.format()} ${currentAccessoryItem?.wHUnitNm ?: ""}"
                    }
                }
            }
        })

        updateState(item_parent.content.isNotBlank(), listOf(accessory_version, item_accessory, accessory_unit, quantity, warehouse, tv_remark))
    }

    override fun createNewForm() {
        setToolbar(R.string.str_add_accessory.getString(), true)
        item = SaleOrderItem()
        updateViewsState()
    }

    override fun fillCurrentInfo() {
        item = intent.getSerializableExtra(EXTRA_CUD_OBJECT) as SaleOrderItem
        setToolbar(R.string.str_edit_accessory.getString(), true)
        currentAccessoryItem = item
        item.apply {
            item_parent.visibility = View.GONE
            accessory_version.visibility = View.GONE
            item_parent_header.visibility = View.GONE
            listOption[0].second.controlValue = arrayListOf(KeyValue(valueCode = parentItem, valueName = parentItem))
            listOption[1].second.controlValue = arrayListOf(KeyValue(valueCode = quotationVer, valueName = quotationVer))

            //item_accessory
            item_accessory.setValue("$itemNo - $itemNm")
            listOption[2].second.controlValue = arrayListOf(KeyValue(valueCode = itemCd, valueName = itemNm))
            listOption[2].second.param.item1 = parentItem
            listOption[2].second.param.item2 = "S"
            listOption[2].second.param.item3 = quotationVer

            //accessory unit
            accessory_unit.setValue(uoMNm)
            listOption[3].second.controlValue = arrayListOf(KeyValue(valueCode = uoMCd, valueName = uoMNm))
            listOption[3].second.param.item1 = itemCd

            listOption[4].second.controlValue = arrayListOf(KeyValue(valueCode = whCd, valueName = whNm))
            warehouse.setValue(whNm)

            quantity.setValue(orderQty.format())
            tv_remark.setText(remark)

            //Load data
            updateQuantity()
            checkWareHouse()
        }
        updateViewsState()

        btn_delete.visibility = View.VISIBLE
        btn_delete.setOnClickListener {
            AppUtil.showAlertDialog(this, R.string.str_question_delete.getString(), {
                viewModel.cudSalesOrderLines(Constants.CUD.TYPE_DELETE, CUDLinesRequest(orderNo = salesOrderNo, orderLineNo = item.serl))
            })
        }
    }

    override fun setListMandatoryView(): ArrayList<EditView> {
        return when (mode) {
            CREATE_MODE -> {
                arrayListOf(item_parent, accessory_version, item_accessory, accessory_unit, quantity, warehouse)
            }
            UPDATE_MODE -> {
                arrayListOf(item_accessory, accessory_unit, quantity, warehouse)
            }
            else -> {
                arrayListOf()
            }
        }
    }

    override fun setListOption(): ArrayList<Pair<EditView, ItemControlForm>> = arrayListOf(
        item_parent to ItemControlForm(
            formId = Form.FORM_ID_SALE_ORDER_MODIFY,
            controlId = "txtParentItem",
            controlName = "Item Parent",
            lookUpCode = "ACSM",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.NAME
        ),
        accessory_version to ItemControlForm(
            formId = Form.FORM_ID_SALE_ORDER_MODIFY,
            controlId = "cmbAccVer",
            controlName = "Accessory Version",
            lookUpCode = "0179",
            lookUpType = Form.LookUpType.COMBO,
            findType = Form.FindType.CODE
        ),
        item_accessory to ItemControlForm(
            formId = Form.FORM_ID_SALE_ORDER_MODIFY,
            controlId = "txtComponentNm",
            controlName = "Item Accessory",
            lookUpCode = "ACSI",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.NAME
        ),
        accessory_unit to ItemControlForm(
            formId = Form.FORM_ID_SALE_ORDER_MODIFY,
            controlId = "txtSalesUoM",
            controlName = "Accessory Unit",
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

    override fun onUpdateItemControl(index: Int) {
        when (index) {
            0 -> {
                listOption[0].second.controlValue.apply {
                    if (isNotEmpty()) {
                        parentItem = Gson().fromJson(
                            listOption[0].second.controlValue[0].jsonData,
                            SaleOrderItem::class.java
                        )
                        parentItem?.apply {
                            //update accessory version
                            listOption[1].second.param.apply {
                                item1 = itemCd
                                item2 = "2"
                            }
                            //update accessory item
                            listOption[2].second.param.item1 = itemCd
                            listOption[2].second.param.item2 = "S"
                        }
                    }
                }
            }
            1 -> {
                listOption[1].second.controlValue.apply {
                    if (isNotEmpty()) {
                        listOption[2].second.param.item3 = listOption[1].second.getFilterValue()
                    }
                }
            }

            2 -> {
                listOption[2].second.controlValue.apply {
                    if (isNotEmpty()) {
                        currentAccessoryItem = Gson().fromJson(
                            listOption[2].second.controlValue[0].jsonData,
                            SaleOrderItem::class.java
                        )
                        currentAccessoryItem?.apply {
                            //accessory unit
                            val keyValue = KeyValue(valueCode = uoMCd, valueName = uoMNm)
                            listOption[3].second.controlValue = arrayListOf(keyValue)
                            listOption[3].second.param.item1 = itemCd
                            listOption[3].first.setValue(uoMNm)

                            val keyValueWh = KeyValue(
                                valueCode = whCd,
                                valueName = whNm
                            )
                            listOption[4].second.controlValue = arrayListOf(keyValueWh)
                            listOption[4].first.setValue(whNm)
                            checkWareHouse()
                        }
                    }
                }
            }
            3 -> {
                listOption[3].second.controlValue.apply {
                    if(isNotEmpty()) {
                        Gson().fromJson(get(0).jsonData, StockItemUnit::class.java)?.apply {
                            currentAccessoryItem?.let {
                                it.uoMCd = uoMCd
                                it.uoMNm = uoMNm
                                it.convQty2 = convQty2
                                it.convQty1 = convQty1
                                it.convStkQty = convStkQty
                                updateQuantity()
                            }
                        }
                    }
                }
            }
            4 ->{
                listOption[4].second.controlValue.apply {
                    if(isNotEmpty()) {
                        currentAccessoryItem?.apply {
                            whCd =  listOption[4].second.getFilterValue()
                            whNm = listOption[4].second.getDisplayText()
                        }
                        checkWareHouse()
                    }else{
                        available_stock.tv_content.text = ""
                        listOption[4].second.controlValue.clear()
                        listOption[4].first.setValue("")
                    }
                }
            }
        }

        updateViewsState()
    }

    private fun updateViewsState() {
        updateState(item_parent.content.isNotBlank(), listOf(accessory_version))
        updateState(accessory_version.content.isNotBlank() && accessory_version.isEnable || mode == UPDATE_MODE, listOf(item_accessory))
        updateState(item_accessory.content.isNotBlank() && item_accessory.isEnable, listOf(accessory_unit, quantity, warehouse, tv_remark))
    }

    private fun updateState(enable: Boolean, listViews: List<View>) {
        listViews.forEach { view ->
            if (view is EditView) view.isEnable = enable
            if (view is EditText) view.isEnabled = enable
        }
    }

    override fun updatedValueListener(content: String, editView: EditView) {
        LogUtils.d("Updated value $content")
        when (editView) {
            quantity -> updateQuantity()
        }
    }

    private fun updateQuantity() {
        ifLet(currentAccessoryItem, saleOrderRequest) { item, _ ->
            val wareHouseQuantity = quantity.intValue * item.convQty1 / item.convQty2
            quantity_wu.tv_content.text =
                if (item.wHUnitNm.isNotBlank()) "${wareHouseQuantity.format()}  ${item.wHUnitNm}" else "$wareHouseQuantity"
        }
    }

    private fun checkWareHouse() {
        ifLet(currentAccessoryItem, saleOrderRequest) { item, saleOrder ->
            viewModel.getAvailableStockOfItem(item, saleOrder)
        }
    }

    override fun requestUpdate() {
        val request = CUDLinesRequest()
        request.apply {
            orderNo = salesOrderNo
            orderLineNo = currentAccessoryItem?.serl ?: ""
            priceChk = "A"
            parentItem = listOption[0].second.getFilterValue()
            quotationVer = listOption[1].second.getFilterValue()
            itemCd = listOption[2].second.getFilterValue()
            uoMCd = listOption[3].second.getFilterValue()
            whCd = listOption[4].second.getFilterValue()
            orderQty = quantity.intValue
            remark = tv_remark.text.toString()
        }
        viewModel.cudSalesOrderLines(
            if (mode == CREATE_MODE) Constants.CUD.TYPE_CREATE else Constants.CUD.TYPE_UPDATE,
            request
        )
    }

    companion object {
        const val EXTRA_ORDER_NO = "EXTRA_ORDER_NO"
    }

}