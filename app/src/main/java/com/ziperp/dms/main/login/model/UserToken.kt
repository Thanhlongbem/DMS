package com.ziperp.dms.main.login.model

import com.google.gson.annotations.SerializedName

data class UserToken (
    @SerializedName("access_token") val accessToken: String = "",
    @SerializedName("token_type") val tokenType: String = "",
    @SerializedName("expires_in") val expiresIn: Long = 0
)