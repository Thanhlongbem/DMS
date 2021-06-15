package com.ziperp.dms.camera


import com.google.gson.annotations.SerializedName

data class SaveToSQLFileResponse(
    @SerializedName("Table")
    val data: List<SaveToSQLFile> = listOf()
) {
    data class SaveToSQLFile(
        @SerializedName("KeyNo")
        val keyNo: String = "",
        @SerializedName("Seq")
        val seq: Int = 0,
        @SerializedName("FileURLOld")
        val fileURLOld: String = "",
        @SerializedName("FileNmOld")
        val fileNmOld: String = "",
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

    fun getSeq(): Int{
        return if(data.isNotEmpty()) {
            data[0].seq
        }else{
            -1
        }
    }
}