package com.ziperp.dms.main.saleorder.model


import com.google.gson.annotations.SerializedName

data class CUDSalesOrderResponse(
    @SerializedName("Table")
    val status: List<CUDStatus> = listOf()
) {
    data class CUDStatus(
        @SerializedName("OrderNo")
        val orderNo: String = "",
        @SerializedName("OrderMngtNo")
        val orderMngtNo: String = "",
        @SerializedName("ErrCd")
        val errCd: String = "",
        @SerializedName("ErrMsg")
        val errMsg: String = "",
        @SerializedName("Status")
        val status: String = ""
    )

    fun isSuccess(): Boolean{
        return status.isNotEmpty() && status[0].status == "OK"
    }

    fun message(): String{
        return if(status.isNotEmpty()){
            "${status[0].errCd} - ${status[0].errMsg}"
        }else{
            ""
        }
    }

    fun getOrderNo(): String{
        return if(status.isNotEmpty()) {
            status[0].orderNo
        }else{
            ""
        }
    }
}