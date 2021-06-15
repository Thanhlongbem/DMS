package com.ziperp.dms.main.trackingreports.salesonroute.model


import com.google.gson.annotations.SerializedName

data class SalesOnRouteResponse(
    @SerializedName("Table")
    val data: List<SalesOnRoute> = listOf()
) {
    data class SalesOnRoute(
        @SerializedName("DeptStaffNm")
        val deptStaffNm: String = "",
        @SerializedName("TotalCst")
        val totalCst: String = "",
        @SerializedName("TotalCstOrder")
        val totalCstOrder: String = "",
        @SerializedName("TotalSO")
        val totalSO: String = "",
        @SerializedName("TotalOrderQty")
        val totalOrderQty: String? = ""
    )
}