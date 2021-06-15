package com.ziperp.dms.main.itemmaster.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.itemmaster.model.ItemMasterImageResponse
import com.ziperp.dms.utils.AppUtil
import kotlinx.android.synthetic.main.item_customer_image_grid.view.*
import kotlinx.android.synthetic.main.item_customer_image_list.view.*

class ImageAdapter(var data: ArrayList<ItemMasterImageResponse.DataImage>, var viewMode: Boolean) :  RecyclerView.Adapter<ImageAdapter.DataViewHolder>() {

    lateinit var onImageClicked: ((position: Int) -> Unit)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(data: MutableList<ItemMasterImageResponse.DataImage>, position: Int){
            val item : ItemMasterImageResponse.DataImage = data[position]
            itemView.apply {
                tv_NameLinear?.text = item.photoFileNm
                tv_nameGrid?.text = item.photoFileNm

                AppUtil.loadImageFromURL(context, Constants.API_BASE_PATH + item.download, if(imgLinear != null) imgLinear else imgGrid)
                setOnClickListener { onImageClicked.invoke(position) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.DataViewHolder {
        return DataViewHolder(LayoutInflater.from(parent.context)
            .inflate(
                if (viewMode) R.layout.item_customer_image_list
                else R.layout.item_customer_image_grid
                , parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ImageAdapter.DataViewHolder, position: Int) {
        holder.bind(data , position)
    }

    fun addData(list: List<ItemMasterImageResponse.DataImage>){
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

}