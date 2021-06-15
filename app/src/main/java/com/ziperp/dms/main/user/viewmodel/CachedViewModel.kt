package com.ziperp.dms.main.user.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.App
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.camera.CustomerImage
import com.ziperp.dms.core.repository.CustomerDetailRepository
import com.ziperp.dms.core.rest.*
import com.ziperp.dms.core.service.CustomerDetailService
import com.ziperp.dms.core.service.TrackingService
import com.ziperp.dms.core.tracking.LocationRepository
import com.ziperp.dms.dao.CachedRequest
import com.ziperp.dms.dao.FormControl
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.extensions.execute
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetail
import com.ziperp.dms.main.customer.customerroute.model.CustomerRoute
import com.ziperp.dms.main.timekeeping.model.TimeKeeping
import com.ziperp.dms.main.user.view.CachedDataActivity
import com.ziperp.dms.main.visitcustomer.model.VisitCustomer
import com.ziperp.dms.utils.LogUtils
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class CachedViewModel(): BaseViewModel(){
    val cachedLiveData = MutableLiveData<ResponseData<List<CachedRequest>>>()
    val positionLiveData = MutableLiveData<ResponseData<Int>>()
    val customerDetailService by lazy {RestService.createService(CustomerDetailService::class.java) }

    val cachedListLiveData = MutableLiveData<ResponseData<List<Any>>>()
    val synchronizedSuccessLiveData = MutableLiveData<Status>()

    val locationRepository = LocationRepository(
        RestService.createService(TrackingService::class.java),
        App.shared().appDatabase.trackingRequestDao()
    )

    val customerRepository = CustomerDetailRepository(
        RestService.createService(CustomerDetailService::class.java),
        App.shared().appDatabase.customerDetailDao()
    )

    fun getDataCachedRequests(type: Int){
        when(type){
            CachedDataActivity.TIMEKEEPING_TYPE -> getCachedTimeKeeping()
            CachedDataActivity.TRACKING_TYPE -> getCachedTrackingRequest()
            CachedDataActivity.CONTROL_TYPE -> getCachedControlRequest()
            CachedDataActivity.CUSTOMER_TYPE -> getCachedCustomerRequest()
            CachedDataActivity.CUSTOMER_IMAGE_TYPE -> getCachedCustomerImage()
            CachedDataActivity.CUSTOMER_ROUTE_TYPE -> getCachedCustomerRoutes()
            CachedDataActivity.VISIT_CUSTOMER_TYPE -> getCachedVisitCustomers()
            CachedDataActivity.VISIT_IMAGE_TYPE -> getCachedVisitImages()
        }
    }

    fun getCachedVisitCustomers(){
        cachedListLiveData.postValue(ResponseData.loading())
        val cachedDao = App.shared().appDatabase.visitCustomerDao()
        cachedDao.getAllVisitCustomer()
            .applyOn()
            .subscribe({
                LogUtils.d("Data size ${it.size}")
                cachedListLiveData.postValue(ResponseData.success(it))
            }, {
                cachedListLiveData.postValue(ResponseData.error(
                    "Something Went Wrong. Error message " + it.message,
                    null
                ))
            })
    }

    fun getCachedVisitImages(){
        cachedListLiveData.postValue(ResponseData.loading())
        val cachedDao = App.shared().appDatabase.customerImageDao()
        cachedDao.getCustomerImages(true)
            .applyOn()
            .subscribe({
                LogUtils.d("Data size ${it.size}")
                cachedListLiveData.postValue(ResponseData.success(it))
            }, {
                cachedListLiveData.postValue(ResponseData.error(
                    "Something Went Wrong. Error message " + it.message,
                    null
                ))
            })
    }

    fun getCachedCustomerRoutes(){
        cachedListLiveData.postValue(ResponseData.loading())
        val cachedDao = App.shared().appDatabase.customerRouteDao()
        cachedDao.getAllRoutes()
            .applyOn()
            .subscribe({
                LogUtils.d("Data size ${it.size}")
                cachedListLiveData.postValue(ResponseData.success(it))
            }, {
                cachedListLiveData.postValue(ResponseData.error(
                    "Something Went Wrong. Error message " + it.message,
                    null
                ))
            })
    }

    fun getCachedCustomerImage(){
        cachedListLiveData.postValue(ResponseData.loading())
        val cachedDao = App.shared().appDatabase.customerImageDao()
        cachedDao.getCustomerImages(false)
            .applyOn()
            .subscribe({
                LogUtils.d("Data size ${it.size}")
                cachedListLiveData.postValue(ResponseData.success(it))
            }, {
                cachedListLiveData.postValue(ResponseData.error(
                    "Something Went Wrong. Error message " + it.message,
                    null
                ))
            })
    }

    fun getCachedCustomerRequest(){
        cachedListLiveData.postValue(ResponseData.loading())
        val cachedDao = App.shared().appDatabase.customerDetailDao()
        cachedDao.getAllCustomerDetail()
            .applyOn()
            .subscribe({
                LogUtils.d("Data size ${it.size}")
                cachedListLiveData.postValue(ResponseData.success(it))
            }, {
                cachedListLiveData.postValue(ResponseData.error(
                    "Something Went Wrong. Error message " + it.message,
                    null
                ))
            })
    }

    fun getCachedControlRequest(){
        cachedListLiveData.postValue(ResponseData.loading())
        val cachedDao = App.shared().appDatabase.formControlDao()
        cachedDao.getFormControls()
            .applyOn()
            .subscribe({
                LogUtils.d("Data size ${it.size}")
                cachedListLiveData.postValue(ResponseData.success(it))
            }, {
                cachedListLiveData.postValue(ResponseData.error(
                    "Something Went Wrong. Error message " + it.message,
                    null
                ))
            })
    }

    fun getCachedTrackingRequest(){
        cachedListLiveData.postValue(ResponseData.loading())
        val cachedDao = App.shared().appDatabase.trackingRequestDao()
        cachedDao.getAllTrackingRequest()
            .applyOn()
            .subscribe({
                LogUtils.d("Data size ${it.size}")
                cachedListLiveData.postValue(ResponseData.success(it.sortedBy { it.TimeLogPos }))
            }, {
                cachedListLiveData.postValue(ResponseData.error(
                    "Something Went Wrong. Error message " + it.message,
                    null
                ))
            })
    }


    fun getCachedTimeKeeping(){
        cachedListLiveData.postValue(ResponseData.loading())
        val cachedDao = App.shared().appDatabase.timeKeepingDao()
        cachedDao.getAllTimeKeeping()
            .applyOn()
            .subscribe({
                cachedListLiveData.postValue(ResponseData.success(
                    it//.filter { it.isSynchonizedCheckIn == false || it.isSynchonizedCheckOut == false}
                ))
            }, {
                cachedListLiveData.postValue(ResponseData.error(
                    "Something Went Wrong. Error message " + it.message,
                    null
                ))
            })
    }


    fun removeItem(item: Any){
        when(item){
            is TimeKeeping ->{
                val cachedDao = App.shared().appDatabase.timeKeepingDao()
                Single.fromCallable { cachedDao.delete(item) }
                    .execute()
            }
            is TrackingRequest ->{
                val cachedDao = App.shared().appDatabase.trackingRequestDao()
                Single.fromCallable { cachedDao.delete(item) }
                    .execute()
            }
            is CustomerDetail ->{
                val cachedDao = App.shared().appDatabase.customerDetailDao()
                Single.fromCallable { cachedDao.delete(item) }
                    .execute()
            }
            is FormControl ->{
                val cachedDao = App.shared().appDatabase.formControlDao()
                Single.fromCallable { cachedDao.delete(item) }
                    .execute()
            }
            is CustomerImage ->{
                val cachedDao = App.shared().appDatabase.customerImageDao()
                Single.fromCallable { cachedDao.delete(item) }
                    .execute()
            }
            is CustomerRoute ->{
                val cachedDao = App.shared().appDatabase.customerRouteDao()
                Single.fromCallable { cachedDao.delete(item) }
                    .execute()
            }
            is VisitCustomer ->{
                val cachedDao = App.shared().appDatabase.visitCustomerDao()
                Single.fromCallable { cachedDao.delete(item) }
                    .execute()
            }
        }
    }

    fun deleteAll(type: Int) {
        when(type){
            CachedDataActivity.TIMEKEEPING_TYPE ->{
                Single.fromCallable {App.shared().appDatabase.timeKeepingDao().deleteAll()}.execute()
            }
            CachedDataActivity.TRACKING_TYPE ->{
                Single.fromCallable {App.shared().appDatabase.trackingRequestDao().deleteAll()}.execute()
            }
            CachedDataActivity.CONTROL_TYPE ->{
                Single.fromCallable {App.shared().appDatabase.formControlDao().deleteAll()}.execute()
            }
            CachedDataActivity.CUSTOMER_TYPE ->{
                Single.fromCallable {App.shared().appDatabase.customerDetailDao().deleteAll()}.execute()
            }
            CachedDataActivity.CUSTOMER_IMAGE_TYPE ->{
                Single.fromCallable {App.shared().appDatabase.customerImageDao().deleteAll(false)}.execute()
            }
            CachedDataActivity.CUSTOMER_ROUTE_TYPE ->{
                Single.fromCallable {App.shared().appDatabase.customerRouteDao().deleteAll()}.execute()
            }
            CachedDataActivity.VISIT_CUSTOMER_TYPE ->{
                Single.fromCallable {App.shared().appDatabase.visitCustomerDao().deleteAll()}.execute()
            }
            CachedDataActivity.VISIT_IMAGE_TYPE ->{
                Single.fromCallable {App.shared().appDatabase.customerImageDao().deleteAll(true)}.execute()
            }
        }
    }

    fun syncTrackingData(list: ArrayList<TrackingRequest>) {
        synchronizedSuccessLiveData.postValue(Status.LOADING)
        locationRepository.syncBatchTrackingLocation(list)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                LogUtils.d("Sync status code ${it}")
            },{
                LogUtils.d("Sync error ${it.message}")
                synchronizedSuccessLiveData.postValue(Status.ERROR)
            },{
                LogUtils.d("Completed")
                synchronizedSuccessLiveData.postValue(Status.SUCCESS)
            })
    }


    fun syncCustomerDetail(item: CustomerDetail){
        synchronizedSuccessLiveData.postValue(Status.LOADING)
        customerRepository.syncCustomer(item)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                synchronizedSuccessLiveData.postValue(Status.SUCCESS)
                LogUtils.d("Sync status code ${it}")
            },{
                synchronizedSuccessLiveData.postValue(Status.ERROR)
                LogUtils.d("Sync error ${it.message}")
            })
    }


    //Unused
    fun getCachedRequests(){
        cachedLiveData.postValue(ResponseData.loading())
        val cachedDao = App.shared().appDatabase.cachedRequestDao()
        cachedDao.getCachedRequests()
            .applyOn()
            .subscribe({
                cachedLiveData.postValue(ResponseData.success(it))
            }, {
                cachedLiveData.postValue(ResponseData.error(
                    "Something Went Wrong. Error message " + it.message,
                    null
                ))
            })
    }

    fun cudCustomer(cacheRequest: CachedRequest, position: Int){
        positionLiveData.postValue(ResponseData.loading())
        val customerRequest = Gson().fromJson(cacheRequest.body, BodyRequest::class.java)
        customerDetailService.cudCustomer(customerRequest)
            .applyOn()
            .subscribe({
                if(it.isSuccess()){
                    positionLiveData.postValue(ResponseData.success(position))
                    removeRequestInDB(cacheRequest.timestamp)
                }else{
                    positionLiveData.postValue(ResponseData.error(it.message(), position))
                }

            }, {
                positionLiveData.postValue(ResponseData.error(it.message?:"Something went wrong!", position))
            }).disposedBy(compositeDisposable)
    }

    fun removeRequestInDB(id: Long){
        val cachedDao = App.shared().appDatabase.cachedRequestDao()

        Observable.fromCallable { cachedDao.deleteById(id) }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ data ->
                LogUtils.d("Deleted $id...")
            }, { error ->
                LogUtils.d("Deleted DBError  ${error.message}")
            })

    }

    fun removeRequestInDB(item: CachedRequest){
        removeRequestInDB(item.timestamp)
    }


}