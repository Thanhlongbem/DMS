package com.ziperp.dms.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.form.model.KeyValue
import kotlinx.android.synthetic.main.item_filter.view.*
import java.util.*

class FilterDialogAdapter(val data: List<ItemControlForm>) :
    RecyclerView.Adapter<FilterDialogAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_filter, parent,
            false
        )
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    fun updateItem(itemControlForm: ItemControlForm, keyValues: ArrayList<KeyValue>) {
        val item = data.find{
            it.controlId == itemControlForm.controlId
        }
        item?.let {
            item.controlValue = keyValues
        }
        notifyDataSetChanged()
    }

    fun clearValue() {
        data.forEach{
            it.controlValue.clear()
        }
        notifyDataSetChanged()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            itemView.tvConditionNameFilter.text = data[position].controlName
            itemView.tvValueFilter.text = data[position].getDisplayText()
            when (data[position].lookUpType) {
                Form.LookUpType.NONE -> {
                    itemView.imgTypeFilter.visibility = View.INVISIBLE
                }
                Form.LookUpType.COMBO,
                Form.LookUpType.COMBO_MUL -> {
                    itemView.imgTypeFilter.setImageResource(R.drawable.ic_down_black)
                }
                Form.LookUpType.DIALOG,
                Form.LookUpType.DIALOG_MUL -> {
                    itemView.imgTypeFilter.setImageResource(R.drawable.ic_search_black)
                }
                Form.LookUpType.DATE, Form.LookUpType.MONTH -> {
                    itemView.imgTypeFilter.setImageResource(R.drawable.icon_calendar)
                }
            }
             itemView.divider.visibility = if (position == itemCount - 1) View.INVISIBLE else View.VISIBLE

            itemView.setOnClickListener { callback?.onItemFilterTypeClick(data[position], position) }

        }
    }

    interface FilterDialogCallback {
        fun onItemFilterTypeClick(data: ItemControlForm, position: Int)
    }

    var callback: FilterDialogCallback? = null


}