package com.ziperp.dms.main.itemmaster.model

import com.google.gson.annotations.SerializedName

data class ItemMasterDetailRequest(
	@SerializedName("ItemCd")
	val itemCd: String = ""
)
