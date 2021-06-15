package com.ziperp.dms.core.form.model

import com.google.gson.annotations.SerializedName

data class ComboDataResponse(
    @SerializedName("Table")
    val table: List<TableItem> = arrayListOf()
) {
    data class TableItem(
        @SerializedName("CmbVal")
        val cmbVal: String = "",
        @SerializedName("CmbText")
        val cmbText: String = "",
        @SerializedName("SubVal1")
        val subVal1: String = "",
        @SerializedName("SubVal2")
        val subVal2: String = "",
        @SerializedName("SubVal3")
        val subVal3: String = "",
        @SerializedName("SubVal4")
        val subVal4: String = ""
    )
}

