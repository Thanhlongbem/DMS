package com.ziperp.dms.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "cachedRequest")
data class CachedRequest(
    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = 0L,

    @ColumnInfo(name = "requestId")
    val requestId: Int = -1,

    @ColumnInfo(name = "bodyRequest")
    val body: String = ""
)
