package com.ziperp.dms.main.visitcustomer.model

import com.google.gson.annotations.SerializedName

data class PresenterModel(
    @SerializedName("IntroTitle")
    val introTitle: String = "",

    @SerializedName("Link")
    val link: String = "",

    @SerializedName("Type")
    val type: String = "",

    @SerializedName("IsSticky")
    var isSticky: Boolean = false

): Comparable<PresenterModel> {
    override fun compareTo(other: PresenterModel): Int {
        return compareValues(other.introTitle, introTitle)
    }
}