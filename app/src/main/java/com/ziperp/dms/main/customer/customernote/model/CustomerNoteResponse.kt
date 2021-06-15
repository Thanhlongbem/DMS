package com.ziperp.dms.main.customer.customernote.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CustomerNoteResponse(
    @SerializedName("Table")
    var data: List<CustomerNote> = listOf()
) {
    data class CustomerNote(
        @SerializedName("NoteId")
        var noteId: String = "",
        @SerializedName("NoteContent")
        var noteContent: String = "",
        @SerializedName("NoteTitle")
        var noteTitle: String = "",
        @SerializedName("RegDate")
        var regDate: String = "",
        @SerializedName("RegMan")
        var regMan: String = "",
        @SerializedName("UptMan")
        var uptMan: String = "",
        @SerializedName("ObjectId")
        var objectId: String = "",
        @SerializedName("ObjectType")
        var objectType: Int = 0,
        @SerializedName("GridKey")
        var gridKey: String = "",
        @SerializedName("LinkFormId")
        var linkFormId: String = "",
        @SerializedName("LinkPara")
        var linkPara: String = ""
    ): Serializable
}