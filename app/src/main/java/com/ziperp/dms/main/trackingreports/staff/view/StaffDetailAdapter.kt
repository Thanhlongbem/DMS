package com.ziperp.dms.main.trackingreports.staff.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.ifEmptyLetBe
import com.ziperp.dms.main.trackingreports.staff.model.TrackingStaffDetailResponse
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.item_staff_detail.view.*

class StaffDetailAdapter: BaseAdapter<TrackingStaffDetailResponse.StaffDetail>() {

    override fun getLayoutId(): Int = R.layout.item_staff_detail

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<TrackingStaffDetailResponse.StaffDetail> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: TrackingStaffDetailResponse.StaffDetail) {
            itemView.apply {
                tv_activity.text = DataConvertUtils.convertTitle(item.typeCheckinNm)
                tv_time.text = if(item.logPosTime.contains("#")) "N/A" else item.logPosTime
                val batteryPercent = item.batteryPer.split("#")[0]
                tv_battery.text = batteryPercent.ifEmptyLetBe("0") + "%"
                when(batteryPercent.ifEmptyLetBe("0").toInt()) {
                    in (0..16) -> {
                        img_battery.setBatteryImage(R.drawable.ic_super_low_battery, "#F65255")
                    }
                    in (16..31) -> {
                        img_battery.setBatteryImage(R.drawable.ic_low_battery, "#FF9052")
                    }
                    in (31..61) -> {
                        img_battery.setBatteryImage(R.drawable.ic_good_battery, "#17992C")
                    }
                    in (61..101) -> {
                        img_battery.setBatteryImage(R.drawable.ic_full_battery, "#17992C")
                    }
                }
                tv_visit_customer.text = item.cstNm.ifEmptyLetBe("N/A")
                tv_location.text = item.posName
                tv_note.text = item.remark
            }
        }
    }

    fun ImageView.setBatteryImage(iconID: Int, colorCode: String) {
        this.setImageResource(iconID)
        this.setColorFilter(Color.parseColor(colorCode), PorterDuff.Mode.SRC_ATOP)
    }
}