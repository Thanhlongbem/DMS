package com.ziperp.dms.main.personalreport.visitresult

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.common.paging.PagingParam
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.VisitResultRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy

class VisitResultViewModel(private val repository: VisitResultRepository) : BaseViewModel() {
    val visitResultListData = MutableLiveData<ResponseData<VisitResultResponse>>()
    var latestItemControls: List<ItemControlForm>? = null

    var pagingParam = PagingParam()
    var requestBody = VisitResultRequest()

    fun getVisitResult(isLoadMore: Boolean = false) {
        visitResultListData.postValue(ResponseData.loading(null))
        requestBody.pageNumber = pagingParam.nextPage(isLoadMore)
        requestBody.rowspPage = 50

        val pJSONData = Gson().toJson(requestBody)
        val bodyRequest = BodyRequestFactory.get(RequestType.VISIT_RESULT, pJSONData, pPageMode = "QM")

        repository
            .getVisitResultSummation(bodyRequest)
            .applyOn()
            .subscribe({
                visitResultListData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.toInt().let {value ->
                        val totalPage = value/ 50 + 1
                        pagingParam.updateTotalPage(totalPage)
                    }
                }
                pagingParam.loadedPage()
            }, {
                visitResultListData.postValue(
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

        getVisitResult()
    }
}