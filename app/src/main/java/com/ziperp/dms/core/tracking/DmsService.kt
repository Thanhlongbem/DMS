package com.ziperp.dms.core.tracking

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.ziperp.dms.core.cache.CacheManager
import com.ziperp.dms.utils.LogUtils


class DmsService : Service() {

    companion object{
        const val STOP_ACTION = "STOP_SERVICE_ACTION"
        const val CHANNEL_ID = "DMS_CHANNEL"
        const val NOTIFICATION_CHANNEL = "DMS_NOTIFICATION_CHANNEL"

        //SYNC
        const val SYNC_ID = "SYNC_ID"
        const val SYNC_CHANNEL = "SYNC_CHANNEL"
    }

    override fun onCreate() {
        super.onCreate()
        ServiceUtils.startForeground(this)
        init()
        LogUtils.d("Init service")
    }

    private fun init() {
        ActivityRecognitionManager.startTracking(this)
        LocationTrackingManager.startTracking( this)
        CacheManager.preload()
    }

    private fun release() {
        ActivityRecognitionManager.stopTracking()
        LocationTrackingManager.stopTracking()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogUtils.d("onStartCommand ${intent?.action}")
        if(intent?.action == STOP_ACTION){
            release()
            stopForeground(true)
            this.stopSelf()
            return START_NOT_STICKY;
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }




}
