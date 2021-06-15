package com.ziperp.dms.main.customer.list.model


import com.google.gson.annotations.SerializedName

data class CUDCustomerRequest(
    @SerializedName("CstCd")
    var cstCd: String = "",
    @SerializedName("CstNm")
    var cstNm: String = "",
    @SerializedName("CstNo")
    var cstNo: String = "",
    @SerializedName("OfficePhone")
    var officePhone: String = "",
    @SerializedName("OfficeFax")
    var officeFax: String = "",
    @SerializedName("WorkTel")
    var workTel: String = "",
    @SerializedName("Email")
    var email: String = "",
    @SerializedName("Website")
    var website: String = "",
    @SerializedName("Remark")
    var remark: String = "",
    @SerializedName("RegMan")
    var regMan: String = "",
    @SerializedName("RegManNm")
    var regManNm: String = "",
    @SerializedName("CstDiv")
    var cstDiv: String = "",
    @SerializedName("CstClssListValues")
    var cstClssListValues: String = "",
    @SerializedName("Representative")
    var representative: String = "",
    @SerializedName("CstBizLicNo")
    var cstBizLicNo: String = "",
    @SerializedName("TaxCode")
    var taxCode: String = "",
    @SerializedName("CstGrp1")
    var cstGrp1: String = "",
    @SerializedName("CstGrp2")
    var cstGrp2: String = "",
    @SerializedName("CstGrp3")
    var cstGrp3: String = "",
    @SerializedName("CstGrp4")
    var cstGrp4: String = "",
    @SerializedName("Street")
    var street: String = "",
    @SerializedName("DistrictCd")
    var districtCd: String = "",
    @SerializedName("CountryCd")
    var countryCd: String = "",
    @SerializedName("RegionCd")
    var regionCd: String = "",
    @SerializedName("AddrOnMap")
    var addrOnMap: String = "",
    @SerializedName("AddrLat")
    var addrLat: String = "",
    @SerializedName("AddrLng")
    var addrLng: String = "",
    @SerializedName("BirthdayDate")
    var birthdayDate: String = "",
    @SerializedName("Gender")
    var gender: String = "",
    @SerializedName("CallFrmMobileYn")
    var callFrmMobileYn: String = "1"
)