package com.ziperp.dms.core.form.model

import com.google.gson.annotations.SerializedName

data class ComboMetaResponse(
    @SerializedName("Table")
    val queries: List<Query> = arrayListOf(),
    @SerializedName("Table1")
    val params: List<Param> = arrayListOf()

) {
    data class Query(
        @SerializedName("CmbQryStr")
        val cmbQryStr: String = ""
    )

    data class Param(
        @SerializedName("Item1")
        val item1: String = "",
        @SerializedName("Item2")
        val item2: String = "",
        @SerializedName("Item3")
        val item3: String = "",
        @SerializedName("Item4")
        val item4: String = "",
        @SerializedName("Item5")
        val item5: String = "",
        @SerializedName("Item6")
        val item6: String = "",
        @SerializedName("Item7")
        val item7: String = "",
        @SerializedName("Item8")
        val item8: String = "",
        @SerializedName("Item9")
        val item9: String = "",
        @SerializedName("Item10")
        val item10: String = "",
        @SerializedName("ParentCd")
        val parentCd: String = "",
        @SerializedName("DlgCd")
        val dlgCd: String = "",
        @SerializedName("JoinCd")
        val joinCd: String = "",
        @SerializedName("CmbCd")
        val cmbCd: String = ""
    )


}


