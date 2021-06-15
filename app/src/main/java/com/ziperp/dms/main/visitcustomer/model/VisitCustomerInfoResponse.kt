package com.ziperp.dms.main.visitcustomer.model

import com.google.gson.annotations.SerializedName

data class VisitCustomerInfoResponse(

	@SerializedName("Table")
	val visitCustomerInfo: List<VisitCustomerInfo> = arrayListOf()
){
	data class VisitCustomerInfo(

		@SerializedName("CstVisitNo")
		val cstVisitNo: String = "",

		@SerializedName("Debit")
		val debit: Double = 0.0,

		@SerializedName("CheckOutTime")
		val checkOutTime: String = "",

		@SerializedName("StaffNm")
		val staffNm: String = "",

		@SerializedName("CstNo")
		val cstNo: String = "",

		@SerializedName("Email")
		val email: String = "",

		@SerializedName("CheckOutDay")
		val checkOutDay: String = "",

		@SerializedName("CstNm")
		val cstNm: String = "",

		@SerializedName("StaffId")
		val staffId: String = "",

		@SerializedName("VisitPosNm")
		val visitPosNm: String = "",

		@SerializedName("VisitSts")
		val visitSts: Int? = 0,

		@SerializedName("CstPhone")
		val cstPhone: String = "",

		@SerializedName("Remark")
		val remark: String = "",

		@SerializedName("VisitLngPos")
		val visitLngPos: String = "",

		@SerializedName("Representative")
		val representative: String = "",

		@SerializedName("VisitResult")
		val visitResult: Int? = 0,

		@SerializedName("Limit")
		val limit: Double = 0.0,

		@SerializedName("CheckInDay")
		val checkInDay: String = "",

		@SerializedName("VisitLatPos")
		val visitLatPos: String = "",

		@SerializedName("CheckInTime")
		val checkInTime: String = "",

		@SerializedName("OfficeAddr")
		val officeAddr: String = ""
	)
}


