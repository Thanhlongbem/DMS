package com.ziperp.dms.common.activity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import kotlinx.android.synthetic.main.activity_pick_location.*
import java.util.*

class PickLocationActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    private val TAG = PickLocationActivity::class.java.simpleName

    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    private var latLng: LatLng? = null
    private var address: String = ""

    override fun setLayoutId(): Int = R.layout.activity_pick_location

    override fun initView() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (intent.hasExtra("latLng")) {
            latLng = intent.getParcelableExtra("latLng")
        }
        if (intent.hasExtra("address")) {
            address = intent.getStringExtra("address")
            txt_address.text = address
        }

        setOnClickListener(but_confirm, img_back, but_my_location)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.but_confirm -> {
                val pinnedPosition = mMap.cameraPosition.target
                val address =
                    getCompleteAddressString(pinnedPosition.latitude, pinnedPosition.longitude)
                Log.d(TAG, "Address $address")
                intent.putExtra("address", address)
                intent.putExtra("latLng", pinnedPosition)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

            R.id.img_back -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }

            R.id.but_my_location ->{
                fetchLocation()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnCameraIdleListener(this)
        latLng?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        } ?: run {
            // Add a marker in Sydney and move the camera
            val sydney = LatLng(-34.0, 151.0)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }
    }



    private fun getCompleteAddressString(lat: Double, long: Double): String {
        var result = "N/A"
        val geoCoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address>? = geoCoder.getFromLocation(lat, long, 1)
            if (addresses != null) {
                val returnedAddress: Address = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ")
                }
                result = strReturnedAddress.toString()
                tv_choosedLocation.text = result
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode
            )
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        Toast.makeText(applicationContext, "Fetch last location ...", Toast.LENGTH_SHORT)
        task.addOnSuccessListener { location ->
            if (location != null) {
                Toast.makeText(applicationContext, "Fetch last location success", Toast.LENGTH_SHORT)
                    .show()
                currentLocation = location
                moveToCurrent()
            }else{
                Toast.makeText(applicationContext, "Couldn't fetch last location", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun moveToCurrent() {
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {
                fetchLocation()
            }
        }
    }

    override fun onCameraIdle() {
        getCompleteAddressString(mMap.cameraPosition.target.latitude, mMap.cameraPosition.target.longitude)
    }

}