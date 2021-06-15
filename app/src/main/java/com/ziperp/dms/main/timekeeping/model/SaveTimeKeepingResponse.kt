package com.ziperp.dms.main.timekeeping.model


import com.google.gson.annotations.SerializedName

data class SaveTimeKeepingResponse(
    @SerializedName("Table")
    val data: List<State> = listOf()
) {
    data class State(
        @SerializedName("TimeKeepNo")
        val timeKeepNo: String = "",
        @SerializedName("ErrCd")
        val errCd: String = "",
        @SerializedName("ErrMsg")
        val errMsg: String = "",
        @SerializedName("Status")
        val status: String = ""
    )

    fun isSuccess(): Boolean{
        return data.isNotEmpty() && data[0].status == "OK" && data[0].timeKeepNo.isNotBlank()
    }
}