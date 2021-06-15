package com.ziperp.dms.main.visitcustomer.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VisitCustomerResponse(

	@SerializedName("Table")
	val data: List<VisitCustomerItem> = arrayListOf(),

	@SerializedName("Table1")
	val record: List<Record> = arrayListOf()
) {

	data class Record(

		@SerializedName("TotalRecords")
		val totalRecords: Double = 0.0
	)

}

data class VisitCustomerItem(

	@SerializedName("GridKey")
	var cstCd: String = "",

	@SerializedName("PosLng")
	val posLng: String = "",

	@SerializedName("LastSoldBy")
	val lastSoldBy: String = "",

	@SerializedName("txtFax")
	val txtFax: String = "",

	@SerializedName("Remark")
	var remark: String = "",

	@SerializedName("txtAddress")
	val txtAddress: String = "",

	@SerializedName("txtCstNo")
	val txtCstNo: String = "",

	@SerializedName("LinkFormId")
	val linkFormId: String = "",

	@SerializedName("LinkPara")
	val linkPara: String = "",

	@SerializedName("txtWebsite")
	val txtWebsite: String = "",

	@SerializedName("PosLat")
	val posLat: String = "",

	@SerializedName("txtCstNm")
	val txtCstNm: String = "",

	@SerializedName("txtPhone")
	val txtPhone: String = "",

	@SerializedName("txtCity")
	val txtCity: String = "",

	@SerializedName("VisitTime")
	val visitTime: String = "",

	@SerializedName("RegDate")
	val regDate: String = "",

	@SerializedName("txtEmail")
	val txtEmail: String = "",

	@SerializedName("RegMan")
	val regMan: String = "",

	@SerializedName("UptMan")
	val uptMan: String = "",

	@SerializedName("txtProspectType")
	val txtProspectType: String = "",

	@SerializedName("txtCstOwner")
	val txtCstOwner: String = "",

	@SerializedName("UptDate")
	val uptDate: String = "",

	@SerializedName("txtCountry")
	val txtCountry: String = "",

	@SerializedName("IsVisited")
	val isVisited: Int? = 0,

	@SerializedName("LastVisitedBy")
	val lastVisitedBy: String = "",

	@SerializedName("RepPerson")
	val repPerson: String = "",

	@SerializedName("LastTimeSold")
	val lastTimeSold: String = "",

	@SerializedName("txtCstDiv")
	val txtCstDiv: String = "",

	@SerializedName("txtDistrict")
	val txtDistrict: String = "",

	@SerializedName("LastTimeVisit")
	val lastTimeVisit: String = "",

	@SerializedName("Distance")
	var distance: Double = 0.0



) : Serializable, Comparable<VisitCustomerItem> {
	override fun compareTo(other: VisitCustomerItem): Int {
		return compareValues(other.distance, distance)
	}

	fun isValidCustomerCode(): Boolean{
		return cstCd.isNotBlank() && !cstCd.contains("Cached_")
	}

}
