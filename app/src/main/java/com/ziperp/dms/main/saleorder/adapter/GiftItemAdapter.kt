package com.ziperp.dms.main.saleorder.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.ifEmptyLetBe
import com.ziperp.dms.main.saleorder.model.SaleOrderItem
import kotlinx.android.synthetic.main.item_gift.view.*

class GiftItemAdapter : BaseAdapter<SaleOrderItem>() {

    override fun getLayoutId(): Int = R.layout.item_gift

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<SaleOrderItem> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: SaleOrderItem){
            itemView.apply {
                tv_itemName.text = item.itemNo + " - " + item.itemNm
                tv_unitValue.text = item.uoMNm
                tv_quantityValue.text = item.orderQty.format()
                tv_availQtyValue.text = item.availStk.format()
                tv_warehouseValue.text = item.whNm + " - " + item.priceChkNm
                tv_parentItemValue.text = item.parentItem.ifEmptyLetBe("N/A")
                tv_promotionValue.text = item.promoCd.ifEmptyLetBe("N/A")
            }
        }

    }


}