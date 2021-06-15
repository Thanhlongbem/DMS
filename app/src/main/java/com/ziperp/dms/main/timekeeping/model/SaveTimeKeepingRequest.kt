package com.ziperp.dms.main.timekeeping.model


import android.os.Build
import com.google.gson.annotations.SerializedName

data class SaveTimeKeepingRequest(
    @SerializedName("TimeKeepNo")
    var timeKeepNo: String = "",
    @SerializedName("StaffId")
    var staffId: String = "",
    @SerializedName("TimeLogPos")
    var timeLogPos: String = "",
    @SerializedName("PosName")
    var posName: String = "",
    @SerializedName("LatPos")
    var latPos: String = "",
    @SerializedName("LngPos")
    var lngPos: String = "",
    @SerializedName("TypeCheckin")
    var typeCheckin: String = "",
    @SerializedName("MoveSts")
    var moveSts: String = "",
    @SerializedName("BatteryPer")
    var batteryPer: Int = 0,
    @SerializedName("DeviceName")
    var deviceName: String = (Build.MANUFACTURER + " " + Build.MODEL),
    @SerializedName("Remark")
    var remark: String = ""
)