package com.ziperp.dms.main.itemmaster.view

import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.main.itemmaster.model.ItemMasterDetailResponse
import kotlinx.android.synthetic.main.fragment_item_master_detail.*
import kotlinx.android.synthetic.main.item_detail.view.*
import kotlinx.android.synthetic.main.item_detail_header.view.*

class DetailFragment(val data: ItemMasterDetailResponse.ItemMasterDetail): BaseFragment() {
    override fun setLayoutId(): Int = R.layout.fragment_item_master_detail

    override fun initView() {
        data.apply {
            item_name.tv_title.text = R.string.str_item_name.getString()
            item_name.tv_content.text = itemEnNm
            item_type.tv_title.text = R.string.str_item_type.getString()
            item_type.tv_content.text = itemTypeNm
            item_status.tv_title.text = R.string.str_item_status.getString()
            item_status.tv_content.text = itemStsNm
            PiC.tv_title.text = R.string.str_pic.getString()
            PiC.tv_content.text = defPICNm
            lot_serial_control.tv_title.text = R.string.str_lot_serial_control.getString()
            lot_serial_control.tv_content.text = lotSeriCtrlNm
            sales_price.tv_title.text = R.string.str_sales_std_price.getString()
            sales_price.tv_content.text = defSellPrice.toString()
            buy_price.tv_title.text = R.string.str_buy_std_price.getString()
            buy_price.tv_content.text = defBuyPrice.toString()
            vat_sales_price.tv_title.text = R.string.str_vat_sales_price.getString()
            vat_sales_price.tv_content.text = if (vatType == "1") R.string.str_use.getString() else R.string.str_not_use.getString()
            vat_purchasing_price.tv_title.text = R.string.str_vat_sales_price.getString()
            vat_purchasing_price.tv_content.text = if (vatType == "2") R.string.str_use.getString() else R.string.str_not_use.getString()
            vat_rate.tv_title.text = R.string.str_vat_rate.getString()
            vat_rate.tv_content.text = vatRate.toString()
            item_category.tv_title.text = R.string.str_item_category.getString()
            item_category.tv_content.text = itemCategoryNm
            item_brand.tv_title.text = R.string.str_item_brand.getString()
            item_brand.tv_content.text = itemBrandNm
            item_grade.tv_title.text = R.string.str_item_grade.getString()
            item_grade.tv_content.text = itemGrade
            item_model.tv_title.text = R.string.str_item_model.getString()
            item_model.tv_content.text = itemModel
            item_group_1.tv_title.text = String.format(R.string.str_item_group.getString(), 1)
            item_group_1.tv_content.text = itemGrp1Nm
            item_group_2.tv_title.text = String.format(R.string.str_item_group.getString(), 2)
            item_group_2.tv_content.text = itemGrp2Nm
            item_group_3.tv_title.text = String.format(R.string.str_item_group.getString(), 3)
            item_group_3.tv_content.text = itemGrp3Nm
            item_group_4.tv_title.text = String.format(R.string.str_item_group.getString(), 4)
            item_group_4.tv_content.text = itemGrp4Nm

            header_sale_purchasing.tv_header.text = R.string.str_sales_purchasing.getString()
            default_purchase_unit.tv_title.text = R.string.str_default_purchase_unit.getString()
            default_purchase_unit.tv_content.text = buyUnitNm
            default_sale_unit.tv_title.text = R.string.str_default_sales_unit.getString()
            default_sale_unit.tv_content.text = salesUnitNm
            default_vendor.tv_title.text = R.string.str_default_vendor.getString()
            default_vendor.tv_content.text = defVendorNm
            default_currency.tv_title.text = R.string.str_default_currency.getString()
            default_currency.tv_content.text = buyDefCurrCd
            default_maker.tv_title.text = R.string.str_default_maker.getString()
            default_maker.tv_content.text = defMakerNm
            country.tv_title.text = R.string.str_country.getString()
            country.tv_content.text = originCountryNm
            lead_time.tv_title.text = R.string.str_lead_time.getString()
            lead_time.tv_content.text = avgBuyLeadtime.toString()
            tax_rate.tv_title.text = R.string.str_tax_rate.getString()
            tax_rate.tv_content.text = specialTaxRate.toInt().toString()
            hs_no.tv_title.text = R.string.str_hs_no.getString()
            hs_no.tv_content.text = itemHSNo

            header_other.tv_header.text = R.string.str_other_info.getString()
            vendor_warranty_period.tv_title.text = R.string.str_vendor_warranty_period.getString()
            vendor_warranty_period.tv_content.text = warrantyPrdNm
            customer_warranty_period.tv_title.text = R.string.str_customer_warranty_period.getString()
            customer_warranty_period.tv_content.text = cstWarrantyPrdNm
            expired_duration.tv_title.text = R.string.str_expiry_duration.getString()
            expired_duration.tv_content.text = expireDurationNm
            keeping_condition.tv_title.text = R.string.str_keeping_condition.getString()
            keeping_condition.tv_content.text = keepCondNm
            header_description.tv_header.text = R.string.str_customer_description.getString()
            tv_description.text = if (detailDesc.isNotEmpty()) detailDesc else R.string.str_not_available.getString()
        }
    }
}