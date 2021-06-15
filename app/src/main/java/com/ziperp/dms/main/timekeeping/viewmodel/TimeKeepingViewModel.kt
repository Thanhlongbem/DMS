package com.ziperp.dms.main.timekeeping.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.core.repository.TimeKeepingRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.extensions.toDateString
import com.ziperp.dms.main.timekeeping.model.*
import com.ziperp.dms.utils.LogUtils
import io.reactivex.Observable.fromIterable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class TimeKeepingViewModel (private val timeKeepingRepository: TimeKeepingRepository): BaseViewModel() {

    val timeKeepingLiveData = MutableLiveData<ResponseData<List<TimeKeeping>>>()
    val timeKeepingCountLiveData = MutableLiveData<Int>(0)

    fun getTimeKeepingList(day: String) {
        timeKeepingLiveData.postValue(ResponseData.loading())
        var request = TimeKeepingListRequest(staffId = DmsUserManager.userInfo.staffId)
        request.timeKeepDay = day
        val pJsonData = Gson().toJson(request)
        val bodyRequest = BodyRequestFactory.get(RequestType.TIME_KEEPING, pJsonData, pPageMode = "QL")
        timeKeepingRepository
            .getTimeKeepingList(day, bodyRequest)
            .applyOn()
            .subscribe({
                timeKeepingLiveData.postValue(ResponseData.success(it))
            }, {
                timeKeepingLiveData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun saveTimeKeeping(timeKeeping: TimeKeeping) {
        timeKeepingRepository
            .saveTimeKeeping(timeKeeping)
            .applyOn()
            .subscribe({
                getTimeKeepingList(Date().toDateString())
            }, {
                timeKeepingLiveData.postValue(ResponseData.error(
                    "Something Went Wrong. Error message " + it.message,
                    null
                ))
            }).disposedBy(compositeDisposable)
    }

    fun synchronizeData(listTimeKeeping: List<TimeKeeping>){
        timeKeepingLiveData.postValue(ResponseData.loading())
        io.reactivex.Observable.fromIterable(listTimeKeeping)
            .flatMap {
                timeKeepingRepository.saveTimeKeepingApi(it).toObservable()
            }.doOnNext{
                timeKeepingCountLiveData.postValue(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
               LogUtils.d("Data success " + it)
            },{
                LogUtils.d("Error " + it.message)
                timeKeepingLiveData.postValue(ResponseData.error(
                    "Something Went Wrong. Error message " + it.message,
                    null
                ))
            },{
                getTimeKeepingList(Date().toDateString())
            }).disposedBy(compositeDisposable)

    }



}