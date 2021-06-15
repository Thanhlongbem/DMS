package com.ziperp.dms.main.saleorder.model


import com.google.gson.annotations.SerializedName

data class ConfirmSaleOderRequest(
    @SerializedName("GridKey")
    val gridKey: String = "",
    @SerializedName("ControlId")
    val controlId: String = "",
    @SerializedName("LinkPara")
    val linkPara: String = "",
    @SerializedName("StaffId")
    val staffId: String = "",
    @SerializedName("LinkFormId")
    val linkFormId: String = "",
    @SerializedName("CheckVal")
    val checkVal: Int = 0
)