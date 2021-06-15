package com.ziperp.dms.main.visitcustomer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.main.visitcustomer.model.PresenterModel

enum class ItemType {
    SIMPLE_TEXT,
    STICKY
}

class AllVideosAdapter (val data: MutableList<PresenterModel>) : RecyclerView.Adapter<AllVideosAdapter.ContentHolder>(){

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int) =
        if (data[position].isSticky) {
            STICKY_ITEM_TYPE
        } else {
            TEXT_ITEM_TYPE
        }



    override fun onBindViewHolder(holder: ContentHolder, position: Int) {
        holder.itemView.isClickable = true

        holder.tvVideoname.text = data[position].introTitle

        holder.layoutContent.setOnClickListener {
            if(onItemPresenterClick != null){
                onItemPresenterClick?.onItemPresenterClick(data[position].link)
            }
        }

        if(data[position].isSticky){
            holder.tvHeadercontent.visibility = View.VISIBLE
        } else {
            holder.tvHeadercontent.visibility = View.GONE
        }
    }

    open inner class ContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvVideoname: TextView = itemView.findViewById(R.id.tv_videoName)
        val tvHeadercontent: TextView = itemView.findViewById(R.id.tv_headerContent)
        val layoutContent: ConstraintLayout = itemView.findViewById(R.id.layoutContent)
    }

    companion object {
        val STICKY_ITEM_TYPE = ItemType.STICKY.ordinal

        val TEXT_ITEM_TYPE = ItemType.SIMPLE_TEXT.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return ContentHolder(layoutInflater.inflate(R.layout.item_all_video, parent, false))
    }

    interface OnItemPresenterClick{
        fun onItemPresenterClick (url: String)
    }

    var onItemPresenterClick: OnItemPresenterClick ?= null

}