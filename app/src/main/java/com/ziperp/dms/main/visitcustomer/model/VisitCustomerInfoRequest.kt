package com.ziperp.dms.main.visitcustomer.model

import com.google.gson.annotations.SerializedName

data class VisitCustomerInfoRequest (
    @SerializedName("CstVisitNo")
    var cstVisitNo: String = ""
)