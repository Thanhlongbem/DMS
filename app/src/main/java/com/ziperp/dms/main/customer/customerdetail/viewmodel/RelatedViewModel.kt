package com.ziperp.dms.main.customer.customerdetail.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ziperp.dms.App
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.core.rest.*
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class RelatedViewModel() : BaseViewModel() {
    val relatedData = MutableLiveData<ResponseData<Pair<Int, Int>>>()

    fun getImagesAndRoutes(customerCode: String) {
        relatedData.postValue(ResponseData.loading())

        val routeDao = App.shared().appDatabase.customerRouteDao()
        val imageDao = App.shared().appDatabase.customerImageDao()

        Single.zip(
            routeDao.getAllRoutes(customerCode)
                .map { it.size }
                .onErrorResumeNext(Single.just(0)),
            imageDao.getCustomerImages(customerCode, false)
                .map { it.size }
                .onErrorResumeNext(Single.just(0)),
            BiFunction {
                    numberRoutes: Int, numberImages: Int ->
                return@BiFunction Pair(numberRoutes, numberImages)
            }).applyOn()
            .subscribe({
                relatedData.postValue(ResponseData.success(it))
            }, {
                relatedData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

}