package com.ziperp.dms.main.visitcustomer.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ziperp.dms.AppConfiguration
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.core.tracking.LocationTrackingManager
import com.ziperp.dms.core.tracking.ServiceUtils
import com.ziperp.dms.utils.AppConstant
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.NetWorkConnection
import kotlinx.android.synthetic.main.fragment_location.*
import java.util.*

class LocationFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener {
    companion object {
        fun newInstance() =  LocationFragment()
    }

    private lateinit var ggMapfr: SupportMapFragment
    private lateinit var ggMap: GoogleMap

    var locationName: String = ""
    var latLng: LatLng? = null

    var isGetLocation: Boolean = true
    var isDisableMap: Boolean = false
    var isDisableSearchBox: Boolean = false


    override fun setLayoutId(): Int = R.layout.fragment_location

    override fun initView() {
        ggMapfr = childFragmentManager.findFragmentById(R.id.frLocation) as SupportMapFragment

        btn_reset_location.setOnClickListener {
            search_box.text = ""
        }
        search_box.addTextChangedListener {
            if(isGetLocation){
                btn_reset_location.visibility = if (it.toString().isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
        updateMap()
    }

    fun updateMap(){

        search_box.text = locationName

        if(isDisableMap){
            layout_invalid_map.visibility = View.VISIBLE
        }else{
            layout_invalid_map.visibility = View.GONE
            ggMapfr.getMapAsync(this)
        }

        if(isDisableSearchBox){
            layout_search.visibility = View.GONE
        }else{
            layout_search.visibility = View.VISIBLE
        }

        if(isGetLocation){
            btn_getLocation.visibility = View.VISIBLE
        }else{
            btn_getLocation.visibility = View.GONE
        }
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        this.ggMap = googleMap ?: return
        ggMap.uiSettings.isMapToolbarEnabled = true

        btn_getLocation.setOnClickListener {
            requestCurrentLocation()
        }

        latLng?.let {
            ggMap.addMarker(
                MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(AppUtil.getBitmap(R.drawable.ic_location_pin1, requireContext())))
                    .position(LatLng(it.latitude, it.longitude))
            )
            ggMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        it.latitude,
                        it.longitude
                    ), AppConfiguration.MAP_ZOOM_LEVEL
                )
            )
        }

    }

    override fun onCameraIdle() {
    }


    private fun requestCurrentLocation() {
        LocationTrackingManager.lastLocation?.let {
            ggMap.clear()

            latLng = LatLng(
                it.latitude,
                it.longitude
            )
            ggMap.addMarker(
                MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(AppUtil.getBitmap(R.drawable.ic_location_pin1, requireContext())))
                    .position(latLng!!)
            )
            ggMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    latLng!!, AppConfiguration.MAP_ZOOM_LEVEL
                )
            )
            search_box.text = AppUtil.getAddrLatLng(context, it.latitude, it.longitude)
            locationName = search_box.text.toString()
        }

    }

}