package com.ziperp.dms.common.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem


class StaffMarkerItem : ClusterItem {
     private val position: LatLng
     val staffNm: String
     val logPosTime: String
     val checkInType: String

    constructor(lat: Double, lng: Double, staffNm: String, logPosTime: String, checkInType: String) {
        this.position = LatLng(lat, lng)
        this.staffNm = staffNm
        this.logPosTime = logPosTime
        this.checkInType = checkInType
    }

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String = staffNm

    override fun getSnippet(): String = ""

}