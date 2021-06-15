package com.ziperp.dms.main.itemmaster.model

import com.google.gson.annotations.SerializedName

data class ItemMasterRequest(
    @SerializedName("ItemInfo")
    var itemInfo: String = "",
    @SerializedName("ItemCate")
    var itemCate: String = "",
    @SerializedName("ItemGrp")
    var itemGrp: String = "",
    @SerializedName("ItemBrand")
    var itemBrand: String = "",
    @SerializedName("ItemModel")
    var itemModel: String = "",
    @SerializedName("Vendor")
    var vendor: String = "",
    @SerializedName("Maker")
    var maker: String = "",
    @SerializedName("PageNumber")
    var pageNumber: Int = 0,
    @SerializedName("RowspPage")
    var rowspPage: Int = 0
)
