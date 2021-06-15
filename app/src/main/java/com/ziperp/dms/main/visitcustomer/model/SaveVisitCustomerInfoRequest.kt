package com.ziperp.dms.main.visitcustomer.model

import com.google.gson.annotations.SerializedName

data class SaveVisitCustomerInfoRequest(

    @SerializedName("CstVisitNo")
    var cstVisitNo: String = "",
    @SerializedName("CstCd")
    var cstCd: String = "",
    @SerializedName("StaffId")
    var staffId: String = "",
    @SerializedName("TimeLogPos")
    var timeLogPos: String = "",
    @SerializedName("VisitSts")
    var visitSts: String = "",
    @SerializedName("VisitResult")
    var visitResult: String = "",
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
    var deviceName: String = "",
    @SerializedName("VisitLatPos")
    var visitLatPos: String = "",
    @SerializedName("VisitLngPos")
    var visitLngPos: String = "",
    @SerializedName("VisitPosNm")
    var visitPosNm: String = "",
    @SerializedName("Remark")
    var remark: String = "",
    @SerializedName("IsDiffCstAddr")
    var isDiffCstAddr: Int = 0,
    @SerializedName("CheckInOutDistance")
    var checkInOutDistance: Int = 0,
    @SerializedName("CheckInDistance")
    var checkInDistance: Int = 0,
    @SerializedName("CheckOutDistance")
    var checkOutDistance: Int = 0,
    @SerializedName("VisitLocDistance")
    var visitLocDistance: Int = 0
)