package com.ziperp.dms.main.user.model


import com.google.gson.annotations.SerializedName

data class UpdateAvatarResponse(
    @SerializedName("Table")
    val table: List<Table> = listOf()
) {
    data class Table(
        @SerializedName("StaffId")
        val staffId: String = "",
        @SerializedName("ImgProfile")
        val imgProfile: String = "",
        @SerializedName("ErrCd")
        val errCd: String = "",
        @SerializedName("ErrMsg")
        val errMsg: String = "",
        @SerializedName("Status")
        val status: String = ""
    )
}