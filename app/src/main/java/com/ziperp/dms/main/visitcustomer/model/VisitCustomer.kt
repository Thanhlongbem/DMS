package com.ziperp.dms.main.visitcustomer.model

import androidx.room.*
import com.ziperp.dms.camera.CustomerImage

@Entity(
    tableName = "visitCustomer",
    indices = arrayOf(Index(value = ["cstVisitNo"], unique = true))
)
data class VisitCustomer(
    @ColumnInfo(name = "cstVisitNo")
    var cstVisitNo: String = "",

    @ColumnInfo(name = "cstCd")
    var cstCd: String = "",

    @ColumnInfo(name = "staffId")
    var staffId: String = "",

    @ColumnInfo(name = "timeLogPosIn")
    var timeLogPosIn: String = "",
    @ColumnInfo(name = "timeLogPosOut")
    var timeLogPosOut: String = "",

    @ColumnInfo(name = "visitStsIn")
    var visitStsIn: String = "",
    @ColumnInfo(name = "visitStsOut")
    var visitStsOut: String = "",

    @ColumnInfo(name = "visitResultIn")
    var visitResultIn: String = "",
    @ColumnInfo(name = "visitResultOut")
    var visitResultOut: String = "",

    @ColumnInfo(name = "posNameIn")
    var posNameIn: String = "",
    @ColumnInfo(name = "latPosIn")
    var latPosIn: String = "",
    @ColumnInfo(name = "lngPosIn")
    var lngPosIn: String = "",

    @ColumnInfo(name = "posNameOut")
    var posNameOut: String = "",
    @ColumnInfo(name = "latPosOut")
    var latPosOut: String = "",
    @ColumnInfo(name = "lngPosOut")
    var lngPosOut: String = "",

    @ColumnInfo(name = "moveStsIn")
    var moveStsIn: String = "",
    @ColumnInfo(name = "moveStsOut")
    var moveStsOut: String = "",

    @ColumnInfo(name = "batteryPerIn")
    var batteryPerIn: Int = 0,
    @ColumnInfo(name = "batteryPerOut")
    var batteryPerOut: Int = 0,

    @ColumnInfo(name = "deviceName")
    var deviceName: String = "",

    @ColumnInfo(name = "visitLatPos")
    var visitLatPos: String = "",

    @ColumnInfo(name = "visitLngPos")
    var visitLngPos: String = "",

    @ColumnInfo(name = "visitPosNm")
    var visitPosNm: String = "",

    @ColumnInfo(name = "remark")
    var remark: String = "",

    @ColumnInfo(name = "isDiffCstAddr")
    var isDiffCstAddr: Int = 0,
    @ColumnInfo(name = "checkInOutDistance")
    var checkInOutDistance: Int = 0,
    @ColumnInfo(name = "checkInDistance")
    var checkInDistance: Int = 0,
    @ColumnInfo(name = "checkOutDistance")
    var checkOutDistance: Int = 0,
    @ColumnInfo(name = "visitLocDistance")
    var visitLocDistance: Int = 0,

    @ColumnInfo(name = "isSynchonizedCheckIn")
    var isSynchonizedCheckIn: Boolean = false,
    @ColumnInfo(name = "isSynchonizedCheckOut")
    var isSynchonizedCheckOut: Boolean = false

) {
    override fun equals(other: Any?): Boolean {
        return (other as? VisitCustomer)?.cstVisitNo == cstVisitNo
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    lateinit var id: Integer  // Purpose for cache

    @Ignore
    var listImages =  listOf<CustomerImage>()

    fun isValidVisitCustomerNo(): Boolean {
        return cstVisitNo.isNotBlank() && !cstVisitNo.contains("Cached_")
    }

    fun isValidCustomerCode(): Boolean {
        return cstCd.isNotBlank() && !cstCd.contains("Cached_")
    }

    fun isNeedSyncCheckOut(): Boolean{
        return !isSynchonizedCheckOut && timeLogPosOut.isNotBlank()
    }


}