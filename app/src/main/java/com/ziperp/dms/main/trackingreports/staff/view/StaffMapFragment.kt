package com.ziperp.dms.main.trackingreports.staff.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.common.model.StaffMarkerItem
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.core.tracking.LocationTrackingManager
import com.ziperp.dms.extensions.format
import com.ziperp.dms.main.saleorder.modify.SalesOrderModifyActivity
import com.ziperp.dms.main.trackingreports.staff.model.StaffMarker
import com.ziperp.dms.main.trackingreports.staff.viewmodel.TrackingStaffViewModel
import com.ziperp.dms.main.visitcustomer.viewmodel.VisitCustomerViewModel
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.DataConvertUtils
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.item_staff_marker.view.*


class StaffMapFragment : BaseFragment(), OnMapReadyCallback, OnCameraIdleListener,
    ClusterManager.OnClusterClickListener<StaffMarkerItem>,
    ClusterManager.OnClusterInfoWindowClickListener<StaffMarkerItem>,
    ClusterManager.OnClusterItemClickListener<StaffMarkerItem>,
    ClusterManager.OnClusterItemInfoWindowClickListener<StaffMarkerItem> {

    companion object {
        val TAG = StaffMapFragment::class.java.canonicalName
        fun newInstance()  = StaffMapFragment()
    }

    private lateinit var ggMapfr: SupportMapFragment
    private lateinit var ggMap: GoogleMap
    private lateinit var mClusterManager: ClusterManager<StaffMarkerItem>
    private lateinit var clickedClusterItem: StaffMarkerItem
    private var listStaffMarker: MutableList<StaffMarker> = arrayListOf()

    val visitActivity by lazy { activity as SalesOrderModifyActivity }
    val viewModel: TrackingStaffViewModel by activityViewModels()

    override fun setLayoutId(): Int = R.layout.fragment_map

    override fun initView() {

        viewModel.staffListMapData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.apply {
                        listStaff.filter {
                            it.latPos.isNotEmpty() && it.lngPos.isNotEmpty()
                        }
                        .map { staff ->
                            staff.apply {
                                val item = StaffMarker(staffId, latPos.toDouble(),
                                    lngPos.toDouble(), staffNm, logPosTime, typeCheckinNm)
                                if(!listStaffMarker.contains(item)) {
                                    listStaffMarker.add(item)
                                }
                            }
                        }
                    }
                    if(listStaffMarker.isNotEmpty()){
                        addMarkers()
                        ggMap.moveCamera(CameraUpdateFactory.newLatLngBounds(AppUtil.getFocusStaffPoint(listStaffMarker), 300))
                    }
                }
                Status.LOADING -> {
                    loading_progressbar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    loading_progressbar.visibility = View.GONE
                }
            }
        })

        viewModel.updateStaffState.observe(this, Observer {
            when (it) {
                VisitCustomerViewModel.CLEAR_DATA_STATE-> {
                    // Unset all markers
                    mClusterManager.clearItems()
                    listStaffMarker.clear()
                    mClusterManager.renderer = MarkerRenderer(context, ggMap, mClusterManager)
                }
            }
        })

        ggMapfr = childFragmentManager.findFragmentById(R.id.frMap) as SupportMapFragment
        ggMapfr.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.ggMap = googleMap ?: return
        ggMap.uiSettings.isMapToolbarEnabled = true
//        ggMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(16.0717635, 107.9380385), 6f))

        mClusterManager = ClusterManager<StaffMarkerItem>(context, ggMap)
        mClusterManager.renderer = MarkerRenderer(context, ggMap, mClusterManager)

        ggMap.setOnCameraIdleListener(mClusterManager)
        ggMap.setOnMarkerClickListener(mClusterManager)
        ggMap.setOnInfoWindowClickListener(mClusterManager)
        mClusterManager.setOnClusterClickListener(this)
        mClusterManager.setOnClusterInfoWindowClickListener(this)
        mClusterManager.setOnClusterItemClickListener(this)
        mClusterManager.setOnClusterItemInfoWindowClickListener(this)
        mClusterManager.setAnimation(true)

    }

    override fun onCameraIdle() {
        LogUtils.d(
            "onCameraIdle - " + AppUtil.getAddrLatLng(
                context,
                ggMap.cameraPosition.target.latitude,
                ggMap.cameraPosition.target.longitude
            )
        )
    }


    private fun addMarkerData(
        staffMarker: StaffMarker
    ) {
        staffMarker.let {
            val offsetItem = StaffMarkerItem(it.lat, it.lng, it.staffNm, it.logPosTime, it.checkInType)
            mClusterManager.addItem(offsetItem)
            mClusterManager.cluster()
            mClusterManager.markerCollection.setInfoWindowAdapter(MyCustomAdapterForItems())
        }

    }

    private fun addMarkers() {
        listStaffMarker.forEach { staff ->
            val lat = staff.lat
            val lng = staff.lng
            val distanceValue = LocationTrackingManager.lastLocation?.let { location ->
                return@let AppUtil.distanceBetweenTwoPointInKm(
                    location.latitude,
                    location.longitude,
                    lat,
                    lng
                ).format() + " km"
            } ?: "N/A"
            addMarkerData(staff)
        }
    }

    private inner class MarkerRenderer @SuppressLint("InflateParams") constructor(
        context: Context?,
        map: GoogleMap?,
        clusterManager: ClusterManager<StaffMarkerItem>?
    ) : DefaultClusterRenderer<StaffMarkerItem>(context, map, clusterManager) {
        private val mIconGenerator = IconGenerator(context)
        private val mClusterIconGenerator = IconGenerator(context)
        private var mImageView: ImageView

        init {
            val multiProfile = layoutInflater.inflate(R.layout.multi_profile, null)
            mClusterIconGenerator.setContentView(multiProfile)
            mClusterIconGenerator.setColor(Color.TRANSPARENT)
            mImageView = ImageView(context)
            mImageView.layoutParams = ViewGroup.LayoutParams(70, 70)
            mIconGenerator.setContentView(mImageView)
            mIconGenerator.setColor(Color.TRANSPARENT)
        }

        override fun onBeforeClusterItemRendered(person: StaffMarkerItem, markerOptions: MarkerOptions) {
            // Draw a single person - show their profile photo and set the info window to show their name
            markerOptions
                .icon(getItemIcon())
                .title(person.title)
        }

        override fun onClusterItemUpdated(person: StaffMarkerItem, marker: Marker) {
            // Same implementation as onBeforeClusterItemRendered() (to update cached markers)
            marker.setIcon(getItemIcon())
            marker.title = person.title
        }

        override fun onBeforeClusterRendered(
            cluster: Cluster<StaffMarkerItem>,
            markerOptions: MarkerOptions
        ) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            markerOptions.icon(getClusterIcon(cluster))
        }

        override fun onClusterUpdated(cluster: Cluster<StaffMarkerItem>, marker: Marker) {
            // Same implementation as onBeforeClusterRendered() (to update cached markers)
            marker.setIcon(getClusterIcon(cluster))
        }

        override fun shouldRenderAsCluster(cluster: Cluster<StaffMarkerItem>): Boolean {
            // Always render clusters.
            return cluster.size > 1
        }


        private fun getItemIcon(): BitmapDescriptor {
            mImageView.setImageResource(R.drawable.icon_location_blue)
            val icon = mIconGenerator.makeIcon()
            return BitmapDescriptorFactory.fromBitmap(icon)
        }

        private fun getClusterIcon(cluster: Cluster<StaffMarkerItem>): BitmapDescriptor {
            val icon = mClusterIconGenerator.makeIcon(cluster.size.toString())
            return BitmapDescriptorFactory.fromBitmap(icon)
        }
    }

    inner class MyCustomAdapterForItems internal constructor() : InfoWindowAdapter {
        @SuppressLint("InflateParams")
        private val myContentsView: View = layoutInflater.inflate(R.layout.item_staff_marker, null)

        @SuppressLint("SetTextI18n")
        override fun getInfoWindow(marker: Marker?): View {
            myContentsView.apply {
                tv_checkin_type.text = DataConvertUtils.convertTitle(clickedClusterItem.checkInType)
                tv_time.text = clickedClusterItem.logPosTime
                tv_staff_name.text = DataConvertUtils.convertTitle(clickedClusterItem.staffNm)
            }

            ggMap.setOnInfoWindowClickListener {
                marker?.let{directionMap(it)}
            }

            return myContentsView
        }

        override fun getInfoContents(marker: Marker?): View? {
            return null
        }

        private fun directionMap(marker: Marker){
            marker.hideInfoWindow()
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=${LocationTrackingManager.lastLocation?.latitude}," +
                        "${LocationTrackingManager.lastLocation?.longitude}" +
                        "&daddr=${clickedClusterItem.position.latitude},${clickedClusterItem.position.longitude}")
            )
            startActivity(intent)
        }

    }

    override fun onClusterClick(cluster: Cluster<StaffMarkerItem>): Boolean {
        val builder = LatLngBounds.builder()
        for (item in cluster.items) {
            builder.include(item.position)
        }
        val bounds = builder.build()
        try {
            ggMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    override fun onClusterInfoWindowClick(cluster: Cluster<StaffMarkerItem>?) {
    }

    override fun onClusterItemClick(item: StaffMarkerItem?): Boolean {
        if (item != null) {
            clickedClusterItem = item
        }
        return false
    }

    override fun onClusterItemInfoWindowClick(item: StaffMarkerItem?) {

    }
}