package com.ziperp.dms.core.tracking

import android.location.Location
import android.os.Build
import com.google.gson.Gson
import com.ziperp.dms.App
import com.ziperp.dms.common.model.TrackingResponse
import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.core.rest.TrackingRequest
import com.ziperp.dms.core.service.TrackingService
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.dao.TrackingRequestDao
import com.ziperp.dms.extensions.ifLet
import com.ziperp.dms.extensions.toDateTimeFullString
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.LogUtils
import com.ziperp.dms.utils.NetWorkConnection
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.*

class LocationRepository(val service: TrackingService, val trackingDao: TrackingRequestDao) {

    fun postTrackingLocation(location: Location, address: String, battery: Int): Single<TrackingResponse> {
        LogUtils.d("Post lat long $address")
        val deviceName = Build.MANUFACTURER + " " + Build.MODEL

        val currentTime = Date().toDateTimeFullString()
        val activity = ActivityRecognitionManager.lastActivityValue
        val trackingRequest = TrackingRequest(
            DmsUserManager.userInfo.staffId,
            currentTime,
            address,
            location.latitude.toString(),
            location.longitude.toString(),
            "",
            "A",
            activity.toString(),
            battery.toString(),
            deviceName
        )
        val pJSONData = Gson().toJson(trackingRequest)
        val bodyRequest = BodyRequest(
            "SP_Save_qryDStaffTracking",
            DmsUserManager.userInfo.userId,
            "0",
            "",
            "qryDStaffTracking",
            "A",
            pJSONData
        )

        LogUtils.d("Post JSON " + Gson().toJson(bodyRequest))

        return service.postTrackingLocation(request = bodyRequest)
            .doOnError{
                saveRequestToDB(trackingRequest)
            }
    }

    fun syncBatchTrackingLocation(trackingRequests: List<TrackingRequest>): Observable<Int> {
        if(trackingRequests.isEmpty() || !NetWorkConnection.isNetworkAvailable()) return Observable.empty()
        return Observable.fromIterable(trackingRequests.chunked(50))
            .flatMap { syncTrackingLocation(it).toObservable()}
    }

    fun syncAllData(): Single<Int>{
        return trackingDao.getAllTrackingRequest()
            .flatMap { syncBatchTrackingLocation(it).toList() }
            .map {Constants.CUD.STATE_SUCCESS }
    }

    private fun syncTrackingLocation(trackingRequests: List<TrackingRequest>): Single<Int>{
        //Packetizing 50 request in one time

        trackingRequests.forEach {
            if(it.LatPos.isNotBlank() && it.LngPos.isNotBlank() && it.PosName.isBlank()){
                ifLet(it.LatPos.toDouble(), it.LngPos.toDouble()){ lat, long ->
                    it.PosName = AppUtil.getAddrLatLng(App.shared(), lat, long) ?: " "
                }
            }
        }


        val splitPara = "!$"

        val batchTrackingRequest = trackingRequests.reduce{request1, request2 ->
             var result = TrackingRequest()
            result.TimeLogPos = request1.TimeLogPos + splitPara + request2.TimeLogPos
            result.PosName = request1.PosName + splitPara + request2.PosName
            result.LatPos = request1.LatPos + splitPara + request2.LatPos
            result.LngPos = request1.LngPos + splitPara + request2.LngPos
            result.TypeCheckin = request1.TypeCheckin + splitPara + request2.TypeCheckin
            result.MoveSts = request1.MoveSts + splitPara + request2.MoveSts
            result.BatteryPer = request1.BatteryPer + splitPara + request2.BatteryPer
            result.DeviceName = request1.DeviceName + splitPara + request2.DeviceName
             return@reduce result
        }
        batchTrackingRequest.StaffId = DmsUserManager.userInfo.staffId
        batchTrackingRequest.SplitPara = splitPara
        batchTrackingRequest.NumbRecord = trackingRequests.size.toString()

        val pJSONData = Gson().toJson(batchTrackingRequest)
        val bodyRequest = BodyRequest(
            "SP_Save_Multi_StaffTracking",
            DmsUserManager.userInfo.userId,
            "0",
            "",
            "qryDStaffTracking",
            "A",
            pJSONData
        )

        return service.postBatchTrackingLocation(request = bodyRequest)
            .map {
                if(it.table.isNotEmpty()) Constants.CUD.STATE_SUCCESS else Constants.CUD.STATE_ERROR
            }.flatMap { result ->
                deleteBatchRequests(trackingRequests)
                    .map { result }
                    .onErrorResumeNext { Single.just(result) }
            }
            .onErrorResumeNext{Single.just(Constants.CUD.STATE_ERROR)}
    }

    private fun saveRequestToDB(trackingRequest: TrackingRequest){
        trackingDao.insert(trackingRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                LogUtils.d("Add data successfully")
            },{
                LogUtils.d("Error ")
            })
    }

    private fun deleteBatchRequests(trackingRequests: List<TrackingRequest>): Single<Unit> {
        return Single.fromCallable {trackingDao.delete(trackingRequests)}
    }

}