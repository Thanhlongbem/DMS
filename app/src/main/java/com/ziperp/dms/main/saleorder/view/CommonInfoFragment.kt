package com.ziperp.dms.main.saleorder.view

import android.annotation.SuppressLint
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.extensions.reformatDateTime
import com.ziperp.dms.main.saleorder.model.SaleOrderDetailResponse
import kotlinx.android.synthetic.main.fragment_common_info.*
import kotlinx.android.synthetic.main.item_detail.view.*
import kotlinx.android.synthetic.main.item_detail_header.view.*

class CommonInfoFragment(val data: SaleOrderDetailResponse) : BaseFragment() {

    companion object {
        val TAG = CommonInfoFragment::class.java.canonicalName
    }

    override fun setLayoutId(): Int = R.layout.fragment_common_info

    override fun initView() {
        initData()
    }

    @SuppressLint("SetTextI18n")
    fun initData(){
        layout_biz_unit.tv_title.text = R.string.str_business_unit.getString()
        layout_warehouseIssueType.tv_title.text = R.string.str_wu_issue_type.getString()
        layout_saleChannel.tv_title.text = R.string.str_sales_channel.getString()
        layout_prepaidAmount.tv_title.text = R.string.str_prepaid_amount.getString()
        layout_discountAmount.tv_title.text = R.string.str_discount_amount.getString()
        layout_paymentTerm.tv_title.text = R.string.str_payment_term.getString()
        layout_additionalInformation.tv_header.text = R.string.str_addition_information.getString()
        layout_poCn.tv_title.text = R.string.str_po_cn.getString()
        layout_project.tv_title.text = R.string.str_project.getString()
        layout_currency.tv_title.text = R.string.str_currency.getString()
        layout_exRate.tv_title.text = R.string.str_ex_rate.getString()
        layout_confirmer.tv_title.text = R.string.str_confirmer.getString()
        layout_confirmDate.tv_title.text = R.string.str_confirm_date.getString()
        layout_creator.tv_title.text = R.string.str_creator.getString()
        layout_create_date.tv_title.text = R.string.str_create_date.getString()
        layout_deliveryInformation.tv_header.text = R.string.str_delivery_information.getString()
        layout_deliveryAddress.tv_title.text = R.string.str_delivery_address.getString()
        layout_deliveryTime.tv_title.text = R.string.str_delivery_time.getString()
        layout_shipMethod.tv_title.text = R.string.str_ship_method.getString()
        layout_contactName.tv_title.text = R.string.str_contact_name.getString()
        tv_contactPhoneTitle.text = R.string.str_contact_phone_no.getString()

        setOnRegisterPhoneContextMenu(tv_contactPhoneContent)

        update(data)
    }

    fun update(response: SaleOrderDetailResponse) {
        if (!response.commonInfo.isNullOrEmpty()) {
            response.commonInfo[0].apply {
                layout_biz_unit.tv_content.text = masterLocNm
                layout_warehouseIssueType.tv_content.text = if (whType == "C") "Company Warehouse" else "Distributor Warehouse"
                layout_saleChannel.tv_content.text = salesChannelName
                layout_prepaidAmount.tv_content.text = prepaidAmt.format()
                layout_discountAmount.tv_content.text = ""
                layout_paymentTerm.tv_content.text = paymentTerm
                layout_poCn.tv_content.text = poNm
                layout_project.tv_content.text = projectNm
                layout_currency.tv_content.text = currencyCd
                layout_exRate.tv_content.text = exRate.format()
                layout_confirmer.tv_content.text = confirmer
                layout_confirmDate.tv_content.text = confirmDate.reformatDate()
                layout_creator.tv_content.text = creator
                layout_create_date.tv_content.text = createDate.reformatDate()
                layout_deliveryAddress.tv_content.text = delvAddress.reformatDate()
                layout_deliveryTime.tv_content.text = delvDateTime.reformatDateTime()
                layout_contactName.tv_content.text = contactName
                tv_contactPhoneContent.text = contactPhone
                layout_shipMethod.tv_content.text = shipMethodNm
                tv_remarkContent.text = remark
            }
        }
    }
}