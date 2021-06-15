package com.ziperp.dms.main.trackingreports.dailyperformance.view

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.format
import com.ziperp.dms.main.trackingreports.dailyperformance.model.DailyPerformanceResponse
import com.ziperp.dms.main.trackingreports.dailyperformance.view.DailyPerformanceActivity.Companion.MODE_SALES_ORDER
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.item_daily_report.view.*

class DailyReportAdapter(val mode: Int): BaseAdapter<DailyPerformanceResponse.DailyReport>() {

    override fun getLayoutId(): Int = R.layout.item_daily_report

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<DailyPerformanceResponse.DailyReport> {
        override fun bind(item: DailyPerformanceResponse.DailyReport) {
            itemView.apply {
                tv_0.text = DataConvertUtils.convertTitle(item.objctNm)
                tv_1.text = DataConvertUtils.convertTitle(if (mode == MODE_SALES_ORDER) item.numbVisit else item.numbMorningVist)
                tv_2.text = DataConvertUtils.convertTitle(if (mode == MODE_SALES_ORDER) item.numbOrder else item.numbAfternoonVisit)
                tv_3.text = DataConvertUtils.convertTitle(if (mode == MODE_SALES_ORDER) item.orderQty else item.distance).toDouble().format()

                if (item.objctNm.contains("#@$")) layout_item.setBackgroundColor(Color.parseColor(item.objctNm.substringAfterLast("#@$")))
                else layout_item.setBackgroundColor(Color.parseColor("#ffffff"))
            }
        }
    }
}