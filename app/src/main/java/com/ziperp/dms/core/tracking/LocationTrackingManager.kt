package com.ziperp.dms.core.tracking

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.ziperp.dms.App
import com.ziperp.dms.AppConfiguration
import com.ziperp.dms.core.rest.RestService
import com.ziperp.dms.core.service.TrackingService
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import java.util.*


object LocationTrackingManager : TrackingManager(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var locationRepository: LocationRepository
    private lateinit var locationRequest: LocationRequest
    private var timerLocation: Timer? = null
    private lateinit var apiClient: GoogleApiClient
    private lateinit var mLocationCallback: LocationCallback
    const val LOCATION_UPDATE_INTERVAL = 10000L
    const val LOCATION_FASTEST_INTERVAL = 5000L
    private var TAG = LocationTrackingManager::class.java.simpleName
    var lastLocation: Location? = null
    var currentTime: Long = 0
    var isLocationAvailable: Boolean = false
    val TAG_SYNC_WORK = "TAG_SYNC_WORK"


    override fun startTracking(context: Context) {
        LogUtils.d("Start LocationTrackingManager")
        isCreated = true
        this.context = context
        applicationContext = context.applicationContext
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationRepository = LocationRepository(
            RestService.createService(TrackingService::class.java),
            App.shared().appDatabase.trackingRequestDao()
        )

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {

                val currentLocation  = result?.lastLocation
                var distance  = 0
                if(currentLocation != null && lastLocation != null){
                    distance = AppUtil.distanceBetweenTwoPointInMeter(
                        currentLocation,
                        lastLocation!!
                    ).toInt()
                }


//                if(currentTime == 0L) currentTime = System.currentTimeMillis()
//                var duration = System.currentTimeMillis() - currentTime
//                if(duration > 60000){
//                }

//                if(distance > 500 ){
//                    LogUtils.d("result " + result?.lastLocation?.latitude + " " + result?.lastLocation?.longitude)
//                }else{
                    lastLocation = result?.lastLocation
//                    LogUtils.d("result " + result?.lastLocation?.latitude + " " + result?.lastLocation?.longitude)
//                }
            }

            @SuppressLint("MissingPermission")
            override fun onLocationAvailability(availability: LocationAvailability?) {
                LogUtils.d("availability " + availability?.isLocationAvailable)
                isLocationAvailable = availability?.isLocationAvailable ?: false
                if (availability?.isLocationAvailable == true) {
//                    if(lastLocation == null){
//                        fusedLocationProviderClient.requestLocationUpdates(
//                            locationRequest,
//                            mLocationCallback,
//                            Looper.myLooper()
//                        )
//                    }
                }
            }
        }

        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(LOCATION_UPDATE_INTERVAL)
            .setFastestInterval(LOCATION_FASTEST_INTERVAL)
//            .setSmallestDisplacement(10.0f)

        apiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()

        connectGoogleClient()
        startTimer()
        registerGpsStatus()
    }

    private fun registerGpsStatus() {
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
        context.registerReceiver(locationSwitchStateReceiver, filter)
    }

    private val locationSwitchStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            if (LocationManager.PROVIDERS_CHANGED_ACTION == intent.action) {
                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//                val isNetworkEnabled =
//                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                LogUtils.d("isGpsEnabled $isGpsEnabled ")
                if (isGpsEnabled) {
                    requestLocationUpdate()
                }
            }
        }
    }

    override fun stopTracking() {
        LogUtils.d("Stop LocationTrackingManager")
        isCreated = false
        if (apiClient.isConnected) {
            apiClient.disconnect()
        }

        timerLocation?.cancel()
    }


    private fun connectGoogleClient() {
        val googleAPI = GoogleApiAvailability.getInstance()
        val resultCode = googleAPI.isGooglePlayServicesAvailable(context)
        if (resultCode == ConnectionResult.SUCCESS) {
            apiClient.connect()
        }
    }

    private fun startTimer() {
        timerLocation = Timer()
        timerLocation?.schedule(object : TimerTask() {
            override fun run() {
                ActivityRecognitionManager.updateTrackingIfNeeded()
                if (hasPermission()) {
                    postTrackingData()
                } else {
                    LogUtils.d("$TAG doesn't enough permission")
                }
                AutoSyncManager.checkSyncedData()
            }
        }, 0, AppConfiguration.TRACKING_INTERVAL)
    }

    @SuppressLint("MissingPermission")
    private fun postTrackingData() {
        lastLocation?.let { location ->
            if (validateLocation(location)) {
                if (!DmsUserManager.isValidUserInfo()) {
                    LogUtils.d("Invalid userInfo")
                    return@let
                }
                val address = AppUtil.getAddrLatLng(
                    applicationContext,
                    location.latitude,
                    location.longitude
                )
                val battery = ServiceUtils.getBatteryLevel(context).toInt()
                LogUtils.d("location ${location.latitude} : ${location.longitude} $address")

                if (AppConfiguration.IS_POST_LOCATION) {
                    locationRepository.postTrackingLocation(location, address ?: " ", battery)
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            LogUtils.d("Post location successfully " + Gson().toJson(it))
                            LogUtils.d("syncData successfully ${location.latitude} : ${location.longitude} $address")
                        }, {
                            LogUtils.d("Error " + it.localizedMessage)
                        })
                }
            }
        } ?: run {
            requestLocationUpdate()
        }
    }

    private fun validateLocation(location: Location?): Boolean {
        return (location != null && location.latitude != 0.0 && location.longitude != 0.0)
    }

    private fun hasPermission(): Boolean {
        var hasPermission = (ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED)
        if (hasPermission) {
            //Check enable GPS & available network
            var isGPSEnabled: Boolean =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            return isGPSEnabled
        }
        return hasPermission
    }


    @SuppressLint("MissingPermission")
    fun getLatestLocation(): Location?{
        val locationListener = object: android.location.LocationListener{
            override fun onLocationChanged(p0: Location?) {
            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            }

            override fun onProviderEnabled(p0: String?) {
            }

            override fun onProviderDisabled(p0: String?) {
            }

        }
        locationManager.requestSingleUpdate(
            LocationManager.GPS_PROVIDER,
            locationListener,
            Looper.getMainLooper()
        )
        val lastKnownLocation: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        return lastKnownLocation ?: lastLocation
    }

    override fun onConnected(arg: Bundle?) {
        requestLocationUpdate()
    }

    // The minimum distance to change Updates in meters
    private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters

    // The minimum time between updates in milliseconds
    private const val MIN_TIME_BW_UPDATES = 1000 * 60 * 1 // 1 minute
        .toLong()
    @SuppressLint("MissingPermission")
    fun requestLocationUpdate(){
        LogUtils.d("Request location update")
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            mLocationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onConnectionSuspended(p0: Int) {
        LogUtils.d("onConnectionSuspended $p0")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        LogUtils.d("onConnectionFailed $p0")
    }


}