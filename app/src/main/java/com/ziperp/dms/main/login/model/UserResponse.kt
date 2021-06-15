package com.ziperp.dms.main.login.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserResponse(
    @SerializedName("Table")
    val table: List<UserInfo> = listOf()
): Serializable

data class UserInfo(
    @SerializedName("Status")
    val status: String = "",
    @SerializedName("UserId")
    val userId: String = "",
    @SerializedName("StaffId")
    val staffId: String = "",
    @SerializedName("StaffNo")
    val staffNo: String = "",
    @SerializedName("StaffNm")
    val staffNm: String = "",
    @SerializedName("DeptCd")
    val deptCd: String = "",
    @SerializedName("DeptNm")
    val deptNm: String = "",
    @SerializedName("MasterLocCd")
    val masterLocCd: String = "",
    @SerializedName("MasterLocNm")
    val masterLocNm: String = "",
    @SerializedName("Birthday")
    val birthday: String = "",
    @SerializedName("Email")
    val email: String = "",
    @SerializedName("PhoneNumber")
    val phoneNumber: String = "",
    @SerializedName("StaffResiNo")
    val staffResiNo: String = "",
    @SerializedName("BloodTypeNm")
    val bloodTypeNm: String = "",
    @SerializedName("BloodType")
    val bloodType: String = "",
    @SerializedName("MarriedStatusNm")
    val marriedStatusNm: String = "",
    @SerializedName("MarriedStatus")
    val marriedStatus: Int = 0,
    @SerializedName("Gender")
    val gender: String = "",
    @SerializedName("GenderCd")
    val genderCd: String = "",
    @SerializedName("Position")
    val position: String = "",
    @SerializedName("BankCd")
    val bankCd: String = "",
    @SerializedName("BankNm")
    val bankNm: String = "",
    @SerializedName("BankAccNo")
    val bankAccNo: String = "",
    @SerializedName("BankAccOwner")
    val bankAccOwner: String = "",
    @SerializedName("Address")
    val address: String = "",
    @SerializedName("ImgProfile")
    val imgProfile: String = "",
    @SerializedName("LoginStatus")
    val loginStatus: Int = 0,
    @SerializedName("LoginFailCnt")
    val loginFailCnt: Int = 0,
    @SerializedName("ActiveYn")
    val activeYn: Int = 0,
    @SerializedName("SecuLevel")
    val secuLevel: Int = 0,
    @SerializedName("LcBaseCurr")
    val lcBaseCurr: String = "",
    @SerializedName("FrBaseCurr")
    val frBaseCurr: String = "",
    @SerializedName("FrBaseCurrExRate")
    val frBaseCurrExRate: Double = 0.0,
    @SerializedName("AskSaveWhenJump")
    val askSaveWhenJump: Int = 0,
    @SerializedName("MaxSizeUpload")
    val maxSizeUpload: Int = 0,
    @SerializedName("MaxSizeOfFileUpload")
    val maxSizeOfFileUpload: Int = 0,
    @SerializedName("MaxHeightOfImgUp")
    val maxHeightOfImgUp: Int = 0,
    @SerializedName("MaxWidthOfImpUp")
    val maxWidthOfImpUp: Int = 0,
    @SerializedName("DefaultLangOfApp")
    val defaultLangOfApp: Int = 0,
    @SerializedName("EnableMobileMode")
    val enableMobileMode: Int = 0,
    @SerializedName("LangId")
    val langId: Int = 0,
    @SerializedName("TenantId")
    val tenantId: String = "",
    @SerializedName("SiteID")
    val siteID: String = "",
    @SerializedName("ShowEvent")
    val showEvent: Int = 0,
    @SerializedName("ColorNavBar")
    val colorNavBar: String = "",
    @SerializedName("DcPntQty")
    val dcPntQty: Int = 0,
    @SerializedName("DcPntBcPrice")
    val dcPntBcPrice: Int = 0,
    @SerializedName("DcPntFcPrice")
    val dcPntFcPrice: Int = 0,
    @SerializedName("DcPntBcAmt")
    val dcPntBcAmt: Int = 0,
    @SerializedName("DcPntFcAmt")
    val dcPntFcAmt: Int = 0,
    @SerializedName("DcExRate")
    val dcExRate: Int = 0,
    @SerializedName("DcPntRate")
    val dcPntRate: Int = 0,
    @SerializedName("DcPntConvStk")
    val dcPntConvStk: Int = 0,
    @SerializedName("SalesPriceRule")
    val salesPriceRule: Int = 0,
    @SerializedName("MapAPI")
    val mapAPI: Int = 0,
    @SerializedName("MapLayer")
    val mapLayer: Int = 0,
    @SerializedName("CheckInDistance")
    val checkInDistance: Int = 0,
    @SerializedName("CheckOutDistance")
    val checkOutDistance: Int = 0,
    @SerializedName("TimeAutoTracking")
    val timeAutoTracking: String = "",
    @SerializedName("GoogleMapAPIKey")
    val googleMapAPIKey: String = "",
    @SerializedName("VisitLocDistance")
    val visitLocDistance: Int = 0,
    @SerializedName("DurationTracking")
    val durationTracking: Int = 0,
    @SerializedName("DistanceTracking")
    val distanceTracking: Int = 0,
    @SerializedName("TenantNm")
    val tenantNm: String ="",
    @SerializedName("DBSrv")
    val dBSrv: String = "",
    @SerializedName("DBNm")
    val dBNm: String = "",
    @SerializedName("ConnStr")
    val connStr: String = "",
    @SerializedName("Remark")
    val remark: String = ""
): Serializable
