package com.ziperp.dms.main.trackingreports.salesresult.view

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.extensions.format
import com.ziperp.dms.main.trackingreports.salesresult.model.GroupSalesDetail
import com.ziperp.dms.utils.AppUtil
import kotlinx.android.synthetic.main.item_detail_sales_result.view.*

class DetailAdapter: BaseAdapter<GroupSalesDetail>() {
    override fun getLayoutId(): Int = R.layout.item_detail_sales_result

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<GroupSalesDetail> {
        override fun bind(item: GroupSalesDetail) {
            itemView.apply {

                AppUtil.loadImageFromURL(context, Constants.API_BASE_PATH + item.listItems[0].imgProfile, img_avatar, defaultImg = R.drawable.avata_default)
                tv_staff_name.text = item.listItems[0].staffNm

                val adapter = DetailItemsAdapter()
                adapter.updateData(item.listItems)
                recycler_view.layoutManager = LinearLayoutManager(context)
                recycler_view.adapter = adapter
                recycler_view.setHasFixedSize(true)

                tv_quantity.text = ((item.listItems.map { item -> item.quantity }).reduce { sum, e -> sum + e }).format()
                tv_amount.text = ((item.listItems.map { item -> item.totAmount }).reduce { sum, e -> sum + e }).format()
            }
        }
    }
}