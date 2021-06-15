package com.ziperp.dms.main.login.model

import com.google.gson.annotations.SerializedName

class UserInfoRequest(
    @SerializedName("pUserId")
    val pUserId: String = ""
)