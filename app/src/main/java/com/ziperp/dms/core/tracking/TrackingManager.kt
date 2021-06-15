package com.ziperp.dms.core.tracking

import android.content.Context

abstract class TrackingManager {
    protected var isCreated = false
    protected lateinit var context: Context
    protected lateinit var applicationContext: Context

    abstract fun startTracking(service: Context)
    abstract fun stopTracking()
}