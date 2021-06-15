package com.ziperp.dms.main.itemmaster.model

import com.google.gson.annotations.SerializedName
data class StockQtyRequest(
    @SerializedName("ItemCd")
    var itemCd: String = ""
)