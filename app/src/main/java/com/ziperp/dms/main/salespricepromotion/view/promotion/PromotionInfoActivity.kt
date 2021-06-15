package com.ziperp.dms.main.salespricepromotion.view.promotion

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.Observer
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.base.PagerAdapter
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getColor
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.main.salespricepromotion.model.PromotionInfoResponse
import com.ziperp.dms.main.salespricepromotion.model.SalesPriceInfoResponse
import com.ziperp.dms.main.salespricepromotion.view.promotion.PromotionActivity.Companion.EXTRA_PROMOTION_CODE
import com.ziperp.dms.main.salespricepromotion.view.salesprice.SalesPriceActivity.Companion.EXTRA_PRICE_LIST_CODE
import com.ziperp.dms.main.salespricepromotion.view.salesprice.SalesPriceCustomerFragment
import kotlinx.android.synthetic.main.activity_sales_price_info.*

class PromotionInfoActivity: BaseActivity() {

    lateinit var actionFragment: PromotionActionFragment
    lateinit var unitAppliedFragment: SalesPriceCustomerFragment
    lateinit var customerFragment: SalesPriceCustomerFragment

    private val viewModel by lazy { getViewModel { Injection.providePromotionViewModel() } }

    override fun setLayoutId(): Int = R.layout.activity_promotion_info

    override fun initView() {
        setToolbar(R.string.str_promotion_list_detail.getString(), true)

        viewModel.getPromotionInfo(intent.getStringExtra(EXTRA_PROMOTION_CODE)!!)
        viewModel.promotionInfoData.observe(this, Observer {
            when(it.status) {
                Status.LOADING -> { loading_progressbar.visibility = View.VISIBLE }
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        updateGeneral(response.generalInfo[0])
                        initPager(response)
                    }
                }
                Status.ERROR -> { loading_progressbar.visibility = View.GONE }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateGeneral(data: PromotionInfoResponse.GeneralInfo) {
        data.apply {
            tv_title.text = promoNm
            tv_price_unit.text = currencyCd
            tv_period.text = "${dateStartEffect.reformatDate()} - ${dateEndEffect.reformatDate()}"
            tv_remark.text = promoDesc
            tv_status.text = activeStsNm
            tv_valid_status.text = validStatusNm
            when(activeSts) {
                0 -> {
                    tv_status.setTextColor(R.color.color_tag_opening.getColor())
                    tv_status.setBackgroundResource(R.drawable.bg_vc_opening_state)
                }
                1 -> {
                    tv_status.setTextColor(R.color.color_issued.getColor())
                    tv_status.setBackgroundResource(R.drawable.bg_vc_issued_state)
                }
            }
            when(validStatus) {
                0 -> {
                    tv_valid_status.setTextColor(R.color.color_tag_stop.getColor())
                    tv_valid_status.setBackgroundResource(R.drawable.bg_vc_stop_state)
                }
                1 -> {
                    tv_valid_status.setTextColor(R.color.color_issued.getColor())
                    tv_valid_status.setBackgroundResource(R.drawable.bg_vc_issued_state)
                }
            }
        }
    }

    private fun initPager(data: PromotionInfoResponse) {
        actionFragment = PromotionActionFragment(data)
        unitAppliedFragment = SalesPriceCustomerFragment(data.unitApplied)
        customerFragment = SalesPriceCustomerFragment(data.customers)

        val pagerAdapter = PagerAdapter(supportFragmentManager, listFragments = listOf(actionFragment, unitAppliedFragment, customerFragment))
        pagerAdapter.listTitles = listOf(R.string.str_action.getString(), R.string.str_unit_applied.getString(), R.string.str_customer.getString())

        viewpager.adapter = pagerAdapter
        viewpager.offscreenPageLimit = 3
        tab.setupWithViewPager(viewpager)
    }
}