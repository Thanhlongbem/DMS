package com.ziperp.dms.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.extensions.getDrawable
import kotlinx.android.synthetic.main.item_dialog_option.view.*

class DialogOptionsAdapter(val data: List<String>, var currentData: String) : RecyclerView.Adapter<DialogOptionsAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_dialog_option, parent,
            false
        )
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: DialogOptionsAdapter.Holder, position: Int) {
        holder.bind(position)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(position: Int){
            itemView.tvNameOption.text = data[position]
            itemView.cbOptionState.background = if(data[position] == currentData)R.drawable.ic_check.getDrawable() else R.drawable.ic_un_check.getDrawable()
            itemView.cbOptionState.setOnClickListener {
                onItemDialogOptionClick?.invoke(data[position], position)
                currentData = data[position]
                notifyDataSetChanged()
            }
        }
    }

    var onItemDialogOptionClick: ((String, Int) -> Unit)? = null

}