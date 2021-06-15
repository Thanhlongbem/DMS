package com.ziperp.dms.core.tracking

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.widget.Toast
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.DetectedActivity
import com.ziperp.dms.utils.LogUtils
import pub.devrel.easypermissions.EasyPermissions


object ActivityRecognitionManager: TrackingManager() {

    const val IN_VEHICLE = 0
    const val ON_BICYCLE = 1
    const val ON_FOOT = 2
    const val STILL = 3
    const val UNKNOWN = 4
    const val TILTING = 5
    const val WALKING = 7
    const val RUNNING = 8

    const val TRANSITION_ACTION_RECEIVER = "com.ziperp.dms.TRANSITIONS_RECEIVER_ACTION"
    private var isSuccess: Boolean = false
    var lastActivity: DetectedActivity? = null
    private lateinit var pendingIntent: PendingIntent
    private lateinit var transitionsReceiver: TransitionsReceiver
    //private val request = ActivityTransitionRequest(ServiceUtils.getTrackingActivity())

    var lastActivityValue: Int = UNKNOWN
        get(){
            lastActivity?.let{
                return when(it.type){
                    DetectedActivity.UNKNOWN -> UNKNOWN
                    DetectedActivity.IN_VEHICLE -> IN_VEHICLE
                    DetectedActivity.ON_BICYCLE -> ON_BICYCLE
                    DetectedActivity.ON_FOOT -> ON_FOOT
                    DetectedActivity.STILL -> STILL
                    DetectedActivity.TILTING -> TILTING
                    DetectedActivity.WALKING -> WALKING
                    DetectedActivity.RUNNING -> RUNNING
                    else -> UNKNOWN
                }
            }
            return UNKNOWN
        }

    override fun startTracking(context: Context) {
        this.context = context
        applicationContext = context.applicationContext
        isCreated = true
        updateTrackingIfNeeded()
    }

    override fun stopTracking() {
        isCreated = false
        isSuccess = false
        ActivityRecognition.getClient(context).removeActivityTransitionUpdates(
            pendingIntent
        )
            .addOnSuccessListener { pendingIntent.cancel() }
            .addOnFailureListener{
                LogUtils.d("Transitions Api could not be stop: " + it.message);
            }
        context.unregisterReceiver(
            transitionsReceiver
        );
    }

    fun updateTrackingIfNeeded(){
//        LogUtils.d("Has permission " + hasPermission())
        if(hasPermission() && isCreated && !isSuccess){
            requestActivityUpdate()
            isSuccess = true
        }
    }

    private fun hasPermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(context, Manifest.permission.ACTIVITY_RECOGNITION)
        }else{
            true
        }
    }

    private fun requestActivityUpdate(){
        LogUtils.d("Request update activity ")
        val intent = Intent(context, TransitionsReceiver::class.java)
        intent.action =
            TRANSITION_ACTION_RECEIVER
        pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        transitionsReceiver = TransitionsReceiver()
        context.registerReceiver(
            transitionsReceiver,
            IntentFilter(TRANSITION_ACTION_RECEIVER)
        )
        ActivityRecognition.getClient(context)
            .requestActivityUpdates(
                3000L,
                pendingIntent
            )
            .addOnSuccessListener {
                LogUtils.d("Transitions Api was successfully registered. " + ServiceUtils.getTrackingActivity().size);
            }
            .addOnFailureListener{
                LogUtils.d("Transitions Api could not be registered: " + it.message);
            }
    }

    fun handleDetectedActivities(probableActivities: List<DetectedActivity>) {
        if(probableActivities.isNotEmpty() && isCreated){
            probableActivities.maxBy { it.confidence }?.let {
                lastActivity = it
            }

            return

            lastActivity?.type?.let { type ->
                when (type) {
                    DetectedActivity.IN_VEHICLE -> {
                        Toast.makeText(
                            applicationContext,
                            "ActivityRecogition In Vehicle: " + lastActivity?.confidence,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    DetectedActivity.ON_BICYCLE -> {
                        Toast.makeText(
                            applicationContext,
                            "ActivityRecogition On Bicycle: " + lastActivity?.confidence,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    DetectedActivity.ON_FOOT -> {
                        Toast.makeText(
                            applicationContext,
                            "ActivityRecogition On Foot: " + lastActivity?.confidence,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    DetectedActivity.RUNNING -> {
                        Toast.makeText(
                            applicationContext,
                            "ActivityRecogition Running: " + lastActivity?.confidence,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    DetectedActivity.STILL -> {
                        Toast.makeText(
                            applicationContext,
                            "ActivityRecogition Still: " + lastActivity?.confidence,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    DetectedActivity.TILTING -> {
                        Toast.makeText(
                            applicationContext,
                            "ActivityRecogition Tilting: " + lastActivity?.confidence,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    DetectedActivity.WALKING -> {
                        Toast.makeText(
                            applicationContext,
                            "ActivityRecogition Walking: " + lastActivity?.confidence,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    DetectedActivity.UNKNOWN -> {
                        Toast.makeText(
                            applicationContext,
                            "ActivityRecogition Unknown: " + lastActivity?.confidence,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> Toast.makeText(
                        applicationContext,
                        "ActivityRecogition Undefine: " + lastActivity?.confidence,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }



}