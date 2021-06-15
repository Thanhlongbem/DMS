package com.ziperp.dms.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "formControl")
data class FormControl(
    @PrimaryKey
    @ColumnInfo(name = "controlFormId")
    val controlFormId: String = "", // controlId_formId

    @ColumnInfo(name = "data")
    val data: String = ""

)