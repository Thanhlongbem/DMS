package com.ziperp.dms.main.visitcustomer.model

import com.google.gson.annotations.SerializedName

data class SaveVisitCustomerInfoResponse(

	@SerializedName("Table")
	val table: List<TableItem> = arrayListOf()
){
	data class TableItem(

		@SerializedName("Status")
		val status: String? = "",

		@SerializedName("CstVisitNo")
		val cstVisitNo: String = "",

		@SerializedName("ErrMsg")
		val errMsg: String? = "",

		@SerializedName("ErrCd")
		val errCd: String? = ""
	)

	fun isSuccess(): Boolean{
		return table.isNotEmpty() && table[0].status == "OK"
	}

	fun message(): String{
		return if(table.isNotEmpty()){
			"${table[0].errCd} - ${table[0].errMsg}"
		}else{
			""
		}
	}

	fun getCstVisitNo(): String{
		return if(table.isNotEmpty()) {
			table[0].cstVisitNo
		}else{
			""
		}
	}
}


