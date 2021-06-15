package com.ziperp.dms.main.customer.customerroute.model


import com.google.gson.annotations.SerializedName

data class RouteItem(
    @SerializedName("NUMBER")
    val nUMBER: Int = 0,
    @SerializedName("RouteId")
    val routeId: String = "",
    @SerializedName("RouteNo")
    val routeNo: String = "",
    @SerializedName("RouteNm")
    val routeNm: String = "",
    @SerializedName("StartDate")
    val startDate: String = "",
    @SerializedName("EndDate")
    val endDate: String = "",
    @SerializedName("RouteSts")
    val routeSts: Int = 0,
    @SerializedName("RouteStaffNm")
    val routeStaffNm: String = "",
    @SerializedName("RouteDeptNm")
    val routeDeptNm: String = ""
)
