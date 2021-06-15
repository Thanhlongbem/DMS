package com.ziperp.dms.main.timekeeping.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.ziperp.dms.utils.AppUtil

data class TimeKeepingListResponse(
    @SerializedName("Table")
    val data: List<TimeKeeping> = listOf()
)

@Entity(tableName = "timeKeeping", indices = arrayOf(Index(value = ["timeKeepNo"], unique = true)))
data class TimeKeeping(

    @ColumnInfo(name = "timeKeepNo")
    @SerializedName("TimeKeepNo")
    var timeKeepNo: String = "",

    @ColumnInfo(name = "checkInDay")
    @SerializedName("CheckInDay")
    var checkInDay: String = "",

    @ColumnInfo(name = "checkInTime")
    @SerializedName("CheckInTime")
    var checkInTime: String = "",

    @ColumnInfo(name = "checkOutDay")
    @SerializedName("CheckOutDay")
    var checkOutDay: String = "",

    @ColumnInfo(name = "checkOutTime")
    @SerializedName("CheckOutTime")
    var checkOutTime: String = "",

    @ColumnInfo(name = "checkInLatPos")
    @SerializedName("CheckInLatPos")
    var checkInLatPos: String = "",

    @ColumnInfo(name = "checkInLngPos")
    @SerializedName("CheckInLngPos")
    var checkInLngPos: String = "",

    @ColumnInfo(name = "checkInPosNm")
    @SerializedName("CheckInPosNm")
    var checkInPosNm: String = "",

    @ColumnInfo(name = "checkOutLatPos")
    @SerializedName("CheckOutLatPos")
    var checkOutLatPos: String = "",

    @ColumnInfo(name = "checkOutLngPos")
    @SerializedName("CheckOutLngPos")
    var checkOutLngPos: String = "",

    @ColumnInfo(name = "checkOutPosNm")
    @SerializedName("CheckOutPosNm")
    var checkOutPosNm: String = "",

    @ColumnInfo(name = "durationHour")
    @SerializedName("DurationHour")
    var durationHour: Double = 0.0,

    @ColumnInfo(name = "staffNm")
    @SerializedName("StaffNm")
    var staffNm: String = "",

    @ColumnInfo(name = "staffId")
    @SerializedName("StaffId")
    var staffId: String = "",

    @ColumnInfo(name = "remarkCheckin")
    @SerializedName("RemarkCheckin")
    var remarkCheckin: String = "",

    @ColumnInfo(name = "remarkCheckout")
    @SerializedName("RemarkCheckout")
    var remarkCheckout: String = "",

    @ColumnInfo(name = "moveStsCheckIn")
    var moveStsCheckIn: String = "",

    @ColumnInfo(name = "moveStsCheckOut")
    var moveStsCheckOut: String = "",

    @SerializedName("InBatteryPer")
    @ColumnInfo(name = "batteryPerCheckIn")
    var batteryPerCheckIn: Int = 0,

    @SerializedName("OutBatteryPer")
    @ColumnInfo(name = "batteryPerCheckOut")
    var batteryPerCheckOut: Int = 0,

    @ColumnInfo(name = "isSynchonizedCheckIn")
    var isSynchonizedCheckIn: Boolean = false,

    @ColumnInfo(name = "isSynchonizedCheckOut")
    var isSynchonizedCheckOut: Boolean = false

) {
    override fun equals(other: Any?): Boolean {
        return (other as? TimeKeeping)?.timeKeepNo == timeKeepNo
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    lateinit var id: Integer  // Purpose for cache

    fun fetchAddress() {
        when {
            isNeedResyncCheckIn() -> checkInPosNm =
                AppUtil.getAddrLatLng(checkInLatPos, checkInLngPos)
            isNeedResyncCheckOut() -> checkOutPosNm =
                AppUtil.getAddrLatLng(checkOutLatPos, checkOutLngPos)
        }
    }

    fun isValidTimeKeepNo(): Boolean{
        return timeKeepNo.isNotBlank() && !timeKeepNo.contains("Cached_")
    }

    fun isNeedSync(): Boolean{
        return isNeedSyncCheckIn() or isNeedSyncCheckOut()
    }

    fun isNeedSyncCheckIn(): Boolean{
        return !isSynchonizedCheckIn && checkInTime.isNotBlank()
    }

    fun isNeedSyncCheckOut(): Boolean{
        return !isSynchonizedCheckOut && checkOutTime.isNotBlank()
    }

    fun isNeedResyncCheckIn(): Boolean{
        return isSynchonizedCheckIn && isValidTimeKeepNo() && checkInPosNm.isBlank()
    }

    fun isNeedResyncCheckOut(): Boolean{
        return isSynchonizedCheckOut && isValidTimeKeepNo() && checkOutPosNm.isBlank()
    }


}