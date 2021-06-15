package com.ziperp.dms.main.customer.list.model


import com.google.gson.annotations.SerializedName

data class CustomerListResponse(
    @SerializedName("Table")
    val data: List<Customer> = listOf(),

    @SerializedName("Table1")
    val record: List<Record> = arrayListOf()
) {
    data class Record(
        @SerializedName("TotalRecords")
        val totalRecords: Double = 0.0
    )
}

data class Customer(
    @SerializedName("txtCstNm")
    val txtCstNm: String = "",
    @SerializedName("txtCstNo")
    val txtCstNo: String = "",
    @SerializedName("txtCstDiv")
    val txtCstDiv: String = "",
    @SerializedName("txtProspectType")
    val txtProspectType: String = "",
    @SerializedName("txtAccountPhoneCol")
    val txtAccountPhoneCol: String = "",
    @SerializedName("txtPhone")
    val txtPhone: String = "",
    @SerializedName("txtFax")
    val txtFax: String = "",
    @SerializedName("txtAccountEmailCol")
    val txtAccountEmailCol: String = "",
    @SerializedName("txtEmail")
    val txtEmail: String = "",
    @SerializedName("txtWebsite")
    val txtWebsite: String = "",
    @SerializedName("txtAddress")
    val txtAddress: String = "",
    @SerializedName("txtDistrict")
    val txtDistrict: String = "",
    @SerializedName("txtCity")
    val txtCity: String = "",
    @SerializedName("txtCountry")
    val txtCountry: String = "",
    @SerializedName("txtCstOwner")
    val txtCstOwner: String = "",
    @SerializedName("RepPerson")
    val repPerson: String = "",
    @SerializedName("Remark")
    val remark: String = "",
    @SerializedName("RegMan")
    val regMan: String = "",
    @SerializedName("UptMan")
    val uptMan: String = "",
    @SerializedName("RegDate")
    val regDate: String = "",
    @SerializedName("UptDate")
    val uptDate: String = "",
    @SerializedName("dtBirthday")
    val dtBirthday: String = "",
    @SerializedName("LinkFormId")
    val linkFormId: String = "",
    @SerializedName("GridKey")
    val gridKey: String = "",
    @SerializedName("LinkPara")
    val linkPara: String = ""
)