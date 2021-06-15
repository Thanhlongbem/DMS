package com.ziperp.dms.core.form.model

import java.io.Serializable

data class KeyValue(
    var keyCode: String = "",
    var valueCode: String = "",
    var keyName: String = "",
    var valueName: String = "",
    var jsonData: String = ""// Keep all data
) : Serializable

data class ItemControlSelection(
    var keyValues: ArrayList<KeyValue>,
    var itemControlForm: ItemControlForm
) : Serializable
