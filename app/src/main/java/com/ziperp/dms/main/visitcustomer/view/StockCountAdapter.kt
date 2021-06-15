package com.ziperp.dms.main.visitcustomer.view

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.main.visitcustomer.model.StockCountListResponse
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.item_stock.view.*

class StockCountAdapter: BaseAdapter<StockCountListResponse.StockCount>() {

    override fun getLayoutId(): Int = R.layout.item_stock

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<StockCountListResponse.StockCount> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: StockCountListResponse.StockCount) {
            itemView.apply {
                tv_StockCountName.text = "${item.itemNo} - ${item.itemNm}"
                tv_count_unit_value.text = "${item.cntQty.format()} ${item.cntUoMNm}"
                tv_stockUnitValue.text = "${item.stkCntQty.format()} ${item.stkUoMNm}"
                tv_lot_serial_value.text = item.lotNo
                tv_mfgDateValue.text = item.mfgDate.reformatDate()
                tv_expDateValue.text = item.expiryDate.reformatDate()
            }
        }
    }
}