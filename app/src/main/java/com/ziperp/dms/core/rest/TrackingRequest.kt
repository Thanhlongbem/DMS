package com.ziperp.dms.core.rest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.ziperp.dms.main.timekeeping.model.TimeKeeping

@Entity(tableName = "trackingRequest", indices = [Index(value = ["TimeLogPos"], unique = true)])
data class TrackingRequest(

    @ColumnInfo(name = "StaffId")
    @SerializedName("StaffId")
    var StaffId: String = "",

    @ColumnInfo(name = "TimeLogPos")
    @SerializedName("TimeLogPos")
    var TimeLogPos: String = "",

    @ColumnInfo(name = "PosName")
    @SerializedName("PosName")
    var PosName: String = "",

    @ColumnInfo(name = "LatPos")
    @SerializedName("LatPos")
    var LatPos: String = "",

    @ColumnInfo(name = "LngPos")
    @SerializedName("LngPos")
    var LngPos: String = "",

    @ColumnInfo(name = "DocuNo")
    @SerializedName("DocuNo")
    var DocuNo: String = "",

    @ColumnInfo(name = "TypeCheckin")
    @SerializedName("TypeCheckin")
    var TypeCheckin: String = "",

    @ColumnInfo(name = "MoveSts")
    @SerializedName("MoveSts")
    var MoveSts: String = "",

    @ColumnInfo(name = "BatteryPer")
    @SerializedName("BatteryPer")
    var BatteryPer: String = "",

    @ColumnInfo(name = "DeviceName")
    @SerializedName("DeviceName")
    var DeviceName: String = "",

    var SplitPara: String = "",

    var NumbRecord: String = ""

){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    lateinit var id: Integer  // Purpose for cache

    override fun equals(other: Any?): Boolean {
         (other as? TrackingRequest)?.let{
             return TimeLogPos == it.TimeLogPos
         } ?: kotlin.run { return false }
    }
}