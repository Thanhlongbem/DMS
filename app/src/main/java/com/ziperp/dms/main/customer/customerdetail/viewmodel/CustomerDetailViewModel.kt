package com.ziperp.dms.main.customer.customerdetail.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.core.repository.CustomerDetailRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.CudData
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.extensions.execute
import com.ziperp.dms.main.customer.customerdetail.model.CUDCustomerResponse
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetail
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetailRequest
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetailResponse
import com.ziperp.dms.main.customer.list.model.CUDCustomerRequest
import com.ziperp.dms.utils.LogUtils

class CustomerDetailViewModel(val customerDetailRepository: CustomerDetailRepository) : BaseViewModel() {
    val customerDetailData = MutableLiveData<ResponseData<CustomerDetailResponse>>()
    val deleteRequestStatus2 = MutableLiveData<ResponseData<CudData>>()
    val cudRequestStatus2 = MutableLiveData<ResponseData<CudData>>()

    fun getCustomerDetail(customerCode: String) {
        customerDetailData.postValue(ResponseData.loading(null))

        val pJsonData = Gson().toJson(CustomerDetailRequest(customerCode))
        val bodyRequest = BodyRequestFactory.get(RequestType.CUSTOMER_DETAIL, pJsonData)

        customerDetailRepository
            .getCustomerDetail(bodyRequest)
            .applyOn()
            .subscribe({
                customerDetailData.postValue(ResponseData.success(it))
            }, {
                customerDetailData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun deleteCustomer(customer: CustomerDetail) {
        deleteRequestStatus2.postValue(ResponseData.loading())
        customerDetailRepository.delete(customer).applyOn().subscribe({
            deleteRequestStatus2.postValue(ResponseData.success(it))
        }, {
            LogUtils.d("Error ${it.message})")
            deleteRequestStatus2.postValue(ResponseData.error(it.message?:"Something went wrong!", null))
        }).disposedBy(compositeDisposable)
    }

    fun saveCustomer(customer: CustomerDetail){

        cudRequestStatus2.postValue(ResponseData.loading())
        customerDetailRepository.saveCustomer(customer)
            .applyOn().subscribe({
                it.data = customer
                cudRequestStatus2.postValue(ResponseData.success(it))
            }, {
                LogUtils.d("ERR: ${it.message})")
                cudRequestStatus2.postValue(
                    ResponseData.error(
                        it.message ?: "Something went wrong!", null
                    )
                )
            }).disposedBy(compositeDisposable)

    }

}