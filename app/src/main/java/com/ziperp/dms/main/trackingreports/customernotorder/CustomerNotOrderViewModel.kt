package com.ziperp.dms.main.trackingreports.customernotorder

import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.common.paging.PagingParam
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.CstNotOrderRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.trackingreports.customernotorder.model.CustomerNotOrderResponse
import com.ziperp.dms.main.trackingreports.customernotorder.model.NewCustomerResponse
import com.ziperp.dms.main.trackingreports.customernotorder.model.NewNotOrderRequest

class CustomerNotOrderViewModel(val repository: CstNotOrderRepository): BaseViewModel() {
    val newCustomerListData = MutableLiveData<ResponseData<NewCustomerResponse>>()
    val customerNotOrderData = MutableLiveData<ResponseData<CustomerNotOrderResponse>>()
    var latestItemControls: List<ItemControlForm>? = null

    var newCustomerPagingParam = PagingParam()
    var notOrderPagingParam = PagingParam()
    var newCustomerRequest = NewNotOrderRequest(modeGetData = "1")
    var customerNotOrderRequest = NewNotOrderRequest(modeGetData = "2")

    fun getNewCustomerList(isLoadMore: Boolean = false) {

        newCustomerListData.postValue(ResponseData.loading(null))
        newCustomerRequest.pageNumber = newCustomerPagingParam.nextPage(isLoadMore)
        newCustomerRequest.rowspPage = 50
        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJSONData = gson.toJson(newCustomerRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.CUSTOMER_NOT_ORDER, pJSONData, pPageMode = "QM")

        repository
            .getNewCustomerList(bodyRequest)
            .applyOn()
            .subscribe({
                newCustomerListData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.let {value ->
                        val totalPage = value/ 50 + 1
                        newCustomerPagingParam.updateTotalPage(totalPage)
                    }
                }
                newCustomerPagingParam.loadedPage()
            }, {
                newCustomerListData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getCustomerNotOrder(isLoadMore: Boolean = false) {

        customerNotOrderData.postValue(ResponseData.loading(null))
        customerNotOrderRequest.pageNumber = notOrderPagingParam.nextPage(isLoadMore)
        customerNotOrderRequest.rowspPage = 50

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJSONData = gson.toJson(customerNotOrderRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.CUSTOMER_NOT_ORDER, pJSONData, pPageMode = "QM")

        repository
            .getCustomerNotOrder(bodyRequest)
            .applyOn()
            .subscribe({
                customerNotOrderData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.let {value ->
                        val totalPage = value/ 50 + 1
                        notOrderPagingParam.updateTotalPage(totalPage)
                    }
                }
                notOrderPagingParam.loadedPage()
            }, {
                customerNotOrderData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun applyFilter(itemControls: List<ItemControlForm>) {
        latestItemControls = itemControls

        newCustomerRequest.timeTracking = itemControls[0].getFilterValue()
        newCustomerRequest.deptCd = itemControls[1].getFilterValue()
        newCustomerRequest.masterLocCd = itemControls[2].getFilterValue()

        customerNotOrderRequest.timeTracking = itemControls[0].getFilterValue()
        customerNotOrderRequest.deptCd = itemControls[1].getFilterValue()
        customerNotOrderRequest.masterLocCd = itemControls[2].getFilterValue()

        getNewCustomerList()
        getCustomerNotOrder()
    }
}