package com.ziperp.dms.main.saleorder.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.main.saleorder.model.SaleOrderItem
import com.ziperp.dms.utils.AppUtil
import kotlinx.android.synthetic.main.item_order_items.view.*

class OrderItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data: MutableList<SaleOrderItem> = ArrayList()
    var onClickListener: ((position: Int) -> Unit)? = null
    var onDelete: ((position: Int) -> Unit)? = null
    var context: Context? = null

    override fun getItemViewType(position: Int): Int {
        if (data[position].priceChk == "G") return 0 //Good
        return 1 //Accessory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        if (viewType == 0) return ItemOrderViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_order_items, parent, false)
        )
        return AccessoryViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_accessory, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        if (holder is ItemOrderViewHolder) holder.bind(item)
        if (holder is AccessoryViewHolder) holder.bind(item)

        holder.itemView.setOnClickListener { onClickListener?.invoke(position) }
        holder.itemView.btn_delete.setOnClickListener {
            AppUtil.showAlertDialog(context, R.string.str_question_delete.getString(), {
            onDelete?.invoke(position)
        }, {}) }
    }

    override fun getItemCount(): Int = data.size

    fun getItem(position: Int): SaleOrderItem {
        return data[position]
    }


    fun addData(list: List<SaleOrderItem>) {
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun updateData(list: List<SaleOrderItem>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    inner class ItemOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: SaleOrderItem) {
            itemView.apply {
                tv_orderItemName.text = "${item.itemNo} - ${item.itemNm}"
                tv_salesUnitValue.text = item.uoMNm
                tv_SalesQtyValue.text = item.orderQty.format()
                tv_stockUnitValue.text = item.wHUnitNm
                tv_qtyWUValue.text = item.stkQty.format()
                tv_availQtyValue.text = item.availStk.format()
                tv_goodTypeValue.text = item.priceChkNm
                tv_warehouseValue.text = item.whNm + " - " + item.priceChkNm
                tv_salesPricesValue.text = item.unitPrice.format()
                tv_discountValue.text = item.discountAmt.format() + " - " + item.discount.format() + "%"
                tv_vatRateValue.text = item.vatRate.format() + "%"
                tv_totalAmountValue.text = item.totalAmount.format()
                img_iconOrderItemType.setImageResource(R.drawable.icon_shopping)
            }
        }
    }

    inner class AccessoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: SaleOrderItem) {
            itemView.apply {
                tv_orderItemName.text = "${item.itemNo} - ${item.itemNm}"
                tv_salesUnitValue.text = item.uoMNm
                tv_SalesQtyValue.text = item.orderQty.format()
                tv_stockUnitValue.text = item.wHUnitNm
                tv_qtyWUValue.text = item.stkQty.format()
                tv_availQtyValue.text = item.availStk.format()
                tv_goodTypeValue.text = item.priceChkNm
                tv_warehouseValue.text = item.whNm + " - " + item.priceChkNm
                img_iconOrderItemType.setImageResource(R.drawable.icon_accessory)
            }
        }
    }

}