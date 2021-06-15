package com.ziperp.dms.main.trackingreports.visitcustomer.model


import com.google.gson.annotations.SerializedName

data class TrackingVSListRequest(
    @SerializedName("SearchInfo")
    var searchInfo: String = "",
    @SerializedName("DeptCd")
    var deptCd: String = "",
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
    @SerializedName("StrMasterLocCd")
    var strMasterLocCd: String = "",
    @SerializedName("CallFrmMobileApp")
    var callFrmMobileApp: String = "",
    @SerializedName("PageNumber")
    var pageNumber: Int = 0,
    @SerializedName("RowspPage")
    var rowspPage: Int = 0
)