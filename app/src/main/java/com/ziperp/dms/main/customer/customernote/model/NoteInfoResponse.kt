package com.ziperp.dms.main.customer.customernote.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NoteInfoResponse(
    @SerializedName("Table")
    var data: List<NoteInfo> = listOf()
) {
    data class NoteInfo(
        @SerializedName("NoteId")
        var noteId: String = "",
        @SerializedName("NoteTitle")
        var noteTitle: String = "",
        @SerializedName("NoteContent")
        var noteContent: String = "",
        @SerializedName("ObjectType")
        var objectType: Int = 0,
        @SerializedName("ObjectId")
        var objectId: String = "",
        @SerializedName("ObjectNm")
        var objectNm: String = "",
        @SerializedName("RegDate")
        var regDate: String = "",
        @SerializedName("RegMan")
        var regMan: String = "",
        @SerializedName("UptDate")
        var uptDate: String = "",
        @SerializedName("UptMan")
        var uptMan: String = ""
    ): Serializable
}