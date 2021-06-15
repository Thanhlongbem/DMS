package com.ziperp.dms.main.itemmaster.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ItemMasterImageResponse(

	@SerializedName("Table")
	val data: List<DataImage> = arrayListOf()
) {
	data class DataImage(
		@SerializedName("FileURL")
		val fileURL: String = "",

		@SerializedName("ItemCd")
		val itemCd: String = "",

		@SerializedName("PhotoFileNmOld")
		val photoFileNmOld: String = "",

		@SerializedName("FileURLOld")
		val fileURLOld: String = "",

		@SerializedName("PhotoNm")
		val photoNm: String = "",

		@SerializedName("Download")
		val download: String = "",

		@SerializedName("Seq")
		val seq: Int = 0,

		@SerializedName("PhotoFileNm")
		val photoFileNm: String = "",

		@SerializedName("UseYn")
		val useYn: Int = 0
	) : Serializable
}


