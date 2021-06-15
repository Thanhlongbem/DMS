package com.ziperp.dms.main.trackingreports.staff.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.extensions.ifEmptyLetBe
import com.ziperp.dms.main.trackingreports.staff.model.TrackingStaffListResponse
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.item_staff_tracking.view.*

class StaffListAdapter: BaseAdapter<TrackingStaffListResponse.Staff>() {

    override fun getLayoutId(): Int = R.layout.item_staff_tracking

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<TrackingStaffListResponse.Staff> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: TrackingStaffListResponse.Staff) {
            itemView.apply {
                AppUtil.loadImageFromURL(context, Constants.API_BASE_PATH + item.imgProfile, img_avatar, defaultImg = R.drawable.avata_default)
                tv_staff_name.text = DataConvertUtils.convertTitle(item.staffNm)
                tv_staff_state.text = DataConvertUtils.convertTitle(item.moveSts)
                tv_staff_state.setTextColor(resources.getColor(R.color.color_tag_opening))
                tv_time.text = item.timeLogPos
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
                tv_location.text = item.posName.ifEmptyLetBe("N/A")
                tv_customer_plan.text = item.planVisitQty.toString()
                tv_customer_actual.text = item.actVistQty.toString()
                tv_sales_plan.text = "${item.planSOQty}/${item.planSalesAmt}"
                tv_sales_actual.text = "${item.actSQQty}/${item.actSalesAmt}"
            }
        }
    }

    fun ImageView.setBatteryImage(iconID: Int, colorCode: String) {
        this.setImageResource(iconID)
        this.setColorFilter(Color.parseColor(colorCode), PorterDuff.Mode.SRC_ATOP)
    }
}