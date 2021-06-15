package com.ziperp.dms.main.trackingreports.dailyperformance.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.DailyPerformanceRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.trackingreports.dailyperformance.model.DailyPerformanceRequest
import com.ziperp.dms.main.trackingreports.dailyperformance.model.DailyPerformanceResponse

class DailyPerformanceViewModel(private val repository: DailyPerformanceRepository): BaseViewModel() {
    val dailyReportData = MutableLiveData<ResponseData<DailyPerformanceResponse>>()

    var latestItemControls: List<ItemControlForm>? = null
    var requestBody = DailyPerformanceRequest()

    fun getDailyReport() {
        dailyReportData.postValue(ResponseData.loading(null))

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJsonData = gson.toJson(requestBody)
        val bodyRequest = BodyRequestFactory.get(RequestType.DAILY_PERFORMANCE, pJsonData)

        repository
            .getDailyReport(bodyRequest)
            .applyOn()
            .subscribe({
                dailyReportData.postValue(ResponseData.success(it))
            }, {
                dailyReportData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun applyFilter(itemControls: List<ItemControlForm>) {
        latestItemControls = itemControls

        requestBody.staffId = itemControls[0].getFilterValue()
        requestBody.deptCd = itemControls[1].getFilterValue()
        requestBody.masterLocCd = itemControls[2].getFilterValue()

        getDailyReport()
    }
}