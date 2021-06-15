package com.ziperp.dms.main.personalreport.salesresult.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.common.paging.PagingParam
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.YourSalesResultRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.personalreport.salesresult.model.CustomerResultResponse
import com.ziperp.dms.main.personalreport.salesresult.model.ItemResultResponse
import com.ziperp.dms.main.personalreport.salesresult.model.YourSalesResultRequest

class YourSalesResultViewModel(val repository: YourSalesResultRepository): BaseViewModel() {
    val customerResultData = MutableLiveData<ResponseData<CustomerResultResponse>>()
    val itemResultData = MutableLiveData<ResponseData<ItemResultResponse>>()
    var latestItemControls: List<ItemControlForm>? = null

    var customerPagingParam = PagingParam()
    var itemPagingParam = PagingParam()

    var customerRequest = YourSalesResultRequest()
    var itemRequest = YourSalesResultRequest()

    fun getCustomerResult(isLoadMore: Boolean = false) {

        customerResultData.postValue(ResponseData.loading(null))
        customerRequest.pageNumber = customerPagingParam.nextPage(isLoadMore)
        customerRequest.rowspPage = 50
        val pJSONData = Gson().toJson(customerRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.YOUR_SALES_RESULT, pJSONData, pPageMode = "QC")

        repository
            .getCustomerResult(bodyRequest)
            .applyOn()
            .subscribe({
                customerResultData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.let {value ->
                        val totalPage = value/ 50 + 1
                        customerPagingParam.updateTotalPage(totalPage)
                    }
                }
                customerPagingParam.loadedPage()
            }, {
                customerResultData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getItemResult(isLoadMore: Boolean = false) {

        itemResultData.postValue(ResponseData.loading(null))
        itemRequest.pageNumber = itemPagingParam.nextPage(isLoadMore)
        itemRequest.rowspPage = 50
        val pJSONData = Gson().toJson(itemRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.YOUR_SALES_RESULT, pJSONData, pPageMode = "QD")

        repository
            .getItemResult(bodyRequest)
            .applyOn()
            .subscribe({
                itemResultData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.let {value ->
                        val totalPage = value/ 50 + 1
                        itemPagingParam.updateTotalPage(totalPage)
                    }
                }
                itemPagingParam.loadedPage()
            }, {
                itemResultData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun applyFilter(itemControls: List<ItemControlForm>) {
        latestItemControls = itemControls

        val filter = YourSalesResultRequest (
            analysisBasic = itemControls[0].getFilterValue(),
            itemCate = itemControls[1].getFilterValue(),
            itemModel = itemControls[2].getFilterValue(),
            itemBrand = itemControls[3].getFilterValue(),
            itemCd = itemControls[4].getFilterValue()
                )
        customerRequest = filter
        itemRequest = filter

        getCustomerResult()
        getItemResult()
    }
}