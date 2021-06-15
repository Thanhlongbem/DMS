package com.ziperp.dms.main.trackingreports.reportsummation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.common.paging.PagingParam
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.ReportSummationRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.extensions.ifEmptyLetBe
import com.ziperp.dms.main.trackingreports.reportsummation.model.*

class ReportSummationViewModel(private val repository: ReportSummationRepository) : BaseViewModel() {
    val reportSummationData = MutableLiveData<ResponseData<ReportSummationResponse>>()
    val orderData = MutableLiveData<ResponseData<SummationOrderResponse>>()
    val debtData = MutableLiveData<ResponseData<SummationDebtResponse>>()
    val stockData = MutableLiveData<ResponseData<SummationStockResponse>>()
    val rewardData = MutableLiveData<ResponseData<SummationStockResponse>>()
    val kpiData = MutableLiveData<ResponseData<SummationStockResponse>>()

    var latestItemControls: List<ItemControlForm>? = null

    var reportSummationRequest = ReportSummationRequest()

    var orderPagingParam = PagingParam()
    var orderRequest = ReportSummationRequest()
    var debtPagingParam = PagingParam()
    var debtRequest = ReportSummationRequest()
    var stockPagingParam = PagingParam()
    var stockRequest = ReportSummationRequest()
    var rewardPagingParam = PagingParam()
    var rewardRequest = ReportSummationRequest()
    var kpiPagingParam = PagingParam()
    var kpiRequest = ReportSummationRequest()

    fun getReportSummation() {
        reportSummationData.postValue(ResponseData.loading(null))

        val pJsonData = Gson().toJson(reportSummationRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.REPORT_SUMMATION, pJsonData)

        repository
            .getReportSummation(bodyRequest)
            .applyOn()
            .subscribe({
                reportSummationData.postValue(ResponseData.success(it))
            }, {
                reportSummationData.postValue(ResponseData.error("Something Went Wrong. Error message " + it.message, null))
            }).disposedBy(compositeDisposable)
    }

    fun getOrderReport(isLoadMore: Boolean = false) {
        orderData.postValue(ResponseData.loading(null))
        orderRequest.pageNumber = orderPagingParam.nextPage(isLoadMore)
        orderRequest.rowspPage = 50

        val pJSONData = Gson().toJson(orderRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.REPORT_SUMMATION, pJSONData, pPageMode = "QO")

        repository
            .getSummationOrder(bodyRequest)
            .applyOn()
            .subscribe({
                orderData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.toInt().let {value ->
                        val totalPage = value/ 50 + 1
                        orderPagingParam.updateTotalPage(totalPage)
                    }
                }
                orderPagingParam.loadedPage()
            }, {
                orderData.postValue(ResponseData.error("Something Went Wrong. Error message " + it.message, null))
            }).disposedBy(compositeDisposable)
    }

    fun getDebtReport(isLoadMore: Boolean = false) {
        debtData.postValue(ResponseData.loading(null))
        debtRequest.pageNumber = debtPagingParam.nextPage(isLoadMore)
        debtRequest.rowspPage = 50

        val pJSONData = Gson().toJson(debtRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.REPORT_SUMMATION, pJSONData, pPageMode = "QI")

        repository
            .getSummationDebt(bodyRequest)
            .applyOn()
            .subscribe({
                debtData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.toInt().let {value ->
                        val totalPage = value/ 50 + 1
                        debtPagingParam.updateTotalPage(totalPage)
                    }
                }
                debtPagingParam.loadedPage()
            }, {
                debtData.postValue(ResponseData.error("Something Went Wrong. Error message " + it.message, null))
            }).disposedBy(compositeDisposable)
    }

    fun getStockReport(isLoadMore: Boolean = false) {
        stockData.postValue(ResponseData.loading(null))
        stockRequest.pageNumber = stockPagingParam.nextPage(isLoadMore)
        stockRequest.rowspPage = 50

        val pJSONData = Gson().toJson(stockRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.REPORT_SUMMATION, pJSONData, pPageMode = "QS")

        repository
            .getSummationStock(bodyRequest)
            .applyOn()
            .subscribe({
                stockData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.toInt().let {value ->
                        val totalPage = value/ 50 + 1
                        stockPagingParam.updateTotalPage(totalPage)
                    }
                }
                stockPagingParam.loadedPage()
            }, {
                stockData.postValue(ResponseData.error("Something Went Wrong. Error message " + it.message, null))
            }).disposedBy(compositeDisposable)
    }

    fun applyFilter(itemControls: List<ItemControlForm>) {
        latestItemControls = itemControls
        val request = ReportSummationRequest(
            cstCd = itemControls[0].getFilterValue(),
            startDate = itemControls[1].getFilterValue().ifEmptyLetBe("20210101"),
            endDate = itemControls[2].getFilterValue().ifEmptyLetBe("20211231")
        )

        reportSummationRequest = request
        orderRequest = request
        debtRequest = request
        stockRequest = request

        getReportSummation()
        getOrderReport()
        getDebtReport()
        getStockReport()
    }
}