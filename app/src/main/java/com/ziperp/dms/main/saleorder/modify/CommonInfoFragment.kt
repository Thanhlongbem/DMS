package com.ziperp.dms.main.saleorder.modify

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseCUDActivity.Companion.CREATE_MODE
import com.ziperp.dms.base.BaseCUDFragment
import com.ziperp.dms.common.model.EditView
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.form.model.KeyValue
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.extensions.*
import com.ziperp.dms.main.saleorder.model.CUDSalesOrderRequest
import com.ziperp.dms.main.saleorder.model.SaleOrderDetailResponse
import com.ziperp.dms.main.saleorder.viewmodel.SaleOrderViewModel
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_common_info_modify.*
import kotlinx.android.synthetic.main.item_detail_header.view.*

class CommonInfoFragment(mode: Int = CREATE_MODE): BaseCUDFragment<SaleOrderDetailResponse.SaleOrderCommonInfo>(mode) {
    val saleActivity by lazy { activity as SalesOrderModifyActivity }
    val sharedVM: SaleOrderViewModel by activityViewModels()
    var customerPhone: String = ""

    override fun setLayoutId(): Int = R.layout.fragment_common_info_modify

    override fun initView() {
        super.initView()

        payment_header.tv_header.text = R.string.str_payment_information.getString()
        addition_info_header.tv_header.text = R.string.str_addition_information.getString()

        delivery_time.setShowDateTimePicker()
        order_date.setShowDatePicker()

        sharedVM.exchangeRateLiveData.observe(this, Observer {
            when(it.status){
                Status.SUCCESS ->{
                    it.data?.rates?.get(0)?.let {
                        ex_rate.setValue(it.exRate.format())
                    }
                }
            }
        })

        if(mode == CREATE_MODE){
            updatePhone(customerPhone)
            loadDefaultValue()
            setOnUpdatedValueListener(order_date)
        }

    }

    override fun updatedValueListener(content: String, editView: EditView){
        if(editView == order_date){
            updatePriceListNo()
        }
    }

    fun updatePhone(phoneNumber: String){
        customer_phone.setValue(phoneNumber)
    }

    private fun loadDefaultValue(){
        DmsUserManager.userInfo.apply {
            listOption[0].second.controlValue = arrayListOf(
                KeyValue(
                    keyCode = "CmbVal",
                    valueCode = masterLocCd,
                    keyName = "CmbText",
                    valueName = masterLocNm
                )
            )

            business_unit.setValue(masterLocNm)

            //sale man
            listOption[1].second.controlValue = arrayListOf(
                KeyValue(
                    keyCode = "StaffId",
                    valueCode = staffId,
                    keyName = "StaffNm",
                    valueName = staffNm
                )
            )
            salesman.setValue(staffNm)

            //sale department
            listOption[2].second.controlValue = arrayListOf(
                KeyValue(
                    keyCode = "DeptCd",
                    valueCode = deptCd,
                    keyName = "DeptNm",
                    valueName = deptNm
                )
            )
            sales_department.setValue(deptNm)

            //currency
            listOption[5].second.controlValue = arrayListOf(
                KeyValue(
                    keyCode = "CurrencyCd",
                    valueCode = lcBaseCurr,
                    keyName = "CurrencyNm",
                    valueName = lcBaseCurr
                )
            )
            currency.setValue(lcBaseCurr)
            val currencyCd = listOption[5].second.getFilterValue()
            sharedVM.getExchangeRate(currencyCd, listOption[0].second.getFilterValue())

        }
    }

    override fun createNewForm() {
        item = SaleOrderDetailResponse.SaleOrderCommonInfo()
        rb_group.check(R.id.rb_company_warehouse)
        when (DmsUserManager.userInfo.salesPriceRule) {
            0, 3 -> {
                purchase_price_list.visibility = View.GONE
                purchase_contract_no.visibility = View.VISIBLE
            }
            1, 2 -> {
                purchase_contract_no.visibility = View.GONE
                purchase_price_list.visibility = View.VISIBLE
                listOption.add(
                    purchase_price_list to ItemControlForm(
                        formId = Form.FORM_ID_SALE_ORDER_MODIFY,
                        controlId = "txtContractNo",
                        controlName = "Price List",
                        lookUpCode = "PRIC",
                        lookUpType = Form.LookUpType.DIALOG,
                        findType = Form.FindType.NAME
                    )
                )
                listMandatoryView.add(purchase_price_list)
                purchase_price_list.isEnable = false
            }
        }

    }

    override fun fillCurrentInfo() {
        item.apply {
            /**Common Info **/
            //Update business
            listOption[0].second.controlValue = arrayListOf(
                KeyValue(
                    keyCode = "CmbVal",
                    valueCode = masterLocCd,
                    keyName = "CmbText",
                    valueName = masterLocNm
                 )
            )
            business_unit.setValue(masterLocNm)
            business_unit.isEnable = false

            //order date
            order_date.setValue(orderDate.reformatDate())
            order_date.isEnable = false

            //update warehouse
            if(whType == "C"){
                rb_group.check(R.id.rb_company_warehouse)
            } else {
                rb_group.check(R.id.rb_distributor_warehouse)
            }
            rb_company_warehouse.isEnabled = false
            rb_distributor_warehouse.isEnabled = false


            when (DmsUserManager.userInfo.salesPriceRule) {
                0, 3 -> {
                    purchase_contract_no.setValue(poNm)
                    purchase_price_list.visibility = View.GONE
                    purchase_contract_no.visibility = View.VISIBLE
                }
                1, 2 -> {
                    listOption.add(
                        purchase_price_list to ItemControlForm(
                            formId = Form.FORM_ID_SALE_ORDER_MODIFY,
                            controlId = "txtContractNo",
                            controlName = "Price List",
                            lookUpCode = "PRIC",
                            lookUpType = Form.LookUpType.DIALOG,
                            findType = Form.FindType.NAME
                        )
                    )
                    listMandatoryView.add(purchase_price_list)
                    purchase_price_list.isEnable = false

                    //Purchase List
                    listOption[7].second.controlValue = arrayListOf(
                        KeyValue(
                            valueCode = poNo,
                            valueName = poNm
                        )
                    )
                    purchase_price_list.setValue(poNm)
                    purchase_contract_no.visibility = View.GONE
                    purchase_price_list.visibility = View.VISIBLE
                    purchase_price_list.isEnable = false
                }
            }

            //Customer Phone
            customer_phone.setValue(cstPhone)
            //sale man
            listOption[1].second.controlValue = arrayListOf(
                KeyValue(
                    keyCode = "StaffId",
                    valueCode = staffId,
                    keyName = "StaffNm",
                    valueName = staffNm
                )
            )
            salesman.setValue(staffNm)

            //sale department
            listOption[2].second.controlValue = arrayListOf(
                KeyValue(
                    keyCode = "DeptCd",
                    valueCode = deptCd,
                    keyName = "DeptNm",
                    valueName = deptNm
                )
            )
            sales_department.setValue(deptNm)
            /**Delivery Information **/

            delivery_contact_name.setValue(contactName)
            contact_phone.setValue(contactPhone)
            delivery_address.setValue(delvAddress)
            delivery_time.setValue(delvDateTime.reformatDateTime())

            //ship method
            listOption[3].second.controlValue = arrayListOf(
                KeyValue(
                    keyCode = "CmbVal",
                    valueCode = shipMethod,
                    keyName = "CmbText",
                    valueName = shipMethodNm
                )
            )
            ship_method.setValue(shipMethodNm)

            item_remark.setValue(remark)

            /**Additional Information **/
            //project
            listOption[4].second.controlValue = arrayListOf(
                KeyValue(
                    keyCode = "ProjectCd",
                    valueCode = projectCd,
                    keyName = "ProjectNm",
                    valueName = projectNm
                )
            )
            project_name.setValue(projectNm)

            //currency
            listOption[5].second.controlValue = arrayListOf(
                KeyValue(
                    keyCode = "CurrencyCd",
                    valueCode = currencyCd,
                    keyName = "CurrencyNm",
                    valueName = currencyCd
                )
            )
            currency.setValue(currencyCd)
            ex_rate.setValue(exRate.format())

            //Payment term
            payment_term.setValue(paymentTerm)

            //sale channel
            listOption[6].second.controlValue = arrayListOf(
                KeyValue(
                    keyCode = "CmbVal",
                    valueCode = sChannels,
                    keyName = "CmbText",
                    valueName = salesChannelName
                )
            )
            sales_channel.setValue(salesChannelName)

        }

        btn_delete.visibility = View.VISIBLE
        btn_delete.setOnClickListener {
            AppUtil.showAlertDialog(context, R.string.str_question_delete.getString(), {
                saleActivity.deleteSalesOrder()
            }, {})

        }
    }

    fun updatePriceListNo() {
        if(mode == CREATE_MODE){
            if((saleActivity.customerCode.isNotBlank()
                        && listOption[5].second.getFilterValue().isNotBlank()
                        && order_date.content.isNotBlank())){
                purchase_price_list.isEnable = true
                listOption[7].second.param.apply {
                    item1 = saleActivity.customerCode
                    item2 = listOption[5].second.getFilterValue() //CurrencyCd
                    item3 = order_date.content.convertDate() //OrderDate
                }
            }else{
                purchase_price_list.isEnable = false
            }
        }
    }

    override fun onUpdateItemControl(index: Int) {
        when (index){
            5 ->{
                val currencyCd = listOption[5].second.getFilterValue()
                sharedVM.getExchangeRate(currencyCd, listOption[0].second.getFilterValue())
                updatePriceListNo()
            }
        }
    }

    override fun setListMandatoryView(): ArrayList<EditView> = arrayListOf(business_unit, order_date, salesman, sales_department, currency)

    override fun setListOption(): ArrayList<Pair<EditView, ItemControlForm>> = arrayListOf(
        business_unit to ItemControlForm(//0
            formId = Form.FORM_ID_SALE_ORDER_MODIFY,
            controlId = "cmbBusinessUnit",
            controlName = "Business Unit",
            lookUpCode = "0001",
            lookUpType = Form.LookUpType.COMBO,
            findType = Form.FindType.CODE
        ),
        salesman to ItemControlForm(//1
            formId = Form.FORM_ID_SALE_ORDER_MODIFY,
            controlId = "txtSalesman",
            controlName = "Salesman",
            lookUpCode = "STAF",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.NAME
        ),
        sales_department to ItemControlForm(//2
            formId = Form.FORM_ID_SALE_ORDER_MODIFY,
            controlId = "txtSalesDept",
            controlName = "Sales Department",
            lookUpCode = "DEPT",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.NAME
        ),
        ship_method to ItemControlForm(//3
            formId = Form.FORM_ID_SALE_ORDER_MODIFY,
            controlId = "cmbShippingMethod",
            controlName = "Ship Method",
            lookUpCode = "0049",
            lookUpType = Form.LookUpType.COMBO,
            findType = Form.FindType.CODE
        ),
        project_name to ItemControlForm(//4
            formId = Form.FORM_ID_SALE_ORDER_MODIFY,
            controlId = "txtProjectCode",
            controlName = "Project Name",
            lookUpCode = "PRJM",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.NAME
        ),
        currency to ItemControlForm(//5
            formId = Form.FORM_ID_SALE_ORDER_MODIFY,
            controlId = "txtCurrency",
            controlName = "Currency",
            lookUpCode = "0079",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.CODE
        ),
        sales_channel to ItemControlForm(//6
            formId = Form.FORM_ID_SALE_ORDER_MODIFY,
            controlId = "cmbSalesChannel",
            controlName = "Sales Channel",
            lookUpCode = "0041",
            lookUpType = Form.LookUpType.COMBO,
            findType = Form.FindType.CODE
        )
    )

    override fun getCUDInfo(): CUDSalesOrderRequest {
        val request = CUDSalesOrderRequest()
        var contractNo = if (purchase_price_list.visibility == View.VISIBLE) {
            listOption[7].second.getFilterValue()
        } else {
            purchase_contract_no.content
        }

        var priceListNm = if (purchase_price_list.visibility == View.VISIBLE) {
            listOption[7].second.getDisplayText()
        } else ""

        request.apply {
            masterLocCd = listOption[0].second.getFilterValue()
            orderNo = item.orderNo
            orderMngtNo = item.orderMngtNo
            poNo = contractNo
            poNm = priceListNm // only purchase_price_list has
            orderVer = "01"
            orderDate = order_date.content.convertDate()
            whType = if (rb_group.checkedRadioButtonId == R.id.rb_company_warehouse) "C" else "D"
            remark = item_remark.content
            projectCd = listOption[4].second.getFilterValue()
            cstPhone = customer_phone.content
            sChannels = listOption[6].second.getFilterValue()
            shipMethod = listOption[3].second.getFilterValue()
            staffId = listOption[1].second.getFilterValue()
            deptCd = listOption[2].second.getFilterValue()
            paymentTerm = payment_term.content
            currencyCd = listOption[5].second.getFilterValue()
            exRate = ex_rate.intValue
            delvDateTime = delivery_time.content.convertDateTime()
            contactName = delivery_contact_name.content
            contactPhone = contact_phone.content
            delvAddress = delivery_address.content
        }
        return request
    }

}