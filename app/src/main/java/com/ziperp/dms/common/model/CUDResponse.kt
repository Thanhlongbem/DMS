package com.ziperp.dms.common.model


import com.google.gson.annotations.SerializedName

data class CUDResponse(
    @SerializedName("Table")
    val data: List<Table> = listOf()
) {
    data class Table(
        @SerializedName("ErrCd")
        val errCd: String = "",
        @SerializedName("ErrMsg")
        val errMsg: String = "",
        @SerializedName("Status")
        val status: String = ""
    )

    fun isSuccess(): Boolean{
        return data.isNotEmpty() && data[0].status == "OK"
    }

    fun message(): String{
        return if(data.isNotEmpty()){
            "${data[0].errCd} - ${data[0].errMsg}"
        }else{
            ""
        }
    }


}