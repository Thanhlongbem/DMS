package com.ziperp.dms.main.salespricepromotion.view

import android.content.Intent
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.setOnClickNetworkListener
import com.ziperp.dms.main.salespricepromotion.view.promotion.PromotionActivity
import com.ziperp.dms.main.salespricepromotion.view.salesprice.SalesPriceActivity
import kotlinx.android.synthetic.main.activity_sales_price_promotion.*

class SalesPricePromotionActivity: BaseActivity() {
    override fun setLayoutId(): Int = R.layout.activity_sales_price_promotion

    override fun initView() {
        setToolbar(R.string.str_sale_price_promotion.getString(), true)

        sales_price_list.setOnClickNetworkListener { moveTo(SalesPriceActivity::class.java) }
        promotion_list.setOnClickNetworkListener { moveTo(PromotionActivity::class.java) }
    }

    private fun moveTo(destination: Class<*>) {
        startActivity(Intent(this, destination))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}