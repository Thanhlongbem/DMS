package com.ziperp.dms.main.user.model


import com.google.gson.annotations.SerializedName

data class UpdateUserInfoRequest(
    @SerializedName("PhoneNumber")
    var phoneNumber: String = "",
    @SerializedName("GenderCd")
    var genderCd: String = "",
    @SerializedName("Birthday")
    var birthday: String = "",
    @SerializedName("StaffResiNo")
    var staffResiNo: String = "",
    @SerializedName("BloodType")
    var bloodType: String = "",
    @SerializedName("MarriedStatus")
    var marriedStatus: String = "",
    @SerializedName("Address")
    var address: String = ""
)