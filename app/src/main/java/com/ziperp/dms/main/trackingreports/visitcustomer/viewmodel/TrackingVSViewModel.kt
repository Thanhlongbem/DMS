package com.ziperp.dms.main.trackingreports.visitcustomer.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.common.paging.PagingParam
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.TrackingVSRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.trackingreports.visitcustomer.model.TrackingVSDetailRequest
import com.ziperp.dms.main.trackingreports.visitcustomer.model.TrackingVSDetailResponse
import com.ziperp.dms.main.trackingreports.visitcustomer.model.TrackingVSListRequest
import com.ziperp.dms.main.trackingreports.visitcustomer.model.TrackingVSListResponse

class TrackingVSViewModel(private val repository: TrackingVSRepository) : BaseViewModel() {
    val visitCustomerListData = MutableLiveData<ResponseData<TrackingVSListResponse>>()
    val visitCustomerDetailData = MutableLiveData<ResponseData<TrackingVSDetailResponse>>()
    var latestItemControls: List<ItemControlForm>? = null

    var pagingParam = PagingParam()
    var pagingParamDetail = PagingParam()
    var requestBody = TrackingVSListRequest()
    var detailRequestBody = TrackingVSDetailRequest()

    fun getTrackingVSList(isLoadMore: Boolean = false) {
        visitCustomerListData.postValue(ResponseData.loading(null))
        requestBody.pageNumber = pagingParam.nextPage(isLoadMore)
        requestBody.rowspPage = 50

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJSONData = gson.toJson(requestBody)
        val bodyRequest = BodyRequestFactory.get(RequestType.VISIT_CUSTOMER_TRACKING, pJSONData, pPageMode = "QM")

        repository
            .getTrackingVSList(bodyRequest)
            .applyOn()
            .subscribe({
                visitCustomerListData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.toInt().let {value ->
                        val totalPage = value/ 50 + 1
                        pagingParam.updateTotalPage(totalPage)
                    }
                }
                pagingParam.loadedPage()
            }, {
                visitCustomerListData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun applyFilter(itemControls: List<ItemControlForm>) {
        latestItemControls = itemControls

        requestBody.timeTracking = itemControls[0].getFilterValue()
        requestBody.routeSts = itemControls[1].getFilterValue()
        requestBody.validSts = itemControls[2].getFilterValue()
        requestBody.strMasterLocCd = itemControls[3].getFilterValue()
        requestBody.deptCd = itemControls[4].getFilterValue()
        requestBody.imageSts = itemControls[5].getFilterValue()
        requestBody.orderSts = itemControls[6].getFilterValue()

        getTrackingVSList()
    }

    fun getTrackingVSDetail(staffID: String, isLoadMore: Boolean = false) {
        visitCustomerDetailData.postValue(ResponseData.loading(null))
        detailRequestBody.staffId = staffID
        detailRequestBody.pageNumber = pagingParamDetail.nextPage(isLoadMore)
        detailRequestBody.rowspPage = 50

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJSONData = gson.toJson(detailRequestBody)
        val bodyRequest = BodyRequestFactory.get(RequestType.VISIT_CUSTOMER_TRACKING, pJSONData, pPageMode = "QD")

        repository
            .getTrackingVSDetail(bodyRequest)
            .applyOn()
            .subscribe({
                visitCustomerDetailData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.toInt().let {value ->
                        val totalPage = value/ 50 + 1
                        pagingParamDetail.updateTotalPage(totalPage)
                    }
                }
                pagingParamDetail.loadedPage()
            }, {
                visitCustomerDetailData.postValue(
                    ResponseData.error("Something Went Wrong. Error message " + it.message, null)
                )
            }).disposedBy(compositeDisposable)
    }
}