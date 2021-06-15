package com.ziperp.dms.main.trackingreports.salesresult

import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.common.paging.PagingParam
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.SalesResultRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.trackingreports.salesresult.model.SalesDetailResponse
import com.ziperp.dms.main.trackingreports.salesresult.model.SalesResultRequest
import com.ziperp.dms.main.trackingreports.salesresult.model.SalesSummationResponse

class SalesResultViewModel(val repository: SalesResultRepository): BaseViewModel() {
    val summationData = MutableLiveData<ResponseData<SalesSummationResponse>>()
    val detailData = MutableLiveData<ResponseData<SalesDetailResponse>>()
    var latestItemControls: List<ItemControlForm>? = null

    var summationPagingParam = PagingParam()
    var detailPagingParam = PagingParam()
    var summationRequest = SalesResultRequest()
    var detailRequest = SalesResultRequest()

    fun getSummationResult(isLoadMore: Boolean = false) {
        summationData.postValue(ResponseData.loading(null))
        summationRequest.pageNumber = summationPagingParam.nextPage(isLoadMore)
        summationRequest.rowspPage = 50

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJSONData = gson.toJson(summationRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.STAFF_SALES_RESULT, pJSONData)

        repository
            .getSummationResult(bodyRequest)
            .applyOn()
            .subscribe({
                summationData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.let {value ->
                        val totalPage = value/ 50 + 1
                        summationPagingParam.updateTotalPage(totalPage)
                    }
                }
                summationPagingParam.loadedPage()
            }, {
                summationData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getDetailResult(isLoadMore: Boolean = false) {
        detailData.postValue(ResponseData.loading(null))
        detailRequest.pageNumber = detailPagingParam.nextPage(isLoadMore)
        detailRequest.rowspPage = 50

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJSONData = gson.toJson(detailRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.STAFF_SALES_RESULT, pJSONData, pPageMode = "QD")

        repository
            .getDetailResult(bodyRequest)
            .applyOn()
            .subscribe({
                detailData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.let {value ->
                        val totalPage = value/ 50 + 1
                        detailPagingParam.updateTotalPage(totalPage)
                    }
                }
                detailPagingParam.loadedPage()
            }, {
                detailData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun applyFilter(itemControls: List<ItemControlForm>) {
        latestItemControls = itemControls

        val salesResultRequest = SalesResultRequest(
            pageNumber = 1,
            rowspPage = 50,
            analysisBasic = itemControls[0].getFilterValue(),
            masterLocCd = itemControls[1].getFilterValue(),
            deptCd = itemControls[2].getFilterValue(),
            itemCate = itemControls[3].getFilterValue(),
            itemModel = itemControls[4].getFilterValue(),
            itemBrand = itemControls[5].getFilterValue(),
            itemCd = itemControls[6].getFilterValue()
        )
        summationRequest = salesResultRequest
        detailRequest = salesResultRequest

        getSummationResult()
        getDetailResult()
    }
}