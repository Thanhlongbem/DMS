package com.ziperp.dms.main.trackingreports.picture.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.common.paging.PagingParam
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.TrackingPictureRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.trackingreports.picture.model.TrackingPictureDetailRequest
import com.ziperp.dms.main.trackingreports.picture.model.TrackingPictureDetailResponse
import com.ziperp.dms.main.trackingreports.picture.model.TrackingPictureListRequest
import com.ziperp.dms.main.trackingreports.picture.model.TrackingPictureListResponse

class TrackingPictureViewModel(private val repository: TrackingPictureRepository) : BaseViewModel() {
    val visitCustomerListData = MutableLiveData<ResponseData<TrackingPictureListResponse>>()
    val VSPictureDetailData = MutableLiveData<ResponseData<TrackingPictureDetailResponse>>()
    var latestItemControls: List<ItemControlForm>? = null

    var pagingParam = PagingParam()
    var pagingParamDetail = PagingParam()
    var requestBody = TrackingPictureListRequest()
    var detailRequestBody = TrackingPictureDetailRequest()

    fun getTrackingPictureList(isLoadMore: Boolean = false) {
        visitCustomerListData.postValue(ResponseData.loading(null))
        requestBody.pageNumber = pagingParam.nextPage(isLoadMore)
        requestBody.rowspPage = 50

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJSONData = gson.toJson(requestBody)
        val bodyRequest = BodyRequestFactory.get(RequestType.PICTURE_TRACKING, pJSONData, pPageMode = "QM")

        repository
            .getTrackingPictureList(bodyRequest)
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
        requestBody.deptCd = itemControls[1].getFilterValue()
        requestBody.strMasterLocCd = itemControls[2].getFilterValue()

        getTrackingPictureList()
    }

    fun getTrackingPictureDetail(staffID: String, isLoadMore: Boolean = false) {
        VSPictureDetailData.postValue(ResponseData.loading(null))
        detailRequestBody.staffId = staffID
        detailRequestBody.pageNumber = pagingParamDetail.nextPage(isLoadMore)
        detailRequestBody.rowspPage = 50

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJSONData = gson.toJson(detailRequestBody)
        val bodyRequest = BodyRequestFactory.get(RequestType.PICTURE_TRACKING, pJSONData, pPageMode = "QD")

        repository
            .getTrackingPictureDetail(bodyRequest)
            .applyOn()
            .subscribe({
                VSPictureDetailData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.toInt().let {value ->
                        val totalPage = value/ 50 + 1
                        pagingParamDetail.updateTotalPage(totalPage)
                    }
                }
                pagingParamDetail.loadedPage()
            }, {
                VSPictureDetailData.postValue(
                    ResponseData.error("Something Went Wrong. Error message " + it.message, null)
                )
            }).disposedBy(compositeDisposable)
    }
}