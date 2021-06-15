package com.ziperp.dms.main.customer.customerimage

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.camera.CustomerImagesResponse
import com.ziperp.dms.core.repository.CustomerImageRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.customer.customerimage.model.CustomerImageRequest

class CustomerImageViewModel(private val customerImageRepository: CustomerImageRepository): BaseViewModel() {
    val imageListData = MutableLiveData<ResponseData<CustomerImagesResponse>>()

    fun getListImage(customerCode: String) {
        imageListData.postValue(ResponseData.loading(null))

        val pJsonData = Gson().toJson(CustomerImageRequest(customerCode))
        val bodyRequest = BodyRequestFactory.get(RequestType.CUSTOMER_IMAGE, pJsonData)

        customerImageRepository
            .getCustomerImages(bodyRequest)
            .applyOn()
            .subscribe({
                imageListData.postValue(ResponseData.success(it))
            }, {
                imageListData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }
}