package com.ziperp.dms.main.timekeeping.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.App
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.core.rest.Constants.CUD.TYPE_CREATE
import com.ziperp.dms.core.rest.Constants.CUD.TYPE_UPDATE
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.core.tracking.ActivityRecognitionManager
import com.ziperp.dms.core.tracking.ErrorLocationDialog
import com.ziperp.dms.core.tracking.LocationTrackingManager
import com.ziperp.dms.core.tracking.ServiceUtils
import com.ziperp.dms.extensions.*
import com.ziperp.dms.main.timekeeping.model.SaveTimeKeepingRequest
import com.ziperp.dms.main.timekeeping.model.TimeKeeping
import com.ziperp.dms.main.user.view.CachedDataActivity
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.LogUtils
import com.ziperp.dms.utils.NetWorkConnection
import kotlinx.android.synthetic.main.activity_time_keeping.*
import java.util.*

class TimekeepingActivity: BaseActivity() {

    private val viewModel by lazy {
        getViewModel { Injection.provideTimeKeepingViewModel() }
    }

    lateinit var adapter: TimeKeepingAdapter

    override fun setLayoutId(): Int = R.layout.activity_time_keeping

    override fun initView() {
        setToolbar(R.string.str_time_keeping.getString(), true)

        tv_date.text = Date().toSlashDateString()
        adapter = TimeKeepingAdapter()
        rv_timekeepingItem.layoutManager = LinearLayoutManager(applicationContext)
        rv_timekeepingItem.setHasFixedSize(true)
        rv_timekeepingItem.adapter = adapter
        dataObserver()
        setUpTimer()
    }

    private fun setUpTimer() {

        btn_start_finish.setOnClickListener {
            if (ServiceUtils.isGPSEnable(this)){
                val location = LocationTrackingManager.lastLocation
                val latitude = location?.latitude
                val longitude = location?.longitude

                ifLet(latitude, longitude) { lat, long ->
                    val locate = AppUtil.getAddrLatLng(App.shared(), lat, long)?: ""
                    var dialog: NoteDialog
                    if (!it.isSelected) {
                        dialog = NoteDialog(this, CHECK_IN_MODE, locate)
                        dialog.onConfirmed =  { note ->
                            requestSave(TYPE_CREATE, lat, long, note)
                            btn_start_finish.isSelected = true
                            btn_start_finish.text = R.string.str_finish.getString()
                            timer.base = SystemClock.elapsedRealtime()
                            timer.start()
                        }
                    } else {
                        dialog = NoteDialog(this, CHECK_OUT_MODE, locate)
                        dialog.onConfirmed = { note ->
                            requestSave(TYPE_UPDATE, lat, long, note)
                            btn_start_finish.isSelected = false
                            btn_start_finish.text = R.string.str_start.getString()
                            timer.stop()
                            timer.text = "00:00"
                        }
                    }
                    dialog.show()
                } ?:run {
                    ErrorLocationDialog(this).show()
                }
            }else{
                ServiceUtils.showSettingsAlert(this)
            }

        }
    }

    private fun continueTimer(listData: List<TimeKeeping>) {
        if (listData.isNotEmpty() &&
            listData.first().checkInDay.isNotBlank() &&
            listData.first().checkInTime.isNotBlank() &&
            listData.first().checkOutTime.isBlank()
        ) {
            if(btn_start_finish.isSelected) return // Ignore if timer is running

            val startedTime = listData.first().checkInDay + "" + listData.first().checkInTime
            val today = Date()
            var diff = 0L
            startedTime.toDateTimeFull()?.let {
                diff = (today.time - it.time)
            }

            timer.base = SystemClock.elapsedRealtime() - diff
            btn_start_finish.isSelected = true
            btn_start_finish.text = R.string.str_finish.getString()
            timer.start()
        }
    }

    private fun requestSave(type: String, latitude: Double, longitude: Double, note: String) {

        val dateTimeLogPos = Date()
        val timeKeeping = if (type == TYPE_UPDATE && adapter.data.isNotEmpty()) adapter.data.first() else TimeKeeping()

        when (type) {
            TYPE_CREATE -> {
                timeKeeping.apply {
                    staffId = DmsUserManager.userInfo.staffId
                    staffNm = DmsUserManager.userInfo.staffNm

                    timeKeepNo = "Cached_"+ System.currentTimeMillis()
                    checkInDay = dateTimeLogPos.toDateString()
                    checkInTime = dateTimeLogPos.toHourMinuteSecond()
                    checkInLatPos = "$latitude"
                    checkInLngPos = "$longitude"
                    checkInPosNm = AppUtil.getAddrLatLng(applicationContext, latitude, longitude) ?: ""
                    remarkCheckin = note
                    moveStsCheckIn = ActivityRecognitionManager.lastActivityValue.toString()
                    batteryPerCheckIn = ServiceUtils.getBatteryLevel(applicationContext).toInt()
                    isSynchonizedCheckIn = false
                }
            }
            TYPE_UPDATE -> {
                timeKeeping.apply {
                    staffId = DmsUserManager.userInfo.staffId
                    staffNm = DmsUserManager.userInfo.staffNm

                    checkOutDay = dateTimeLogPos.toDateString()
                    checkOutTime = dateTimeLogPos.toHourMinuteSecond()
                    checkOutLatPos = "$latitude"
                    checkOutLngPos = "$longitude"
                    checkOutPosNm = AppUtil.getAddrLatLng(applicationContext, latitude, longitude) ?: ""
                    remarkCheckout = note
                    moveStsCheckOut = ActivityRecognitionManager.lastActivityValue.toString()
                    batteryPerCheckOut = ServiceUtils.getBatteryLevel(applicationContext).toInt()
                    isSynchonizedCheckOut = false
                    durationHour = AppUtil.duration(checkInTime, checkOutTime)
                }
            }
        }

        viewModel.saveTimeKeeping(timeKeeping)
    }

    @SuppressLint("SetTextI18n")
    private fun dataObserver() {
        viewModel.timeKeepingLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.let {
                        val sortedList = it.sortedByDescending {it.checkInTime.toInt()}
                        adapter.updateData(sortedList)
                        continueTimer(sortedList)
                        var totalDuration = 0.0
                        sortedList.forEach { totalDuration += it.durationHour }
                        tv_totalTime.text = "${totalDuration.format()} Hours"
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

        viewModel.timeKeepingCountLiveData.observe(this, Observer {
            LogUtils.d("Synchonize success data at " + it)
        })

        viewModel.getTimeKeepingList(Date().toDateString())

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        menu?.findItem(R.id.action_sync)?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sync -> {
                synchronizeData()
            }
        }
        return true
    }

    private fun synchronizeData(){
        val data = adapter.data.filter {
            !it.isSynchonizedCheckIn || !it.isSynchonizedCheckOut //|| it.isNeedResyncCheckIn() || it.isNeedResyncCheckOut()
        }
        if(data.isEmpty()){
            "No data need to be synchronized".toast()
        }else{
            if(NetWorkConnection.isNetworkAvailable()){
                viewModel.synchronizeData(data)
            }else{
                "Make sure you connected a network".toast()
            }
        }
    }

    private fun listTimeKeeping() {
        startActivity(CachedDataActivity.newInstance(this, 0))
    }

    companion object {
        const val CHECK_IN_MODE = 1
        const val CHECK_OUT_MODE = 2
    }

}