package com.ziperp.dms.core.form.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class DialogDataResponse(
    @SerializedName("Table")
    val table: List<JsonObject> = arrayListOf(),
    @SerializedName("Table1")
    val record: List<Record> = arrayListOf()
) {
    data class Record(
        @SerializedName("TotalResults")
        val totalRecords: Int = 0
    )
}

