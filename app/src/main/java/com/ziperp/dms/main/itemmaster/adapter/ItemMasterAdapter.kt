package com.ziperp.dms.main.itemmaster.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.setOnClickNetworkListener
import com.ziperp.dms.main.itemmaster.model.ItemMasterResponse
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.item_master.view.*

class ItemMasterAdapter : BaseAdapter<ItemMasterResponse.ItemMaster>() {

    var onStockQTYClick: ((ItemMasterResponse.ItemMaster) -> Unit)? = null

    override fun getLayoutId(): Int = R.layout.item_master

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<ItemMasterResponse.ItemMaster> {
        override fun bind(item: ItemMasterResponse.ItemMaster) {
            itemView.apply {
                tvNameItemMaster.text = "${item.txtItemNo} - ${item.txtItemNm}"
                tv_itemCategory.text = DataConvertUtils.convertTitle(item.txtItemCate)
                tv_itemBrand.text = DataConvertUtils.convertTitle(item.txtBrand)
                tv_vendorName.text = DataConvertUtils.convertTitle(item.txtDefVendorCd)
                tv_stockUnit.text = item.txtWhUnitCd
                tv_salesUnit.text = item.txtSalesUnitCd
                tv_convQuantity.text = item.fltConvStkQtyPur.format()
                tv_salesPrice.text = item.defSellPrice.format()
                tv_vatRate.text = "${item.vatRate.format()}%"
                tv_originCountry.text = item.txtCO
                tv_itemSpec.text = item.txtItemSpec

                btn_StockQty.setOnClickNetworkListener {
                    onStockQTYClick?.invoke(item)
                }
            }
        }
    }



}