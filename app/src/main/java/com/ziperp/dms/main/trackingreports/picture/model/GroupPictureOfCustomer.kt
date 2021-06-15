package com.ziperp.dms.main.trackingreports.picture.model

data class GroupPictureOfCustomer(
    val customerVisitNo : String,
    val listPicture: List<TrackingPictureDetailResponse.Picture>
)