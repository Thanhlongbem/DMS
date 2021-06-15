package com.ziperp.dms.main.timekeeping.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.getDrawable
import com.ziperp.dms.extensions.ifEmptyLetBe
import com.ziperp.dms.main.timekeeping.model.TimeKeeping
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.item_time_keeping_in.view.*
import kotlinx.android.synthetic.main.item_time_keeping_in.view.tv_location
import kotlinx.android.synthetic.main.item_time_keeping_in.view.tv_note
import kotlinx.android.synthetic.main.item_time_keeping_in.view.tv_time
import kotlinx.android.synthetic.main.item_time_keeping_out.view.*

class TimeKeepingAdapter : BaseAdapter<TimeKeeping>() {
    override fun getLayoutId(): Int = R.layout.item_time_keeping_in

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<TimeKeeping> {
        override fun bind(item: TimeKeeping) {
            itemView.apply {
                tv_time.text = DataConvertUtils.divideTime(item.checkInTime)
                tv_location.text = item.checkInPosNm.ifEmptyLetBe(
                    if (item.checkInLatPos.isBlank()) "N/A" else "Waiting for synchronization"
                )
                tv_note.text = item.remarkCheckin.ifEmptyLetBe("N/A") + " No: " + item.timeKeepNo
                img_tick_checkin.setImageDrawable(if(item.isSynchonizedCheckIn) R.drawable.ic_ticked.getDrawable() else R.drawable.icon_not_synchonized.getDrawable())


                if(item.checkOutTime.isBlank()) {
                    layout_check_out.visibility = View.GONE
                    invalid_check_out.visibility = View.VISIBLE
                    layout_check_out.img_tick_checkout.visibility = View.INVISIBLE
                } else {
                    layout_check_out.visibility = View.VISIBLE
                    invalid_check_out.visibility = View.GONE
                    layout_check_out.img_tick_checkout.visibility = View.VISIBLE

                    layout_check_out.apply {
                        tv_duration.text = "${item.durationHour.format()} Hours"
                        tv_time.text = DataConvertUtils.divideTime(item.checkOutTime)
                        tv_location.text = item.checkOutPosNm.ifEmptyLetBe(
                            if (item.checkOutLatPos.isBlank()) "N/A" else "Waiting for synchronization"
                        )
                        tv_note.text = item.remarkCheckout.ifEmptyLetBe("N/A")
                        img_tick_checkout.setImageDrawable(if(item.isSynchonizedCheckOut) R.drawable.ic_ticked.getDrawable() else R.drawable.icon_not_synchonized.getDrawable())

                    }
                }
            }
        }
    }
}