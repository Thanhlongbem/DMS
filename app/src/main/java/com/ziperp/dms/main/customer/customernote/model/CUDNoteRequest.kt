package com.ziperp.dms.main.customer.customernote.model


import com.google.gson.annotations.SerializedName

data class CUDNoteRequest(
    @SerializedName("NoteId")
    var noteId: String = "",
    @SerializedName("ObjectId")
    var objectId: String = "",
    @SerializedName("ObjectType")
    var objectType: String = "",
    @SerializedName("NoteTitle")
    var noteTitle: String = "",
    @SerializedName("NoteContent")
    var noteContent: String = ""
)