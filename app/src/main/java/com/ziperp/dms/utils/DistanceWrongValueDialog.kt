package com.ziperp.dms.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import com.ziperp.dms.R
import com.ziperp.dms.core.tracking.LocationTrackingManager
import com.ziperp.dms.core.tracking.ServiceUtils
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.setOnClickNetworkListener
import com.ziperp.dms.main.visitcustomer.model.VisitCustomerItem
import kotlinx.android.synthetic.main.dialog_can_not_checkin.*


class DistanceWrongValueDialog : DialogFragment(), View.OnClickListener, OnMapReadyCallback, GoogleMap.OnCameraIdleListener {
    private lateinit var ggMapfr: SupportMapFragment
    private lateinit var ggMap: GoogleMap

    private var type = TYPE_CHECK_IN
    private lateinit var data: VisitCustomerItem
    private var distance: Double = 0.0
    private var curLat = 0.0
    private var curLng = 0.0
    private lateinit var checkAction: ()-> Unit


    companion object {
        fun newInstance(type: Int, data: VisitCustomerItem, distance: Double, curLat: Double, curLng: Double, checkAction: ()->Unit): DistanceWrongValueDialog {
            val fragment = DistanceWrongValueDialog()
            fragment.type = type
            fragment.data = data
            fragment.distance = distance
            fragment.curLat = curLat
            fragment.curLng = curLng
            fragment.checkAction = checkAction
            return fragment
        }

        const val TYPE_CHECK_IN = 0
        const val TYPE_CHECK_OUT = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.AppTheme_Dialog_Custom)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_can_not_checkin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        img_check.setImageResource(if (type == TYPE_CHECK_IN) R.drawable.ic_flag else R.drawable.ic_checkout_small)
        tv_check.text = if (type == TYPE_CHECK_IN) R.string.str_check_in.getString() else R.string.str_check_out.getString()
        setupView()
        ggMapfr = childFragmentManager.findFragmentById(R.id.fr_Location) as SupportMapFragment
        ggMapfr.getMapAsync(this)
    }


    private fun setupView() {
        layout_check.setOnClickNetworkListener {
            this.dismiss()
            checkAction.invoke()
        }
        layout_relocate.setOnClickListener(this)
        layout_cancel.setOnClickListener(this)
        if (data.posLat.isEmpty() || data.posLng.isEmpty()) {
            tv_warningContent.text = R.string.str_cannot_calculate_distance.getString()
        } else {
            tv_warningContent.text = String.format(R.string.str_invalid_checkin_message.getString(), distance.format())
        }
    }

    private lateinit var ic1: IconGenerator
    private lateinit var ic2: IconGenerator

    private fun addMaker(){
        ic1 = IconGenerator(requireContext())
        ic2 = IconGenerator(requireContext())
        ic1.makeIcon("${R.string.str_you_are_here.getString()}\n")
        ic1.setTextAppearance(requireContext(), R.style.InfoDialog)
        ic2.makeIcon("${R.string.str_customer_position.getString()}\n")
        ic2.setTextAppearance(requireContext(), R.style.InfoDialog)
    }

    private fun getBitmapFromVectorDrawable(
        context: Context?,
        drawableId: Int
    ): Bitmap? {
        var drawable = ContextCompat.getDrawable(requireContext(), drawableId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.ggMap = googleMap ?: return

        ggMap.uiSettings.isMapToolbarEnabled = true
        addMaker()
        ggMap.addMarker(MarkerOptions()
            .icon(BitmapDescriptorFactory.fromBitmap(ic1.makeIcon()))
            .position(LatLng(curLat, curLng))
        ).showInfoWindow()

        ggMap.addMarker(MarkerOptions()
            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(requireContext(),R.drawable.ic_location_blue_small)))
            .position(LatLng(curLat, curLng))
        )

        if (data.posLng.isNotEmpty() && data.posLat.isNotEmpty()) {
            ggMap.addMarker(MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(ic2.makeIcon()))
                .position(LatLng(data.posLat.toDouble(), data.posLng.toDouble()))
            )
            ggMap.addMarker(MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(requireContext(),R.drawable.icon_location_red_small)))
                .position(LatLng(data.posLat.toDouble(), data.posLng.toDouble()))
            ).showInfoWindow()



            ggMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng((curLat + data.posLat.toDouble())/2, (curLng + data.posLng.toDouble())/2), 12f))

            if(distance > 10000){
                val latLngBounds = LatLngBounds.Builder()
                latLngBounds.include(LatLng(curLat, curLng))
                latLngBounds.include(LatLng(data.posLat.toDouble(), data.posLng.toDouble()))
                ggMap.setOnMapLoadedCallback {
                    ggMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 300))
                }
            }

        } else {
            ggMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(curLat, curLng), 12f))
        }

    }

    override fun onCameraIdle() {

    }

    override fun onClick(v: View?) {
        when (v) {
            layout_relocate -> {
                addMaker()
                ggMap.clear()
                if(ServiceUtils.isGPSEnable(requireContext())){
                    LocationTrackingManager.lastLocation?.let {
                        if(data.posLat.isNotEmpty() && data.posLng.isNotEmpty()){
                            val distanceValue = AppUtil.distanceBetweenTwoPointInMeter(
                                it.latitude,
                                it.longitude,
                                data.posLat.toDouble(),
                                data.posLng.toDouble())
                            ggMap.addMarker(MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromBitmap(ic1.makeIcon()))
                                .position(LatLng(it.latitude, it.longitude))
                            ).showInfoWindow()

                            ggMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 12f))

                            ggMap.addMarker(MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromBitmap(ic2.makeIcon()))
                                .position(LatLng(data.posLat.toDouble(), data.posLng.toDouble()))
                            ).showInfoWindow()

                            ggMap.addMarker(MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(requireContext(),R.drawable.ic_location_blue_small)))
                                .position(LatLng(it.latitude, it.longitude))
                            )

                            ggMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 12f))

                            ggMap.addMarker(MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(requireContext(),R.drawable.icon_location_red_small)))
                                .position(LatLng(data.posLat.toDouble(), data.posLng.toDouble()))
                            )
                            tv_warningContent.text = String.format(R.string.str_invalid_checkin_message.getString(), distanceValue.toInt())
                        }
                    } ?: run{
                        tv_warningContent.text = R.string.str_cannot_calculate_distance.getString()
                    }
                } else {
                    Toast.makeText(context , "You need to turn on GPS", Toast.LENGTH_LONG).show()
                    ServiceUtils.showSettingsAlert(requireContext())
                }
            }
            layout_cancel -> this.dismiss()
        }
    }
}