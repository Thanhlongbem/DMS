package com.ziperp.dms.main.home

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.common.model.HomeItem
import com.ziperp.dms.extensions.setOnClickNetworkListener
import kotlinx.android.synthetic.main.item_home.view.*

class HomeAdapter() :
    RecyclerView.Adapter<HomeAdapter.DataViewHolder>() {

    private var homeItems = ArrayList<HomeItem>()
    var onClickListener: ((position: Int) -> Unit)? = null

    init {
        homeItems.add(HomeItem(R.string.str_visit_customer, R.drawable.visit_customer))
        homeItems.add(HomeItem(R.string.str_sales_order, R.drawable.sale_order))
        homeItems.add(HomeItem(R.string.str_time_keeping, R.drawable.time_keeping))
        homeItems.add(HomeItem(R.string.str_item_master, R.drawable.item_master))
        homeItems.add(HomeItem(R.string.str_sale_price_promotion, R.drawable.price))
        homeItems.add(HomeItem(R.string.str_customer, R.drawable.customer))
        homeItems.add(HomeItem(R.string.str_tracking_report, R.drawable.tracking))
        homeItems.add(HomeItem(R.string.str_personal_report, R.drawable.report))
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: HomeItem) {
            itemView.title.text = itemView.context.getString(data.title)
            itemView.image.setImageResource(data.image)
            itemView.image.setColorFilter(Color.parseColor("#0B65AD"), PorterDuff.Mode.SRC_ATOP)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_home, parent,
                false
            )
        )

    override fun getItemCount(): Int = homeItems.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(homeItems[position])
        if (position < 6) {
            holder.itemView.setOnClickNetworkListener { onClickListener?.invoke(position) }
        } else {
            holder.itemView.setOnClickListener { onClickListener?.invoke(position) }
        }

    }

}