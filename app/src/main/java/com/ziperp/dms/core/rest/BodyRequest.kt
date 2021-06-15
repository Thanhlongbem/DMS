package com.ziperp.dms.core.rest

import com.google.gson.annotations.SerializedName

data class BodyRequest(

    @SerializedName("pSPName")
    var pSPName: String = "",

    @SerializedName("pUserId")
    var pUserId: String = "",

    @SerializedName("pLangId")
    var pLangId: String = "",

    @SerializedName("pConnStr")
    var pConnStr: String = "",

    @SerializedName("pFormId")
    var pFormId: String = "",

    @SerializedName("pPageMode")
    var pPageMode: String = "",

    @SerializedName("pJSONData")
    var pJSONData: String = ""

)