package com.ziperp.dms.core.tracking

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.ziperp.dms.AppConfiguration
import com.ziperp.dms.common.activity.SplashActivity
import com.ziperp.dms.core.rest.Constants


object ServiceUtils {

    fun getTrackingActivity(): List<ActivityTransition> {
        val results = arrayListOf<ActivityTransition>()
        val activities = arrayListOf<Int>(
            DetectedActivity.STILL,
            DetectedActivity.WALKING,
            DetectedActivity.ON_FOOT,
            DetectedActivity.RUNNING,
            DetectedActivity.ON_BICYCLE,
            DetectedActivity.IN_VEHICLE
        )
        for (activity in activities) {
            results.add(
                ActivityTransition.Builder()
                    .setActivityType(activity)
                    .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build()
            )
            results.add(
                ActivityTransition.Builder()
                    .setActivityType(activity)
                    .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build()
            )
        }
        return results
    }

    fun getBatteryLevel(service: Context): Float {
        val batteryIntent =
            service.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryIntent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

        // Error checking that probably isn't needed but I added just in case.
        return if (level == -1 || scale == -1) {
            50.0f
        } else level.toFloat() / scale.toFloat() * 100.0f
    }

    fun startForeground(service: Service) {
        val notificationIntent = Intent(service, SplashActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(service, 0, notificationIntent, 0)

        //Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val warningChannel =
                NotificationChannel(
                    DmsService.CHANNEL_ID,
                    DmsService.NOTIFICATION_CHANNEL,
                    NotificationManager.IMPORTANCE_HIGH
                )
            warningChannel.description = Constants.Service.NOTIFICATION_CHANNEL
            val notificationManager =
                service.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(warningChannel)
        }

//        val stopIntent = Intent(service, DmsService::class.java)
//        stopIntent.action = DmsService.STOP_ACTION
//        val stopPendingIntent = PendingIntent.getService(
//            service, 0,
//            stopIntent, PendingIntent.FLAG_CANCEL_CURRENT
//        )

        val notification: Notification =
            NotificationCompat.Builder(service, DmsService.CHANNEL_ID)
                .setSmallIcon(com.ziperp.dms.R.drawable.ic_dms_noti)
                .setContentTitle("Do DMS")
                .setContentText("Location Service activated")
                .setContentIntent(pendingIntent)
//                .addAction(com.ziperp.dms.R.drawable.icon_delete_service, "STOP", stopPendingIntent)
                .build()

        service.startForeground(123, notification)
    }

    fun startDMSService(context: Context) {
        if(!AppConfiguration.IS_ENABLE_SERVICE) return
        if (!isServiceRunning(context, DmsService::class.java)) {
            val intent = Intent(context, DmsService::class.java)
            context.startService(intent)
        }
    }

    private fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun isGPSEnable(context: Context?): Boolean{
        if(context != null){
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            if(locationManager != null){
                return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            }
        }
        return false

    }

    fun showSettingsAlert(context: Context) {
//        val alertDialog = AlertDialog.Builder(context)
//        alertDialog.setTitle("GPS required")
//        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?")
//        alertDialog.setPositiveButton("Settings") { _, _ ->
//            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//            context.startActivity(intent)
//        }
//        alertDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
//        alertDialog.show()

        val googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API).build()

        googleApiClient.connect()

        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(LocationTrackingManager.LOCATION_UPDATE_INTERVAL)
            .setFastestInterval(LocationTrackingManager.LOCATION_FASTEST_INTERVAL)


        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)

        val pendingResult = LocationServices
            .getSettingsClient(context)
            .checkLocationSettings(builder.build())

        pendingResult.addOnCompleteListener { task ->
            if (task.isSuccessful.not()) {
                task.exception?.let {
                    if (it is ApiException && it.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        when (context) {
                            is Activity -> {
                                (it as ResolvableApiException).startResolutionForResult(
                                    context,
                                    120
                                )
                            }
                            is Fragment -> {
                                (it as ResolvableApiException).startResolutionForResult(
                                    context.activity,
                                    120
                                )
                            }
                            else -> {
                                val alertDialog = AlertDialog.Builder(context)
                                alertDialog.setTitle("GPS required")
                                alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?")
                                alertDialog.setPositiveButton("Settings") { _, _ ->
                                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                    context.startActivity(intent)
                                }
                                alertDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                                alertDialog.show()
                            }
                        }
                    }
                }
            }
        }

    }
}