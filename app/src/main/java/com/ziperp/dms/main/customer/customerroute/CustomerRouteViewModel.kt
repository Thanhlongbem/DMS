package com.ziperp.dms.main.customer.customerroute

import androidx.lifecycle.MutableLiveData
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.core.repository.CustomerRouteRepository
import com.ziperp.dms.core.rest.CudData
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.customer.customerroute.model.CustomerRoute
import com.ziperp.dms.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CustomerRouteViewModel(private val customerRouteRepository: CustomerRouteRepository): BaseViewModel() {
    val addRouteStatus = MutableLiveData<ResponseData<CudData>>()
    val deleteRouteStatus = MutableLiveData<ResponseData<CudData>>()
    val routeListData2 = MutableLiveData<ResponseData<List<CustomerRoute>>>()

    fun getListRoute2(customerCode: String) {
        routeListData2.postValue(ResponseData.loading())

        customerRouteRepository
            .getCustomerRoutes(customerCode)
            .applyOn()
            .subscribe({
                routeListData2.postValue(ResponseData.success(it))
            }, {
                routeListData2.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }


    fun addRoutes2(customerRoutes: List<CustomerRoute>) {
        addRouteStatus.postValue(ResponseData.loading())
        customerRouteRepository.addRoutes(customerRoutes)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                LogUtils.d("Add success route ")
                addRouteStatus.postValue(ResponseData.success(it))
            },{
                addRouteStatus.postValue(ResponseData.error(
                    "Something Went Wrong. Error message " + it.message,
                    null
                ))
            }).disposedBy(compositeDisposable)
    }

    fun deleteRoute(item: CustomerRoute, position: Int) {
        deleteRouteStatus.postValue(ResponseData.loading())
        customerRouteRepository.deleteRoute(item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                it.data = position
                deleteRouteStatus.postValue(ResponseData.success(it))
            },{
                deleteRouteStatus.postValue(ResponseData.error(
                    "Something Went Wrong. Error message " + it.message,
                    null
                ))
            }).disposedBy(compositeDisposable)
    }

}