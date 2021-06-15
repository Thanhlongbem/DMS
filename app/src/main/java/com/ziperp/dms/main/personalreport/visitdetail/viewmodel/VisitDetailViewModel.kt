package com.ziperp.dms.main.personalreport.visitdetail.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.common.paging.PagingParam
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.VisitDetailRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.personalreport.visitdetail.model.NotVisitedResponse
import com.ziperp.dms.main.personalreport.visitdetail.model.VisitDetailRequest
import com.ziperp.dms.main.personalreport.visitdetail.model.VisitedDetailResponse

class VisitDetailViewModel(val repository: VisitDetailRepository): BaseViewModel() {
    val visitedData = MutableLiveData<ResponseData<VisitedDetailResponse>>()
    val notVisitedData = MutableLiveData<ResponseData<NotVisitedResponse>>()
    var latestItemControls: List<ItemControlForm>? = null

    var visitedPagingParam = PagingParam()
    var notVisitedPagingParam = PagingParam()
    var visitedRequest = VisitDetailRequest(staffId = DmsUserManager.userInfo.staffId)
    var notVisitedRequest = VisitDetailRequest(staffId = DmsUserManager.userInfo.staffId)

    fun getVisitedDetail(isLoadMore: Boolean = false) {

        visitedData.postValue(ResponseData.loading(null))
        visitedRequest.pageNumber = visitedPagingParam.nextPage(isLoadMore)
        visitedRequest.rowspPage = 50
        val pJSONData = Gson().toJson(visitedRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.VISIT_DETAIL, pJSONData, pPageMode = "QD")

        repository
            .getVisitedDetail(bodyRequest)
            .applyOn()
            .subscribe({
                visitedData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.let {value ->
                        val totalPage = (value.toInt())/ 50 + 1
                        visitedPagingParam.updateTotalPage(totalPage)
                    }
                }
                visitedPagingParam.loadedPage()
            }, {
                visitedData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getNotVisited(isLoadMore: Boolean = false) {

        notVisitedData.postValue(ResponseData.loading(null))
        notVisitedRequest.pageNumber = notVisitedPagingParam.nextPage(isLoadMore)
        notVisitedRequest.rowspPage = 50
        val pJSONData = Gson().toJson(notVisitedRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.VISIT_DETAIL, pJSONData, pPageMode = "QN")

        repository
            .getNotVisited(bodyRequest)
            .applyOn()
            .subscribe({
                notVisitedData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.let {value ->
                        val totalPage = (value.toInt())/ 50 + 1
                        notVisitedPagingParam.updateTotalPage(totalPage)
                    }
                }
                notVisitedPagingParam.loadedPage()
            }, {
                notVisitedData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun applyFilter(itemControls: List<ItemControlForm>) {
        latestItemControls = itemControls

        visitedRequest.timeTracking = itemControls[0].getFilterValue()
        notVisitedRequest.timeTracking = itemControls[0].getFilterValue()

        getVisitedDetail()
        getNotVisited()
    }
}