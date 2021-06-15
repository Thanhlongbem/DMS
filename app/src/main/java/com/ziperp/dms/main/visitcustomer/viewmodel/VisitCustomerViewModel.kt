package com.ziperp.dms.main.visitcustomer.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.common.paging.PagingParam
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.VisitCustomerRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.CudData
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.visitcustomer.model.*
import com.ziperp.dms.utils.LogUtils

class VisitCustomerViewModel(val visitCustomerRepository: VisitCustomerRepository) :
    BaseViewModel() {

    companion object{
        val CLEAR_DATA_STATE = 1
    }
    val visitCustomerLiveData = MutableLiveData<ResponseData<VisitCustomerResponse>>()
    val visitCustomerMapLiveData = MutableLiveData<ResponseData<VisitCustomerResponse>>()

    val visitCustomerInfoLiveData = MutableLiveData<ResponseData<VisitCustomerInfoResponse>>()
    val saveVisitCustomerLiveData = MutableLiveData<ResponseData<SaveVisitCustomerInfoResponse>>()
    val saveVisitCustomerLiveData2 = MutableLiveData<ResponseData<CudData>>()
    val salesOrderLiveData = MutableLiveData<ResponseData<VisitCustomerOrderResponse>>()

    var pagingParam = PagingParam()
    var visitCustomerRequest = SubVisitCustomerRequest()
    var visitCustomerInfoRequest = VisitCustomerInfoRequest()
    var saveVisitCustomerInfoRequest = SaveVisitCustomerInfoRequest()
    var latestItemControls: List<ItemControlForm>? = null

    val updateVisitCustomerState = MutableLiveData<Int>() // 0: Idle 1: clear data

    fun getVisitCustomer(isLoadMore: Boolean = false) {
        visitCustomerLiveData.postValue(ResponseData.loading(null))
        visitCustomerRequest.pageNumber = pagingParam.nextPage(isLoadMore)
        visitCustomerRequest.rowspPage = 50

        val pJSONData = GsonBuilder().disableHtmlEscaping().create().toJson(visitCustomerRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.VISIT_CUSTOMER, pJSONData)

        visitCustomerRepository
            .getVisitCustomer(bodyRequest)
            .applyOn()
            .subscribe({
                visitCustomerLiveData.postValue(ResponseData.success(it))
                if (it.record.isNotEmpty()) {
                    it.record[0].totalRecords.toInt().let { value ->
                        val totalPage = value / 50 + 1
                        pagingParam.updateTotalPage(totalPage)
                    }
                }
                pagingParam.loadedPage()

                LogUtils.d("Loaded page ${pagingParam.currentPage}  and total ${pagingParam.totalPage}")
            }, {
                visitCustomerLiveData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getVisitCustomerMap() {
        visitCustomerMapLiveData.postValue(ResponseData.loading(null))
        visitCustomerRequest.pageNumber = 0
        visitCustomerRequest.rowspPage = 0

        val pJSONData = GsonBuilder().disableHtmlEscaping().create().toJson(visitCustomerRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.VISIT_CUSTOMER, pJSONData)

        visitCustomerRepository
            .getVisitCustomer(bodyRequest)
            .applyOn()
            .subscribe({
                visitCustomerMapLiveData.postValue(ResponseData.success(it))
            }, {
                visitCustomerMapLiveData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getVisitCustomerInfo(cstVisitNo: String) {
        visitCustomerInfoLiveData.postValue(ResponseData.loading(null))
        visitCustomerInfoRequest.cstVisitNo = cstVisitNo

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJSONData = gson.toJson(visitCustomerInfoRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.VISIT_CUSTOMER_INFO, pJSONData)

        visitCustomerRepository
            .getVisitCustomerInfo(bodyRequest)
            .applyOn()
            .subscribe({
                visitCustomerInfoLiveData.postValue(ResponseData.success(it))
            }, {
                visitCustomerInfoLiveData.postValue(ResponseData.error("Something Went Wrong. Error message " + it.message, null)
                )
            }).disposedBy(compositeDisposable)
    }

    fun saveVisitCustomer(visitCustomer: VisitCustomer){
        saveVisitCustomerLiveData2.postValue(ResponseData.loading())
        visitCustomerRepository.saveVisitCustomer(visitCustomer)
            .applyOn()
            .subscribe({
                it.data = visitCustomer
                saveVisitCustomerLiveData2.postValue(ResponseData.success(it))
            }, {
                saveVisitCustomerLiveData2.postValue(ResponseData.error("Something Went Wrong. Error message " + it.message, null)
                )
            }).disposedBy(compositeDisposable)
    }



    fun getVisitCustomerOrder(cstVisitNo: String) {
        LogUtils.d("getVisitCustomerOrder ..  ")
        salesOrderLiveData.postValue(ResponseData.loading(null))
        visitCustomerInfoRequest.cstVisitNo = cstVisitNo

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJSONData = gson.toJson(visitCustomerInfoRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.VISIT_CUSTOMER_ORDER, pJSONData)

        visitCustomerRepository
            .getVisitCustomerOrder(bodyRequest)
            .applyOn()
            .subscribe({
                salesOrderLiveData.postValue(ResponseData.success(it))
            }, {
                salesOrderLiveData.postValue(ResponseData.error("Something Went Wrong. Error message " + it.message, null))
            }).disposedBy(compositeDisposable)
    }

    fun applyFilter(itemControls: List<ItemControlForm>) {
        latestItemControls = itemControls

        visitCustomerRequest.route = itemControls[0].getFilterValue()
        visitCustomerRequest.visitStatus = itemControls[1].getFilterValue()
        //Ignore search [2]
        visitCustomerRequest.visitDay = itemControls[3].getFilterValue()
        visitCustomerRequest.cstGrp1 = itemControls[4].getFilterValue()
        visitCustomerRequest.cstGrp2 = itemControls[5].getFilterValue()
        visitCustomerRequest.cstGrp3 = itemControls[6].getFilterValue()
        visitCustomerRequest.cstGrp4 = itemControls[7].getFilterValue()

        getVisitCustomer(false)
        getVisitCustomerMap()
    }
}