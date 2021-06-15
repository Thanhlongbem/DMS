package com.ziperp.dms.main.visitcustomer.model

import com.google.gson.annotations.SerializedName

data class UploadFileResponse(

	@SerializedName("path")
	val path: String = "",

	@SerializedName("pathFolder")
	val pathFolder: String = "",

	@SerializedName("name")
	val name: String = "",

	@SerializedName("contentLength")
	val contentLength: String = "",

	@SerializedName("contentType")
	val contentType: String = "",

	@SerializedName("status")
	val status: String = ""
)
