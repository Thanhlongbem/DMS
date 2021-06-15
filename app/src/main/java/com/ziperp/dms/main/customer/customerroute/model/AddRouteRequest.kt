package com.ziperp.dms.main.customer.customerroute.model


import com.google.gson.annotations.SerializedName

data class AddRouteRequest(
    @SerializedName("CstCd")
    val cstCd: String = "",
    @SerializedName("RouteId")
    val routeId: String = ""
)