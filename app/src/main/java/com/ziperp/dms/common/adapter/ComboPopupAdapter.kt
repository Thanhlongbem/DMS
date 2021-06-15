package com.ziperp.dms.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import kotlinx.android.synthetic.main.item_combo_popup.view.*

class ComboPopupAdapter(val datas: MutableList<String>, var currentData: String) : RecyclerView.Adapter<ComboPopupAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_combo_popup, parent,
            false
        )
    )

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(position: Int){
            itemView.imgChecked.visibility = if(datas[position] == currentData)View.VISIBLE else View.GONE
            itemView.tvNameComboItem.isSelected = datas[position] == currentData
            itemView.tvNameComboItem.text = datas[position]
            itemView.clItemCombo.setOnClickListener {
                onItemComboClick?.invoke(datas[position], position)
            }
        }
    }


    var onItemComboClick: ((String, Int) -> Unit)? = null
}