package com.ziperp.dms.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.ziperp.dms.R
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.form.model.KeyValue
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.main.visitcustomer.model.StockItem
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.item_dialog_popup.view.*
import kotlinx.android.synthetic.main.item_dialog_popup.view.checkbox
import kotlinx.android.synthetic.main.item_dialog_route.view.*
import kotlinx.android.synthetic.main.item_master.view.*
import java.util.*

class DialogPopupAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: ArrayList<KeyValue> = arrayListOf()
    var selectedData: ArrayList<KeyValue> = arrayListOf()
    var lookUpType: Int = Form.LookUpType.NONE
    var isMultiple: Boolean = false
        get() = lookUpType == Form.LookUpType.DIALOG_MUL || lookUpType == Form.LookUpType.COMBO_MUL


    var type = ItemControlForm.TYPE_COMMON

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (type) {
            ItemControlForm.TYPE_ITEM_COUNT -> {
                ItemCountHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_master, parent, false))
            }
            ItemControlForm.TYPE_ROUTE -> {
                RouteHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_dialog_route, parent, false))
            }
            else -> {
                CommonHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_dialog_popup, parent, false))
            }
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (type) {
            ItemControlForm.TYPE_COMMON -> {
                (holder as CommonHolder).bind(position)
            }
            ItemControlForm.TYPE_ITEM_COUNT -> {
                (holder as ItemCountHolder).bind(position)
            }
            ItemControlForm.TYPE_ROUTE -> {
                (holder as RouteHolder).bind(position)
            }
        }
    }

    //Basic Item
    inner class CommonHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            itemView.apply {
                val isSelected = isSelected(position)
                checkbox.isChecked = isSelected
                checkbox.isClickable = false
                tvNameComboItem.text = data[position].valueName
                clItemCombo.setOnClickListener {
                    onItemClicked?.invoke(data[position], position, isSelected)
                }
            }
        }
    }
    //For Item Count
    inner class ItemCountHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            itemView.apply {
                val isSelected = isSelected(position)
                checkbox.visibility = View.VISIBLE
                checkbox.isChecked = isSelected
                checkbox.isClickable = false

                val item = Gson().fromJson(data[position].jsonData, StockItem::class.java) as? StockItem
                item?.let {
                    tvNameItemMaster.text = "${item.itemNo} - ${item.itemNm}"
                    tv_itemCategory.text = DataConvertUtils.convertTitle(item.itemCategoryNm)
                    tv_itemBrand.text = DataConvertUtils.convertTitle(item.itemBrandNm)
                    tv_vendorName.text = DataConvertUtils.convertTitle(item.vendorNm)
                    tv_stockUnit.text = item.wHUnitNm
                    tv_salesUnit.text = item.uoMNm
                    tv_convQuantity.text =item.convStkQty.format()
                    tv_salesPrice.text = item.sellVATinPriceYn.toString()
                    tv_vatRate.text = "${item.vATRate.toInt()}%"
                    tv_originCountry.text = item.originCountryNm
                    tv_itemSpec.text = item.itemSpec
                }
                itemView.setOnClickListener {
                    onItemClicked?.invoke(data[position], position, isSelected)
                }
            }
        }
    }

    //For Route select
    inner class RouteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            itemView.apply {
                val isSelected = isSelected(position)
                checkbox.isChecked = isSelected
                checkbox.isClickable = false

                val jsonObject = JsonParser().parse(data[position].jsonData) as? JsonObject
                if(jsonObject != null){
                    tv_route_name.text = jsonObject.get("RouteNo").asString
                    tv_owner.text = String.format(R.string.str_owner_name.getString(), jsonObject.get("RouteStaffNm").asString)
                } else {
                    tv_route_name.text = data[position].valueName
                    tv_owner.text = String.format(R.string.str_owner_name.getString(),data[position].valueName)
                }
                layout_route.setOnClickListener {
                    onItemClicked?.invoke(data[position], position, isSelected)
                }
            }
        }
    }

    fun isSelected(position: Int): Boolean {
        return selectedData.find { it.valueCode == data[position].valueCode } != null
    }


    fun addData(list: List<KeyValue>) {
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun selectedItem(position: Int) {
        if (isMultiple) {
            selectedData.add(data[position])
        } else {
            selectedData.clear()
            selectedData.add(data[position])
        }
        notifyDataSetChanged()
    }

    fun removeSelectedItem(position: Int) {
        if (isMultiple) {
            selectedData.remove(data[position])
        } else {
            selectedData.clear()
        }
        notifyDataSetChanged()
    }

    fun updateType(control: ItemControlForm) {
        type = control.getType()
    }

    fun removeAll() {
        data.clear()
        notifyDataSetChanged()
    }

    var onItemClicked: ((KeyValue, Int, Boolean) -> Unit)? = null


}