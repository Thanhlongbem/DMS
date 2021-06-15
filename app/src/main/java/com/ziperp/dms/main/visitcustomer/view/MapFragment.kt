package com.ziperp.dms.main.visitcustomer.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
import com.ziperp.dms.common.model.MarkerItem
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.core.tracking.LocationTrackingManager
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.ifEmptyLetBe
import com.ziperp.dms.main.saleorder.modify.SalesOrderModifyActivity
import com.ziperp.dms.main.visitcustomer.model.ItemMarker
import com.ziperp.dms.main.visitcustomer.viewmodel.VisitCustomerViewModel
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.DataConvertUtils
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_map.*


class MapFragment : BaseFragment(), OnMapReadyCallback, OnCameraIdleListener,
    ClusterManager.OnClusterClickListener<MarkerItem>,
    ClusterManager.OnClusterInfoWindowClickListener<MarkerItem>,
    ClusterManager.OnClusterItemClickListener<MarkerItem>,
    ClusterManager.OnClusterItemInfoWindowClickListener<MarkerItem> {

    companion object {
        val TAG = MapFragment::class.java.canonicalName
        fun newInstance()  = MapFragment()
    }

    private lateinit var ggMapfr: SupportMapFragment
    private lateinit var ggMap: GoogleMap
    private lateinit var mClusterManager: ClusterManager<MarkerItem>
    private lateinit var clickedClusterItem: MarkerItem
    private var listItemMarker: MutableList<ItemMarker> = arrayListOf()

    val saleOrderActivity by lazy { activity as SalesOrderModifyActivity }
    val viewModel: VisitCustomerViewModel by activityViewModels()

    override fun setLayoutId(): Int = R.layout.fragment_map

    override fun initView() {

        viewModel.visitCustomerMapLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    LogUtils.d("Data size ${it.data?.data?.size})")
                    it.data?.apply {
                        data.filter {
                            it.posLat.isNotEmpty() && it.posLng.isNotEmpty()
                        }
                            .map { customer ->
                                val item = ItemMarker(
                                    customer.cstCd,
                                    customer.posLat.toDouble(),
                                    customer.posLng.toDouble(),
                                    customer.txtCstNm,
                                    customer.txtAddress,
                                    customer.repPerson,
                                    customer.txtPhone,
                                    customer.distance.toString(),
                                    false
                                )
                                if (!listItemMarker.contains(item)) {
                                    listItemMarker.add(item)
                                }
                            }
                    }


                    if (listItemMarker.isNotEmpty()) {
                        addMarkers()
                        ggMap.moveCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                AppUtil.getFocusPoint2(
                                    listItemMarker
                                ), 300
                            )
                        )
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

        viewModel.updateVisitCustomerState.observe(this, Observer {
            when (it) {
                VisitCustomerViewModel.CLEAR_DATA_STATE-> {
                    // Unset all markers
                    mClusterManager.clearItems()
                    listItemMarker.clear()
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

        mClusterManager = ClusterManager<MarkerItem>(context, ggMap)
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
        itemMarkers: List<ItemMarker>
    ) {
        val markers = itemMarkers.map {
            return@map MarkerItem(
                it.lat, it.lng, it.txtCstNm,
                "", R.drawable.icon_eliott,
                it.txtAddress, it.repPerson,
                it.txtPhone, it.distanceValue, it.isCurrentPosition
            )
        }
        mClusterManager.addItems(markers)
        mClusterManager.cluster()
        mClusterManager.markerCollection.setInfoWindowAdapter(MyCustomAdapterForItems())
    }

    private fun addMarkers() {
        listItemMarker.forEach { customer ->
            val currentLocation = LocationTrackingManager.lastLocation
            val lat = customer.lat
            val lng = customer.lng
            val distanceValue = currentLocation?.let { location ->
                return@let AppUtil.distanceBetweenTwoPointInKm(
                    location.latitude,
                    location.longitude,
                    lat,
                    lng
                ).format() + " km"
            } ?: "N/A"
            customer.distanceValue = distanceValue

        }

        addMarkerData(listItemMarker)
    }

    private inner class MarkerRenderer @SuppressLint("InflateParams") constructor(
        context: Context?,
        map: GoogleMap?,
        clusterManager: ClusterManager<MarkerItem>?
    ) : DefaultClusterRenderer<MarkerItem>(context, map, clusterManager) {
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

        override fun onBeforeClusterItemRendered(person: MarkerItem, markerOptions: MarkerOptions) {
            // Draw a single person - show their profile photo and set the info window to show their name
            markerOptions
                .icon(getItemIcon())
                .title(person.title)
        }

        override fun onClusterItemUpdated(person: MarkerItem, marker: Marker) {
            // Same implementation as onBeforeClusterItemRendered() (to update cached markers)
            marker.setIcon(getItemIcon())
            marker.title = person.title
        }

        override fun onBeforeClusterRendered(
            cluster: Cluster<MarkerItem>,
            markerOptions: MarkerOptions
        ) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            markerOptions.icon(getClusterIcon(cluster))
        }

        override fun onClusterUpdated(cluster: Cluster<MarkerItem>, marker: Marker) {
            // Same implementation as onBeforeClusterRendered() (to update cached markers)
            marker.setIcon(getClusterIcon(cluster))
        }

        override fun shouldRenderAsCluster(cluster: Cluster<MarkerItem>): Boolean {
            // Always render clusters.
            return cluster.size > 1
        }


        private fun getItemIcon(): BitmapDescriptor {
            mImageView.setImageResource(R.drawable.icon_location_blue)
            val icon = mIconGenerator.makeIcon()
            return BitmapDescriptorFactory.fromBitmap(icon)
        }

        private fun getClusterIcon(cluster: Cluster<MarkerItem>): BitmapDescriptor {
            val icon = mClusterIconGenerator.makeIcon(cluster.size.toString())
            return BitmapDescriptorFactory.fromBitmap(icon)
        }
    }

    inner class MyCustomAdapterForItems internal constructor() : InfoWindowAdapter {
        @SuppressLint("InflateParams")
        private val myContentsView: View = layoutInflater.inflate(R.layout.info_window, null)

        @SuppressLint("SetTextI18n")
        override fun getInfoWindow(marker: Marker?): View {
            val tvCustomerName: TextView =
                myContentsView.findViewById(R.id.txt_customer_name) as TextView
            val tvAddress: TextView = myContentsView.findViewById(R.id.tvAddress) as TextView
            val tvRep: TextView = myContentsView.findViewById(R.id.txt_rep) as TextView
            val tvPhone: TextView = myContentsView.findViewById(R.id.tvPhone) as TextView
            val tvDistance: TextView = myContentsView.findViewById(R.id.tvDistance) as TextView

            tvCustomerName.text = DataConvertUtils.convertTitle(clickedClusterItem.title)
            tvAddress.text = clickedClusterItem.getAddress().ifEmptyLetBe("N/A")
            tvRep.text = clickedClusterItem.getRepName().ifEmptyLetBe("N/A")
            tvPhone.text = clickedClusterItem.getPhone().ifEmptyLetBe("N/A")
            tvDistance.text = clickedClusterItem.getDistance().ifEmptyLetBe("N/A")

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

    override fun onClusterClick(cluster: Cluster<MarkerItem>): Boolean {
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

    override fun onClusterInfoWindowClick(cluster: Cluster<MarkerItem>?) {
    }

    override fun onClusterItemClick(item: MarkerItem?): Boolean {
        if (item != null) {
            clickedClusterItem = item
        }
        return false
    }

    override fun onClusterItemInfoWindowClick(item: MarkerItem?) {

    }
}