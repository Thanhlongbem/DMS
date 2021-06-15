package com.ziperp.dms.main.personalreport.timekeeping.view

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.format
import com.ziperp.dms.main.personalreport.timekeeping.model.TKDiaryResponse
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.item_time_keeping_diary.view.*

class TKDiaryAdapter(): BaseAdapter<TKDiaryResponse.TimeKeepingDiary>() {

    override fun getLayoutId(): Int = R.layout.item_time_keeping_diary

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<TKDiaryResponse.TimeKeepingDiary> {
        override fun bind(item: TKDiaryResponse.TimeKeepingDiary) {
            itemView.apply {
                tv_day.text = DataConvertUtils.convertTitle(item.day)
                tv_check_in_time.text = DataConvertUtils.convertTitle(reformatTime(item.checkinTime))
                tv_late.text = DataConvertUtils.convertTitle(item.numbLate)
                tv_check_out_time.text = DataConvertUtils.convertTitle(reformatTime(item.checkoutTime))
                tv_early.text = DataConvertUtils.convertTitle(item.numbEarly)
                tv_duration.text = DataConvertUtils.convertTitle(item.workDuration).toDouble().format()

                if (item.day.contains("#@$")) layout_item.setBackgroundColor(Color.parseColor(item.day.substringAfterLast("#@$")))
                else layout_item.setBackgroundColor(Color.parseColor("#ffffff"))
            }
        }
    }

    fun reformatTime(time: String): String {
        var result = time
        if (result != "N/A") {
            result = time.substring(0,2) + ":" + time.substring(2,4)
        }
        return result
    }
}