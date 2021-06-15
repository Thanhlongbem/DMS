package com.ziperp.dms.main.visitcustomer.view

import android.view.View
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
import com.ziperp.dms.core.rest.Constants.CUD.TYPE_DELETE
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.*
import com.ziperp.dms.main.saleorder.model.StockItemUnit
import com.ziperp.dms.main.visitcustomer.model.StockCountListResponse
import com.ziperp.dms.main.visitcustomer.model.StockCountRequest
import com.ziperp.dms.main.visitcustomer.model.StockItem
import com.ziperp.dms.showNotificationBanner
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.activity_stock_count_modify.*
import kotlinx.android.synthetic.main.item_detail.view.*
import java.util.*

class StockCountModifyActivity : BaseCUDActivity<StockCountListResponse.StockCount>() {

    var itemCount: StockItem? = null

    override fun setLayoutId(): Int = R.layout.activity_stock_count_modify

    private val viewModel by lazy { getViewModel { Injection.provideStockCountViewModel() } }

    override fun initView() {
        warehouse_unit.tv_title.text = R.string.str_quantity_wu.getString()
        viewModel.cudRequestStatus.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {showLoading(true)}
                Status.SUCCESS -> {
                    showLoading(false)
                    response.data?.data?.get(0)?.apply {
                        if (status == "OK") {
                            showNotificationBanner(NotificationType.SUCCESS, errMsg)
                        } else {
                            showNotificationBanner(NotificationType.ERROR, errMsg)
                        }
                    }
                    setResult(RESULT_OK)
                    finish()
                }
                Status.ERROR -> {
                    showLoading(false)
                    showNotificationBanner(NotificationType.ERROR, "Something went wrong!")
                }
            }
        })
        setOnUpdatedValueListener(count_quantity)
        make_date.setShowDatePicker()
        expiry.setShowDatePicker()

    }

    override fun createNewForm() {
        setToolbar(R.string.str_item_stock_count.getString(), true)
        item = StockCountListResponse.StockCount()
        item.cstVisitNo = intent.getStringExtra(EXTRA_CST_VISIT_NO)
        updateState(false)
    }

    override fun fillCurrentInfo() {
        setToolbar(R.string.str_update_stock_count.getString(), true)
        itemCount = item.toStockItem()
        item.apply {
            item_no.setValue("$itemNo - $itemNm")
            listOption[0].second.controlValue =
                arrayListOf(KeyValue(valueCode = itemCd, valueName = itemNm))
            count_unit.setValue(cntUoMNm)
            listOption[1].second.controlValue =
                arrayListOf(KeyValue(valueCode = cntUoMCd, valueName = cntUoMNm))
            listOption[1].second.param.item1 = itemCd

            count_quantity.setValue(cntQty.format())
            lot_serial_no.setValue(lotNo)
            make_date.setValue(mfgDate.reformatDate())
            expiry.setValue(expiryDate.reformatDate())
            tv_description.setText(remark)

            //Load data if need
            updateQuantity()
        }

        updateState(item_no.content.isNotBlank())

        btn_delete.visibility = View.VISIBLE
        btn_delete.setOnClickListener {
            AppUtil.showAlertDialog(this, R.string.str_question_delete.getString(), {
                viewModel.cudItemCount(TYPE_DELETE, StockCountRequest(cstVisitNo = item.cstVisitNo, itemCd = item.itemCd, cstVisitLineNo = item.cstVisitLineNo))
            })
        }
    }

    override fun setListMandatoryView(): ArrayList<EditView> = arrayListOf(item_no, count_unit, count_quantity)

    override fun setListOption(): ArrayList<Pair<EditView, ItemControlForm>> = arrayListOf(
        item_no to ItemControlForm(
            formId = Form.FORM_ID_STOCK_COUNT,
            controlId = "txtItemNo",
            controlName = "Item No",
            lookUpCode = "ITNO",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.NAME
        ),
        count_unit to ItemControlForm(
            formId = Form.FORM_ID_STOCK_COUNT,
            controlId = "txtUoMNm",
            controlName = "Count Unit",
            lookUpCode = "0092",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.CODE
        )
    )

    override fun requestUpdate() {
        val request = StockCountRequest()
        itemCount?.apply {
            //calculate quantity
            if(convQty1 <= 0) convQty1 = 1.0
            if(convQty2 <= 0) convQty2 = 1.0
            val number = count_quantity.doubleValue ?: 0.0
            val quantity  = (number * convQty1/ convQty2)
            request.apply {
                cstVisitNo = item.cstVisitNo
                cstVisitLineNo = item.cstVisitLineNo
                itemCd = listOption[0].second.getFilterValue()
                cntUoMCd = listOption[1].second.getFilterValue()
                cntQty = quantity
                lotSerialNo = lot_serial_no.content
                mfgDate = make_date.content.convertDate()
                expiryDate = expiry.content.convertDate()
                remark = tv_description.text.toString()
            }

            viewModel.cudItemCount(if (mode == CREATE_MODE) Constants.CUD.TYPE_CREATE else Constants.CUD.TYPE_UPDATE, request)
        }

    }

    override fun onUpdateItemControl(index: Int){
        if(index == 0){// First Control
            listOption[0].second.controlValue.apply {
                if (isNotEmpty()) {
                    itemCount = Gson().fromJson(listOption[0].second.controlValue[0].jsonData, StockItem::class.java)
                    itemCount?.apply {
                        val keyValue = KeyValue(
                            valueCode = uoMCd,
                            valueName = uoMNm
                        )
                        listOption[1].second.controlValue = arrayListOf(keyValue)
                        listOption[1].second.param.item1 = itemCd
                        listOption[1].first.setValue(uoMNm)

                        if(lotNoCtrlYn == 1 || serialNoCtrlYn == 1){
                            listMandatoryView.add(lot_serial_no)
                        } else {
                            listMandatoryView.remove(lot_serial_no)
                        }
                    }
                } else {
                    listOption[1].second.controlValue.clear()
                    listOption[1].first.setValue("")
                    listMandatoryView.remove(lot_serial_no)
                }
            }
            updateState(item_no.content.isNotBlank())
        } else if(index == 1){
            listOption[1].second.controlValue.apply {
                if(isNotEmpty()) {
                    Gson().fromJson(get(0).jsonData, StockItemUnit::class.java)?.apply {
                        itemCount?.let {
                            it.uoMCd = uoMCd
                            it.uoMNm = uoMNm
                            it.convQty2 = convQty2
                            it.convQty1 = convQty1

                            if(it.convQty1 <= 0) it.convQty1 = 1.0
                            if(it.convQty2 <= 0) it.convQty2 = 1.0
                            it.convStkQty = convStkQty
                            updateQuantity()
                        }
                    }
                }
            }
        }
    }

    private fun updateState(enable: Boolean) {
        count_unit.isEnable = enable
        count_quantity.isEnable = enable
        lot_serial_no.isEnable = enable
        make_date.isEnable = enable
        expiry.isEnable = enable
        tv_description.isEnabled = enable
    }

    private fun updateQuantity() {
        itemCount?.apply {
            val wareHouseQuantity = count_quantity.intValue * convQty1/ convQty2
            warehouse_unit.tv_content.text = if(wHUnitNm.isNotBlank()) "${wareHouseQuantity.format()}  $wHUnitNm" else "$wareHouseQuantity"
        }
    }

    override fun updatedValueListener(content: String, editView: EditView) {
        LogUtils.d("Updated value $content")
        when (editView) {
            count_quantity -> updateQuantity()
        }
    }

    companion object{
        const val EXTRA_CST_VISIT_NO = "CST_VISIT_NO"
    }


}