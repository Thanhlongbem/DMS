package com.ziperp.dms.main.trackingreports.salesonroute.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.SalesOnRouteRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.trackingreports.salesonroute.model.SalesOnRouteRequest
import com.ziperp.dms.main.trackingreports.salesonroute.model.SalesOnRouteResponse

class SalesOnRouteViewModel(private val repository: SalesOnRouteRepository) : BaseViewModel() {
    val salesOnRouteData = MutableLiveData<ResponseData<SalesOnRouteResponse>>()
    var latestItemControls: List<ItemControlForm>? = null

    var requestBody = SalesOnRouteRequest()

    fun getSalesOnRoute() {
        salesOnRouteData.postValue(ResponseData.loading(null))

        val pJSONData = Gson().toJson(requestBody)
        val bodyRequest = BodyRequestFactory.get(RequestType.SALES_ON_ROUTE, pJSONData, pPageMode = "QM")

        repository
            .getSalesOnRoute(bodyRequest)
            .applyOn()
            .subscribe({
                salesOnRouteData.postValue(ResponseData.success(it))
            }, {
                salesOnRouteData.postValue(ResponseData.error("Something Went Wrong. Error message " + it.message, null))
            }).disposedBy(compositeDisposable)
    }

    fun applyFilter(itemControls: List<ItemControlForm>) {
        latestItemControls = itemControls

//        requestBody.month = itemControls[0].getFilterValue()

        getSalesOnRoute()
    }
}