package com.ziperp.dms.main.trackingreports.staff.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.common.paging.PagingParam
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.TrackingStaffRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.trackingreports.staff.model.TrackingStaffDetailRequest
import com.ziperp.dms.main.trackingreports.staff.model.TrackingStaffDetailResponse
import com.ziperp.dms.main.trackingreports.staff.model.TrackingStaffListResponse
import com.ziperp.dms.main.trackingreports.staff.model.TrackingStaffRequest

class TrackingStaffViewModel(private val trackingStaffRepository: TrackingStaffRepository) : BaseViewModel() {
    val staffListData = MutableLiveData<ResponseData<TrackingStaffListResponse>>()
    val staffListMapData = MutableLiveData<ResponseData<TrackingStaffListResponse>>()
    val staffDetailData = MutableLiveData<ResponseData<TrackingStaffDetailResponse>>()
    var latestItemControls: List<ItemControlForm>? = null

    var pagingParam = PagingParam()
    var pagingParamDetail = PagingParam()
    var requestBody = TrackingStaffRequest()
    var detailRequestBody = TrackingStaffDetailRequest()

    val updateStaffState = MutableLiveData<Int>()

    fun getTrackingStaffList(isLoadMore: Boolean = false) {
        staffListData.postValue(ResponseData.loading(null))
        requestBody.pageNumber = pagingParam.nextPage(isLoadMore)
        requestBody.rowspPage = 50

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJSONData = gson.toJson(requestBody)
        val bodyRequest = BodyRequestFactory.get(RequestType.STAFF_TRACKING, pJSONData, pPageMode = "QM")

        trackingStaffRepository
            .getTrackingStaffList(bodyRequest)
            .applyOn()
            .subscribe({
                staffListData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.toInt().let {value ->
                        val totalPage = value/ 50 + 1
                        pagingParam.updateTotalPage(totalPage)
                    }
                }
                pagingParam.loadedPage()
            }, {
                staffListData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getMapStaffList() {
        staffListMapData.postValue(ResponseData.loading(null))
        requestBody.pageNumber = 0
        requestBody.rowspPage = 0

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJSONData = gson.toJson(requestBody)
        val bodyRequest = BodyRequestFactory.get(RequestType.STAFF_TRACKING, pJSONData, pPageMode = "QM")

        trackingStaffRepository
            .getTrackingStaffList(bodyRequest)
            .applyOn()
            .subscribe({
                staffListMapData.postValue(ResponseData.success(it))
            }, {
                staffListMapData.postValue(ResponseData.error("Something Went Wrong. Error message " + it.message, null))
            }).disposedBy(compositeDisposable)
    }

    fun applyFilter(itemControls: List<ItemControlForm>) {
        latestItemControls = itemControls

        requestBody.timeTracking = itemControls[0].getFilterValue()
        requestBody.staffId = itemControls[1].getFilterValue()
        requestBody.deptCd = itemControls[2].getFilterValue()
        requestBody.strMasterLocCd = itemControls[3].getFilterValue()
        requestBody.positionSts = itemControls[4].getFilterValue()

        getTrackingStaffList()
        getMapStaffList()
    }

    fun getTrackingStaffDetail(staffID: String, isLoadMore: Boolean = false) {
        staffDetailData.postValue(ResponseData.loading(null))
        detailRequestBody.staffId = staffID
        detailRequestBody.pageNumber = pagingParamDetail.nextPage(isLoadMore)
        detailRequestBody.rowspPage = 50

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJSONData = gson.toJson(detailRequestBody)
        val bodyRequest = BodyRequestFactory.get(RequestType.STAFF_TRACKING, pJSONData, pPageMode = "QM")

        trackingStaffRepository
            .getTrackingStaffDetail(bodyRequest)
            .applyOn()
            .subscribe({
                staffDetailData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.toInt().let {value ->
                        val totalPage = value/ 50 + 1
                        pagingParamDetail.updateTotalPage(totalPage)
                    }
                }
                pagingParamDetail.loadedPage()
            }, {
                staffDetailData.postValue(
                    ResponseData.error("Something Went Wrong. Error message " + it.message, null)
                )
            }).disposedBy(compositeDisposable)
    }


}