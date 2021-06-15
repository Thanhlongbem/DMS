package com.ziperp.dms.main.customer.customerdetail.model

import com.google.gson.annotations.SerializedName

data class CustomerDetailRequest(
	@SerializedName("CstCd")
	val cstCd: String = ""
)
