package com.ziperp.dms.main.trackingreports.visitcustomer.model


import com.google.gson.annotations.SerializedName

data class TrackingVSDetailRequest(
    @SerializedName("StaffId")
    var staffId: String = "",
    @SerializedName("RouteSts")
    var routeSts: String = "",
    @SerializedName("ImageSts")
    var imageSts: String = "",
    @SerializedName("OrderSts")
    var orderSts: String = "",
    @SerializedName("ValidSts")
    var validSts: String = "",
    @SerializedName("TimeTracking")
    var timeTracking: String = "",
    @SerializedName("CallFrmMobileApp")
    var callFrmMobileApp: String = "",
    @SerializedName("PageNumber")
    var pageNumber: Int = 0,
    @SerializedName("RowspPage")
    var rowspPage: Int = 0
)