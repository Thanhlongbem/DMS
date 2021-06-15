package com.ziperp.dms.core.repository

import com.google.gson.Gson
import com.ziperp.dms.App
import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.core.rest.Constants.CUD.TYPE_CREATE
import com.ziperp.dms.core.rest.Constants.CUD.TYPE_UPDATE
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.service.TimeKeepingService
import com.ziperp.dms.dao.TimeKeepingDao
import com.ziperp.dms.extensions.execute
import com.ziperp.dms.extensions.ifEmptyLetBe
import com.ziperp.dms.extensions.toDateString
import com.ziperp.dms.main.timekeeping.model.SaveTimeKeepingRequest
import com.ziperp.dms.main.timekeeping.model.TimeKeeping
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.LogUtils
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.*

class TimeKeepingRepository(
    private val timeKeepingService: TimeKeepingService,
    private val timeKeepingDao: TimeKeepingDao
) {

    fun getTimeKeepingList(day: String, request: BodyRequest): Single<List<TimeKeeping>> {
        return Single.zip(
            getTimeKeepingFromApi(request),
            getTimeKeepingInDB(day),
            BiFunction { apiList: List<TimeKeeping>, cachedList: List<TimeKeeping> ->
                val result = arrayListOf<TimeKeeping>()
                val syncList = arrayListOf<TimeKeeping>()

                if (apiList.isNotEmpty()) {
                    result.addAll(apiList)
                    syncList.addAll(apiList)

                    cachedList.forEach { cachedItem ->
                        if (apiList.contains(cachedItem)) {
                            val apiItem = apiList.first { it.timeKeepNo == cachedItem.timeKeepNo }
                            if (!apiItem.isSynchonizedCheckOut &&
                                apiItem.checkOutTime.isBlank() &&
                                cachedItem.checkOutTime.isNotBlank()
                            ) {

                                result.remove(apiItem)
                                syncList.remove(apiItem)
                                result.add(cachedItem)
                            }
                        } else {
                            result.add(cachedItem)
                        }
                    }

                    //Update location
                    result.forEach { it.fetchAddress() }
                    //Replace current data with api data
                    timeKeepingDao.insert(syncList).execute()
                    LogUtils.d("Combine api and cached")
                } else if (cachedList.isNotEmpty()) {
                    result.addAll(cachedList)
                    LogUtils.d("Get from cached")
                }
                return@BiFunction result
            })
    }

    fun saveTimeKeeping(timeKeeping: TimeKeeping): Single<Int> {
        return saveTimeKeepingToDB(timeKeeping)
            .doOnSuccess { timeKeeping.id = Integer(it.toInt()) }
            .flatMap { saveTimeKeepingApi(timeKeeping) } //always success
            .onErrorResumeNext { Single.just(Constants.CUD.STATE_ERROR) }
    }

    fun saveTimeKeepingApi(timeKeeping: TimeKeeping): Single<Int> {// 3 cases: check in, check out, check in-out
        if (timeKeeping.isNeedSyncCheckIn()) {
            return saveTimeKeepingApi(TYPE_CREATE, timeKeeping)
                .flatMap {
                    if (it == Constants.CUD.STATE_SUCCESS && timeKeeping.isNeedSyncCheckOut()) {
                        saveTimeKeepingApi(TYPE_UPDATE, timeKeeping)
                    } else {
                        Single.just(it)
                    }
                }
        } else if (timeKeeping.isNeedSyncCheckOut()) {// sync check out
            return saveTimeKeepingApi(TYPE_UPDATE, timeKeeping)
        } else if (timeKeeping.isNeedResyncCheckIn()) {// resync location check in
            return saveTimeKeepingApi(TYPE_CREATE, timeKeeping)
                .flatMap {
                    if (it == Constants.CUD.STATE_SUCCESS && timeKeeping.isNeedResyncCheckOut()) {
                        saveTimeKeepingApi(TYPE_UPDATE, timeKeeping)
                    } else {
                        Single.just(it)
                    }
                }
        } else if (timeKeeping.isNeedResyncCheckOut()) {// resync location check out
            return saveTimeKeepingApi(TYPE_UPDATE, timeKeeping)
        } else {
            return Single.just(Constants.CUD.STATE_ERROR)
        }
    }

    private fun saveTimeKeepingToDB(timeKeeping: TimeKeeping): Single<Long> {
        return timeKeepingDao.insert(timeKeeping)
    }

    private fun saveTimeKeepingApi(type: String, timeKeeping: TimeKeeping): Single<Int> {
        val request = SaveTimeKeepingRequest()
        when (type) {
            TYPE_CREATE -> {
                var timeKeepingNo = ""
                if (timeKeeping.isValidTimeKeepNo()) {// Purpose for update
                    timeKeepingNo = timeKeeping.timeKeepNo
                }
                request.apply {
                    timeKeepNo = timeKeepingNo
                    staffId = timeKeeping.staffId
                    timeLogPos = timeKeeping.checkInDay + "" + timeKeeping.checkInTime
                    latPos = timeKeeping.checkInLatPos
                    lngPos = timeKeeping.checkInLngPos
                    posName = timeKeeping.checkInPosNm.ifEmptyLetBe(
                        AppUtil.getAddrLatLng(
                            App.shared(),
                            latPos.toDouble(),
                            lngPos.toDouble()
                        ) ?: ""
                    )
                    timeKeeping.checkInPosNm = posName
                    typeCheckin = "T"
                    remark = timeKeeping.remarkCheckin
                    batteryPer = timeKeeping.batteryPerCheckIn
                    moveSts = timeKeeping.moveStsCheckIn
                }
            }
            TYPE_UPDATE -> {
                request.apply {
                    timeKeepNo = timeKeeping.timeKeepNo
                    staffId = timeKeeping.staffId
                    timeLogPos = timeKeeping.checkOutDay + "" + timeKeeping.checkOutTime
                    latPos = timeKeeping.checkOutLatPos
                    lngPos = timeKeeping.checkOutLngPos
                    posName = timeKeeping.checkOutPosNm.ifEmptyLetBe(
                        AppUtil.getAddrLatLng(
                            App.shared(),
                            latPos.toDouble(),
                            lngPos.toDouble()
                        ) ?: ""
                    )
                    timeKeeping.checkOutPosNm = posName
                    typeCheckin = "T"
                    remark = timeKeeping.remarkCheckout
                    batteryPer = timeKeeping.batteryPerCheckOut
                    moveSts = timeKeeping.moveStsCheckOut
                }
            }
        }
        val pJsonData = Gson().toJson(request)
        val bodyRequest =
            BodyRequestFactory.get(RequestType.SAVE_TIME_KEEPING, pJsonData, pPageMode = type)
        return timeKeepingService.saveTimeKeeping(bodyRequest)
            .map {
                LogUtils.d("Save status " + it.isSuccess())
                if (it.isSuccess()) {
                    timeKeeping.timeKeepNo = it.data[0].timeKeepNo
                    when (type) {
                        TYPE_CREATE -> {
                            timeKeeping.isSynchonizedCheckIn = true
                        }
                        TYPE_UPDATE -> {
                            timeKeeping.isSynchonizedCheckOut = true
                        }
                    }
                    //Update db
                    saveTimeKeepingToDB(timeKeeping).execute()
                    return@map Constants.CUD.STATE_SUCCESS
                } else Constants.CUD.STATE_ERROR
            }
            .onErrorResumeNext { Single.just(Constants.CUD.STATE_ERROR) }
    }

    private fun getTimeKeepingFromApi(request: BodyRequest): Single<List<TimeKeeping>> {
        return timeKeepingService
            .getTimeKeepingList(request)
            .map {
                it.data.forEach { item ->
                    item.isSynchonizedCheckIn = true
                    if (item.checkOutTime.isNotBlank()) {
                        item.isSynchonizedCheckOut = true
                    }
                }
                return@map it.data
            }
            .onErrorResumeNext(Single.just(arrayListOf()))
    }

    private fun getTimeKeepingInDB(day: String): Single<List<TimeKeeping>> {
        return timeKeepingDao
            .getAllTimeKeeping(day)
            .onErrorResumeNext {
                Single.just(arrayListOf())
            }
    }


    fun syncAllData(): Single<Int> {
        return getTimeKeepingInDB(Date().toDateString())
            .flatMapObservable { list ->
                LogUtils.d("syncAllData + size " + list.size)
                Observable.fromIterable(list)
            }.flatMap { saveTimeKeepingApi(it).toObservable() }
            .toList()
            .map { Constants.CUD.STATE_SUCCESS }
    }

}