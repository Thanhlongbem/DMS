package com.ziperp.dms.main.visitcustomer.model

data class ItemMarker (
    val customerCd: String,
    val lat: Double,
    val lng: Double,
    val txtCstNm: String,
    val txtAddress: String,
    val repPerson: String,
    val txtPhone: String,
    var distanceValue: String,
    val isCurrentPosition: Boolean


): Comparable<ItemMarker> {
    override fun toString(): String {
        return "ItemMarker(lat=$lat, lng=$lng, txtCstNm='$txtCstNm', txtAddress='$txtAddress', repPerson='$repPerson', txtPhone='$txtPhone', distanceValue='$distanceValue', isCurrentPosition=$isCurrentPosition)"
    }

    override fun compareTo(other: ItemMarker): Int {
        return compareValues(other.customerCd, customerCd)
    }


}