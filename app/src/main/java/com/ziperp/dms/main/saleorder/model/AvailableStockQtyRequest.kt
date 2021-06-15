package com.ziperp.dms.main.saleorder.model


import com.google.gson.annotations.SerializedName

data class AvailableStockQtyRequest(
    @SerializedName("RowCnt")
    val rowCnt: Int = 0,
    @SerializedName("RowNo")
    val rowNo: String = "",
    @SerializedName("ItemCd")
    val itemCd: String = "",
    @SerializedName("LotNo")
    val lotNo: String = "",
    @SerializedName("WhCd")
    val whCd: String = "",
    @SerializedName("StaffId")
    val staffId: String = "",
    @SerializedName("OwnShip")
    val ownShip: String = "",
    @SerializedName("CurrDate")
    val currDate: String = "",
    @SerializedName("MasterLocCd")
    val masterLocCd: String = "",
    @SerializedName("SepaStr")
    val sepaStr: String = "",
    @SerializedName("GetTotMode")
    val getTotMode: Int = 0,
    @SerializedName("PartnerCd")
    val partnerCd: String = "",
    @SerializedName("BinCd")
    val binCd: String = ""
)