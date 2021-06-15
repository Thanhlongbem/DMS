package com.ziperp.dms.main.customer.customerroute.model


import com.google.gson.annotations.SerializedName

data class AddRouteResponse(
    @SerializedName("Table")
    val data: List<Status> = listOf()
) {
    data class Status(
        @SerializedName("RouteId")
        val routeId: String = "",
        @SerializedName("CstCd")
        val cstCd: String = "",
        @SerializedName("ErrCd")
        val errCd: String = "",
        @SerializedName("ErrMsg")
        val errMsg: String = "",
        @SerializedName("Status")
        val status: String = ""
    )

    fun isSuccess(): Boolean{
        return data.isNotEmpty() && data[0].status == "OK"
    }

    fun message(): String{
        return if(data.isNotEmpty()){
            "${data[0].errCd} - ${data[0].errMsg}"
        }else{
            ""
        }
    }

}