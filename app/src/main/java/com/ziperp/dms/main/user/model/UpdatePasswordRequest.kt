package com.ziperp.dms.main.user.model


import com.google.gson.annotations.SerializedName

data class UpdatePasswordRequest(
    @SerializedName("OldPassword")
    val oldPassword: String = "",
    @SerializedName("NewPassword")
    val newPassword: String = "",
    @SerializedName("ConfirmPassword")
    val confirmPassword: String = ""
)