package com.ziperp.dms.main.customer.customerdetail.model


import com.google.gson.annotations.SerializedName

data class CUDCustomerResponse(
    @SerializedName("Table")
    var status: List<CUDCustomer> = arrayListOf()
) {
    data class CUDCustomer(
        @SerializedName("CstCd")
        val cstCd: String = "",
        @SerializedName("CstNo")
        val cstNo: String = "",
        @SerializedName("ErrCd")
        val errCd: String = "",
        @SerializedName("ErrMsg")
        val errMsg: String = "",
        @SerializedName("Status")
        var status: String = ""
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

    fun getCstCd(): String{
        return if(status.isNotEmpty()) {
            status[0].cstCd
        }else{
            ""
        }
    }
}