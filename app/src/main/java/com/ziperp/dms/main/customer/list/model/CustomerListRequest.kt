package com.ziperp.dms.main.customer.list.model


import com.google.gson.annotations.SerializedName

data class CustomerListRequest(
    @SerializedName("CstCd")
    var cstCd: String = "",
    @SerializedName("CstType")
    var cstType: String = "",
    @SerializedName("CstNm")
    var cstNm: String = "",
    @SerializedName("OwnerId")
    var ownerId: String = "",
    @SerializedName("CreateDateFr")
    var createDateFr: String = "",
    @SerializedName("CreateDateTo")
    var createDateTo: String = "",
    @SerializedName("ViewSts")
    var viewSts: String = "",
    @SerializedName("CstGrp1")
    var cstGrp1: String = "",
    @SerializedName("CstGrp2")
    var cstGrp2: String = "",
    @SerializedName("CstGrp3")
    var cstGrp3: String = "",
    @SerializedName("CstGrp4")
    var cstGrp4: String = "",
    @SerializedName("PageNumber")
    var pageNumber: Int = 1,
    @SerializedName("RowspPage")
    var rowspPage: Int = 50
)