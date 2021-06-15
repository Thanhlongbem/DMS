package com.ziperp.dms.main.customer.list.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.common.paging.PagingParam
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.AccountListRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.customer.list.model.CustomerListRequest
import com.ziperp.dms.main.customer.list.model.CustomerListResponse

class CustomerViewModel(private val accountListRepository: AccountListRepository) : BaseViewModel() {
    val accountListData = MutableLiveData<ResponseData<CustomerListResponse>>()
    var latestItemControls: List<ItemControlForm>? = null

    var pagingParam = PagingParam()
    var requestBody = CustomerListRequest()

    fun getAccountList(isLoadMore: Boolean = false) {
        accountListData.postValue(ResponseData.loading(null))
        requestBody.pageNumber = pagingParam.nextPage(isLoadMore)
        requestBody.rowspPage = 50

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJSONData = gson.toJson(requestBody)
        val bodyRequest = BodyRequestFactory.get(RequestType.CUSTOMER, pJSONData)

        accountListRepository
            .getAccountList(bodyRequest)
            .applyOn()
            .subscribe({
                accountListData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.toInt().let {value ->
                        val totalPage = value/ 50 + 1
                        pagingParam.updateTotalPage(totalPage)
                    }
                }
                pagingParam.loadedPage()
            }, {
                accountListData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun applyFilter(itemControls: List<ItemControlForm>) {
        latestItemControls = itemControls

        requestBody.ownerId = itemControls[0].getFilterValue()
        requestBody.cstType = itemControls[1].getFilterValue()
        requestBody.cstGrp1 = itemControls[2].getFilterValue()
        requestBody.cstGrp2 = itemControls[3].getFilterValue()
        requestBody.cstGrp3 = itemControls[4].getFilterValue()
        requestBody.cstGrp4 = itemControls[5].getFilterValue()

        getAccountList(false)
    }
}