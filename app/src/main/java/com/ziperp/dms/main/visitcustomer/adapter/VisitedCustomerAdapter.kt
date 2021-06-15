package com.ziperp.dms.main.visitcustomer.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.core.tracking.LocationTrackingManager
import com.ziperp.dms.extensions.ifEmptyLetBe
import com.ziperp.dms.extensions.setOnClickNetworkListener
import com.ziperp.dms.extensions.setSafeClickListener
import com.ziperp.dms.main.visitcustomer.model.VisitCustomerItem
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.item_visited_customer.view.*

class VisitedCustomerAdapter(
    val context: Context,
    private val data: ArrayList<VisitCustomerItem>
) :
    RecyclerView.Adapter<VisitedCustomerAdapter.DataViewHolder>() {

    fun addData(list: List<VisitCustomerItem>) {
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun updateData(list: List<VisitCustomerItem>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    interface OnVisitCustomerClick {
        fun onItemClick(data: VisitCustomerItem)
        fun onCheckInClick(data: VisitCustomerItem)
        fun onCreateSale(data: VisitCustomerItem)
        fun onReportClick(data: VisitCustomerItem)
    }

    var onVisitCustomerClick: OnVisitCustomerClick? = null

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(data: List<VisitCustomerItem>, position: Int) {

            val item: VisitCustomerItem = data[position]
            itemView.apply {
                tv_title.text = DataConvertUtils.convertTitle(item.txtCstNm)
                tv_location.text = item.txtAddress.ifEmptyLetBe("N/A")
                tv_phone.text = item.txtPhone.ifEmptyLetBe("N/A")
                tv_owner.text = item.repPerson.ifEmptyLetBe("N/A")
                tv_visited.text = item.lastVisitedBy.ifEmptyLetBe("N/A")
                tv_sold.text = item.lastSoldBy.ifEmptyLetBe("N/A")
                tvLastVisitedTime.text = "${item.lastTimeVisit} ".ifEmptyLetBe("N/A")
                tvLastSoldTime.text = "${item.lastTimeSold} ".ifEmptyLetBe("N/A")

                if (item.isVisited == 1) {
                    customer_content.isSelected = true
                    tv_title.isSelected = true
                    checkin_information_content.visibility = View.VISIBLE
                    tv_checkin_time.text = item.visitTime
                } else {
                    customer_content.isSelected = false
                    tv_title.isSelected = false
                    checkin_information_content.visibility = View.GONE
                }
            }

            itemView.setOnClickNetworkListener {
                onVisitCustomerClick?.onItemClick(item)
            }

            itemView.tag_checkin.setSafeClickListener {
                onVisitCustomerClick?.onCheckInClick(item)
            }

            itemView.tag_sales.setOnClickListener {
                onVisitCustomerClick?.onCreateSale(item)
            }

            itemView.tag_report.setOnClickNetworkListener {
                onVisitCustomerClick?.onReportClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_visited_customer, parent,
                false
            )
        )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(data, position)
    }

    fun removeAll() {
        data.clear()
        notifyDataSetChanged()
    }

    fun sortDataByLocation() : Boolean{
        if (data.isNotEmpty()) {
            val lastLocation = LocationTrackingManager.lastLocation
            if (lastLocation != null) {
                data.forEach { customer ->
                    if (customer.posLat.isNotEmpty() && customer.posLng.isNotEmpty()) {
                        val distanceValue = AppUtil.distanceBetweenTwoPointInMeter(
                            lastLocation.latitude,
                            lastLocation.longitude,
                            customer.posLat.toDouble(),
                            customer.posLng.toDouble()
                        )
                        if(distanceValue > 0){
                            customer.distance = distanceValue.toDouble()
                        }
                    }
                }

                data.sortBy { it.distance }
                notifyDataSetChanged()
            }
        }
        return false
    }

    companion object {
        const val TITLE = 0
        const val CHECK_IN = 1
        const val SALES = 2
        const val REPORT = 3
    }

}