package com.ziperp.dms.main.timekeeping.model


data class TimeKeepingModelExample (
    val isDone: Boolean = false,
    val checkinTime: String = "",
    val checkinPlace: String = "",
    val checkinNote: String = "",
    val checkoutTime: String = "",
    val checkoutPlace: String = "",
    val checkoutNote: String = ""
)