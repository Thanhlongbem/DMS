package com.ziperp.dms.main.visitcustomer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.camera.CustomerImage
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.reformatDateTime
import com.ziperp.dms.utils.AppUtil
import kotlinx.android.synthetic.main.item_customer_image_grid.view.*
import kotlinx.android.synthetic.main.item_customer_image_list.view.*

class ImageAdapter(var data: ArrayList<CustomerImage>, var isListMode: Boolean) : RecyclerView.Adapter<ImageAdapter.DataViewHolder>(){

    var context: Context? = null
    lateinit var onImageClickListener: (Int) -> Unit
    var onDelete: ((position: Int) -> Unit)? = null

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ArrayList<CustomerImage>, position: Int){
            val item: CustomerImage = data[position]
            itemView.apply {
                tv_fileLength?.text = item.fileLength
                tv_NameLinear?.text = item.fileNm
                tv_nameGrid?.text = item.fileNm
                tv_TimeLinear?.text = item.attachedDate.reformatDateTime()
                tv_TimeGrid?.text = item.attachedDate.reformatDateTime()

                img_no_synchronized_gid?.visibility = if(item.isSynchonized) View.GONE else View.VISIBLE
                img_no_synchronized?.visibility = if(item.isSynchonized) View.GONE else View.VISIBLE

                    if(item.download.isNotBlank()){
                        AppUtil.loadImageFromURL(
                            context, Constants.API_BASE_PATH + item.download,
                            if (imgLinear != null) imgLinear else imgGrid, enableCache = true
                        )
                    } else if(!item.isSynchonized && item.localPath.isNotBlank()){
                        AppUtil.loadImageFromLocal(
                            context, item.localPath,
                            if (imgLinear != null) imgLinear else imgGrid
                        )
                    }

            }
            itemView.setOnClickListener { onImageClickListener.invoke(position) }
            if (isListMode) itemView.btn_delete.setOnClickListener {
                AppUtil.showAlertDialog(context, R.string.str_question_delete.getString(), {
                    onDelete?.invoke(position)
                }, {}) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.DataViewHolder {
        context = parent.context
        return if(isListMode){
            DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_customer_image_list, parent, false))
        } else {
            DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_customer_image_grid, parent, false))
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ImageAdapter.DataViewHolder, position: Int) {
        holder.bind(data, position)
    }

    fun switchViewMode(mode: Boolean){
        isListMode = mode
        notifyDataSetChanged()
    }

    fun addData(list: List<CustomerImage>){
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

}