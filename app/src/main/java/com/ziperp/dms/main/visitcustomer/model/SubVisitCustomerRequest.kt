package com.ziperp.dms.main.visitcustomer.model

import com.google.gson.annotations.SerializedName

data class SubVisitCustomerRequest(

    @SerializedName("CstInfo")
    var cstInfo: String = "",

    @SerializedName("Route")
    var route: String = "",

    @SerializedName("VisitStatus")
    var visitStatus: String = "",

    @SerializedName("VisitDay")
    var visitDay: String = "",

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
