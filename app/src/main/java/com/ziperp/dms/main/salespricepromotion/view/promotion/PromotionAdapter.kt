package com.ziperp.dms.main.salespricepromotion.view.promotion

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.getColor
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.main.salespricepromotion.model.PromotionResponse
import kotlinx.android.synthetic.main.item_promotion.view.*

class PromotionAdapter : BaseAdapter<PromotionResponse.Promotion>() {
    override fun getLayoutId(): Int = R.layout.item_promotion

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<PromotionResponse.Promotion> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: PromotionResponse.Promotion) {
            itemView.apply {
                tv_title.text = item.promotionNm
                tv_period.text = "${item.startDate.reformatDate()} - ${item.endDate.reformatDate()}"
                tv_unit.text = item.currency
                tv_action.text = item.promoAction
                tv_gift.text = item.strGift
                tv_address.text = item.strStore
                tv_customer_type.text = item.strCustomer
                tv_note.text = item.promotionDesc
                tv_status.text = item.activeStsNm
                tv_valid_status.text = item.validStatusNm
                when(item.activeSts) {
                    0 -> {
                        tv_status.setTextColor(R.color.color_tag_opening.getColor())
                        tv_status.setBackgroundResource(R.drawable.bg_vc_opening_state)
                    }
                    1 -> {
                        tv_status.setTextColor(R.color.color_issued.getColor())
                        tv_status.setBackgroundResource(R.drawable.bg_vc_issued_state)
                    }
                }
                when(item.validStatus) {
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
    }
}