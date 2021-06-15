package com.ziperp.dms.main.personalreport.datasummary.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.core.repository.DataSummaryRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.personalreport.datasummary.model.DataSummaryRequest
import com.ziperp.dms.main.personalreport.datasummary.model.DataSummaryResponse

class DataSummaryViewModel(private val repository: DataSummaryRepository): BaseViewModel() {
    val summaryData = MutableLiveData<ResponseData<DataSummaryResponse>>()

    val request = DataSummaryRequest(staffId = DmsUserManager.userInfo.staffId)

    fun getDataSummary() {
        summaryData.postValue(ResponseData.loading(null))

        val pJsonData = Gson().toJson(request)
        val bodyRequest = BodyRequestFactory.get(RequestType.DATA_SUMMARY, pJsonData)

        repository
            .getDataSummary(bodyRequest)
            .applyOn()
            .subscribe({
                summaryData.postValue(ResponseData.success(it))
            }, {
                summaryData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

}