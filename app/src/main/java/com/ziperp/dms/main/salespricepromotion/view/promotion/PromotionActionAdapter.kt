package com.ziperp.dms.main.salespricepromotion.view.promotion

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.getColor
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.main.salespricepromotion.model.PromotionInfoResponse
import com.ziperp.dms.main.salespricepromotion.model.PromotionResponse
import kotlinx.android.synthetic.main.item_promotion.view.*
import kotlinx.android.synthetic.main.item_promotion_action.view.*

class PromotionActionAdapter : BaseAdapter<Any>() {
    override fun getLayoutId(): Int = R.layout.item_promotion_action

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<Any> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: Any) {
            itemView.apply {
                when(item) {
                    is PromotionInfoResponse.Item -> tv_content.text = item.strGiftItem
                    is PromotionInfoResponse.Condition -> tv_content.text = item.strConditionApply
                }
            }
        }
    }
}