package com.ziperp.dms

object AppConfiguration {
    val IS_ENABLE_SERVICE = true
    var IS_POST_LOCATION = !BuildConfig.DEBUG

    val UPDATE_LOCATION_INTERVAL = 15// 15s
    val MAX_CHECK_IN_DISTANCE_ACCEPT = 500f // 500m

    val AUTO_UPDATE_INTERVAL = 20// 20s
    var TRACKING_INTERVAL = 10000L //10s
    val NUMBER_DIGIT = 2
    var IMAGE_QUALITY = "1"// origin: 0 reduced size: 1
    var MAP_ZOOM_LEVEL = 16f

    val SERVER_DATE_FORMAT = "yyyyMMdd"
    val SERVER_DATE_TIME_FORMAT = "yyyyMMddHHmm"
    val SERVER_DATE_TIME_FULL_FORMAT = "yyyyMMddHHmmss"
    val SERVER_HOUR_MINUTE_FORMAT = "HHmmss"

    val APP_DATE_FORMAT = "dd/MM/yyyy"
    val APP_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm"

}