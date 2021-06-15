package com.ziperp.dms.main.visitcustomer.model

import com.google.gson.annotations.SerializedName

data class GetFileOfVisitCustomerRequest (
    @SerializedName("KeyNo")
    var keyNo: String = ""
)