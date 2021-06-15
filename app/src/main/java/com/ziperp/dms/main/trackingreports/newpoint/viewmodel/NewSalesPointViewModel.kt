package com.ziperp.dms.main.trackingreports.newpoint.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.NewSalesPointRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.trackingreports.newpoint.model.NewSalesPointRequest
import com.ziperp.dms.main.trackingreports.newpoint.model.NewSalesPointResponse

class NewSalesPointViewModel(private val repository: NewSalesPointRepository) : BaseViewModel() {
    val newSalesPointData = MutableLiveData<ResponseData<NewSalesPointResponse>>()
    var latestItemControls: List<ItemControlForm>? = null

    var requestBody = NewSalesPointRequest()

    fun getSalesPoint() {
        newSalesPointData.postValue(ResponseData.loading(null))

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJSONData = gson.toJson(requestBody)
        val bodyRequest = BodyRequestFactory.get(RequestType.NEW_SALES_POINT, pJSONData, pPageMode = "QM")

        repository
            .getSalesPoint(bodyRequest)
            .applyOn()
            .subscribe({
                newSalesPointData.postValue(ResponseData.success(it))
            }, {
                newSalesPointData.postValue(ResponseData.error("Something Went Wrong. Error message " + it.message, null))
            }).disposedBy(compositeDisposable)
    }

    fun applyFilter(itemControls: List<ItemControlForm>) {
        latestItemControls = itemControls

        requestBody.staffId = itemControls[0].getFilterValue()
        requestBody.deptCd = itemControls[1].getFilterValue()
        requestBody.masterLocCd = itemControls[2].getFilterValue()

        getSalesPoint()
    }
}