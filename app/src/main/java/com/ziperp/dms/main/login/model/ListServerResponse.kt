package com.ziperp.dms.main.login.model

import com.google.gson.annotations.SerializedName

data class ListServerResponse(

	@SerializedName("Table")
	val listServer: List<Server> = listOf()
)

data class Server(

	@SerializedName("DefDomain")
	val defDomain: Int = 0,

	@SerializedName("ServerName")
	val serverName: String = "",

	@SerializedName("Domain")
	val domain: String = ""
)
