package com.ziperp.dms.main.trackingreports.newpoint.view

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.main.trackingreports.newpoint.model.NewSalesPointResponse
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.item_daily_report.view.*

class NewSalesPointAdapter(): BaseAdapter<NewSalesPointResponse.SalesPoint>() {

    override fun getLayoutId(): Int = R.layout.item_daily_report

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<NewSalesPointResponse.SalesPoint> {
        override fun bind(item: NewSalesPointResponse.SalesPoint) {
            itemView.apply {
                tv_0.text = DataConvertUtils.convertTitle(item.txtGroupNm)
                tv_1.text = DataConvertUtils.convertTitle(item.intBeginQty)
                tv_2.text = DataConvertUtils.convertTitle(item.intNewQty)
                tv_3.text = DataConvertUtils.convertTitle(item.intEndQty)

                if (item.txtGroupNm.contains("#@$")) layout_item.setBackgroundColor(Color.parseColor(item.txtGroupNm.substringAfterLast("#@$")))
                else layout_item.setBackgroundColor(Color.parseColor("#ffffff"))
            }
        }
    }
}