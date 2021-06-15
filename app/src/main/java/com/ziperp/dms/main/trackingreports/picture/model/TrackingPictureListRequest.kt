package com.ziperp.dms.main.trackingreports.picture.model


import com.google.gson.annotations.SerializedName

data class TrackingPictureListRequest(
    @SerializedName("SearchInfo")
    var searchInfo: String = "",
    @SerializedName("DeptCd")
    var deptCd: String = "",
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