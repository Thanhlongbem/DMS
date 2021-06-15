package com.ziperp.dms.main.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.PopupWindow
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.maps.model.LatLng
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.common.activity.SettingsActivity
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.core.tracking.AutoSyncManager
import com.ziperp.dms.core.tracking.AutoSyncWorker
import com.ziperp.dms.core.tracking.LocationTrackingManager
import com.ziperp.dms.core.tracking.ServiceUtils
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.toast
import com.ziperp.dms.main.customer.list.view.CustomerActivity
import com.ziperp.dms.main.itemmaster.view.ItemMasterActivity
import com.ziperp.dms.main.personalreport.PersonalReportsActivity
import com.ziperp.dms.main.saleorder.view.SaleOrderActivity
import com.ziperp.dms.main.salespricepromotion.view.SalesPricePromotionActivity
import com.ziperp.dms.main.timekeeping.view.TimekeepingActivity
import com.ziperp.dms.main.trackingreports.TrackingReportsActivity
import com.ziperp.dms.main.user.view.MoreActivity
import com.ziperp.dms.main.visitcustomer.view.VisitedCustomerActivity
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.LogUtils
import com.ziperp.dms.utils.NetWorkConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.popup_menu_sync.view.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest


class HomeActivity : BaseActivity() {

    lateinit var popupWindow: PopupWindow

    private val viewModel by lazy { getViewModel { HomeViewModel() } }

    private val homeAdapter = HomeAdapter()

    override fun setLayoutId() = R.layout.activity_home

    override fun initView() {
        configureToolbar()
        tv_username.text = String.format(
            R.string.str_hello_user.getString(),
            DmsUserManager.userInfo.staffNm
        )
        homeAdapter.onClickListener = { position ->
            when (position) {
                VISITED_CUSTOMER -> startActivity(Intent(this, VisitedCustomerActivity::class.java))
                SALE_ORDER -> startActivity(Intent(this, SaleOrderActivity::class.java))
                TIMEKEEPING -> startActivity(Intent(this, TimekeepingActivity::class.java))
                ITEM_MASTER -> startActivity(Intent(this, ItemMasterActivity::class.java))
                SALE_PRICE -> startActivity(Intent(this, SalesPricePromotionActivity::class.java))
                CUSTOMER -> startActivity(Intent(this, CustomerActivity::class.java))
                TRACKING -> startActivity(Intent(this, TrackingReportsActivity::class.java))
                PERSONAL -> startActivity(Intent(this, PersonalReportsActivity::class.java))
                else -> "Clicked $position".toast(this)
            }

        }

        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = homeAdapter
        recyclerView.setHasFixedSize(true)

        ServiceUtils.startDMSService(this)

    }

    private fun configureToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.title = ""
        toolbar_title.text = getString(R.string.str_home)
        ic_more.setOnClickListener {
            startActivity(Intent(this, MoreActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    companion object {
        const val VISITED_CUSTOMER = 0
        const val SALE_ORDER = 1
        const val TIMEKEEPING = 2
        const val ITEM_MASTER = 3
        const val SALE_PRICE = 4
        const val CUSTOMER = 5
        const val TRACKING = 6
        const val PERSONAL = 7
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        menu?.findItem(R.id.action_setting)?.isVisible = true
        menu?.findItem(R.id.action_sync)?.isVisible = true
        menu?.findItem(R.id.action_notification)?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_setting -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.action_sync -> {
                //showPopupWindow()
                AppUtil.showAlertDialog(this, "Do you want to synchronize all offline data?", {
                    synAllData()
                })
            }
        }
        return true
    }


    override fun onResume() {
        super.onResume()
        AutoSyncManager.checkSyncedData()
        checkPermission()
    }

    private fun synAllData(){
        if(NetWorkConnection.isNetworkAvailable()){
            AutoSyncManager.availableDataSynced()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if(it){
                        AutoSyncManager.autoSync()
                    }else{
                        "Not found data need to be synced".toast()
                    }
                },{
                    "Not found data need to be synced".toast()
                })
        }else{
            "No network connection!".toast()
        }
    }

    private fun showPopupWindow() {
        LocationTrackingManager.getLatestLocation()
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_menu_sync, null)
        popupWindow = PopupWindow(view, WRAP_CONTENT, WRAP_CONTENT)
        popupWindow.elevation = 10.0F

        if (popupWindow.isShowing) {
            popupWindow.dismiss()
        }
        else {
            layout_dim.visibility = View.VISIBLE
            layout_dim.setOnClickListener { popupWindow.dismiss() }
            popupWindow.showAsDropDown(findViewById(R.id.action_sync), -360, -20)
            view.layout_sync.setOnClickListener {

            }
            view.layout_not_sync.setOnClickListener {

            }
        }
        popupWindow.setOnDismissListener { layout_dim.visibility = View.GONE }

    }

    @AfterPermissionGranted(200)
    private fun checkPermission() {
        LogUtils.d("Check permission")
        val permissions = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                arrayOf(
                    Manifest.permission.ACTIVITY_RECOGNITION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else {
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }

        if (EasyPermissions.hasPermissions(this, *permissions)) {
            LogUtils.d("Enough permission")
            checkGPSEnable()
        } else {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(
                    this, 200,
                    *permissions
                ).build()
            )
            LogUtils.d("Request permission")
        }
    }

    private fun checkGPSEnable() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            ServiceUtils.showSettingsAlert(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            LogUtils.d("PickLocationActivity : ${data?.getStringExtra("address")}")
            val latLng = intent.getParcelableExtra("latLng") as? LatLng
        }
    }
}