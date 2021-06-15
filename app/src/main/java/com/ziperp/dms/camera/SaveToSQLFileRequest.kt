package com.ziperp.dms.camera


import com.google.gson.annotations.SerializedName
import com.ziperp.dms.core.token.DmsUserManager

data class SaveToSQLFileRequest(
    @SerializedName("pPageMode")
    var pPageMode: String = "",
    @SerializedName("pSeq")
    var pSeq: String = "",
    @SerializedName("pKeyNo")
    var pKeyNo: String = "",
    @SerializedName("pFileNote")
    var pFileNote: String = "",
    @SerializedName("pFileType")
    var pFileType: String = "",
    @SerializedName("pFileNm")
    var pFileNm: String = "",
    @SerializedName("pFileNmOld")
    var pFileNmOld: String = "",
    @SerializedName("pFileURL")
    var pFileURL: String = "",
    @SerializedName("pFileURLOld")
    var pFileURLOld: String = "",
    @SerializedName("pIsSaveAfterUpload")
    var pIsSaveAfterUpload: String = "1",
    @SerializedName("pFormId")
    var pFormId: String = "",
    @SerializedName("pUserId")
    var pUserId: String = DmsUserManager.userInfo.userId,
    @SerializedName("pLangId")
    var pLangId: String = "0",
    @SerializedName("pConnStr")
    var pConnStr: String = ""
)