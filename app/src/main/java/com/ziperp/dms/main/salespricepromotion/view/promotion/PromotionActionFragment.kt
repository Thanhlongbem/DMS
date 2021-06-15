package com.ziperp.dms.main.salespricepromotion.view.promotion

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.main.salespricepromotion.model.PromotionInfoResponse
import kotlinx.android.synthetic.main.fragment_promotion_action.*

class PromotionActionFragment(val promotionInfo: PromotionInfoResponse): BaseFragment() {

    override fun setLayoutId(): Int = R.layout.fragment_promotion_action

    @SuppressLint("SetTextI18n")
    override fun initView() {
        promotionInfo.generalInfo[0].apply {
            tv_action.text = promoAction
            tv_buy_step.text = stepBuyItem
            tv_max_gift.text = maximumGiftGet
            tv_condition.text = "${R.string.str_condition.getString()}: $combinationNm"
        }

        val giftAdapter = PromotionActionAdapter()
        giftAdapter.addData(promotionInfo.items)
        if (promotionInfo.items.isEmpty()) tv_no_gift.visibility = View.VISIBLE
        rv_list_gift.layoutManager = LinearLayoutManager(activity)
        rv_list_gift.adapter = giftAdapter
        rv_list_gift.setHasFixedSize(true)

        val conditionAdapter = PromotionActionAdapter()
        conditionAdapter.addData(promotionInfo.condition)
        rv_list_condition.layoutManager = LinearLayoutManager(activity)
        rv_list_condition.adapter = conditionAdapter
        rv_list_condition.setHasFixedSize(true)

    }
}