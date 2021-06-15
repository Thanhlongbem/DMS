package com.ziperp.dms.main.salespricepromotion.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.SalesPriceRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.salespricepromotion.model.SalesPriceInfoRequest
import com.ziperp.dms.main.salespricepromotion.model.SalesPriceInfoResponse
import com.ziperp.dms.main.salespricepromotion.model.SalesPriceRequest
import com.ziperp.dms.main.salespricepromotion.model.SalesPriceResponse

class SalesPriceViewModel(private val repository: SalesPriceRepository) : BaseViewModel() {
    val salesPriceData = MutableLiveData<ResponseData<SalesPriceResponse>>()
    val salesPriceInfoData = MutableLiveData<ResponseData<SalesPriceInfoResponse>>()

    var requestBody = SalesPriceRequest()
    var latestItemControls: List<ItemControlForm>? = null

    fun getSalesPriceList() {
        salesPriceData.postValue(ResponseData.loading(null))

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJsonData = gson.toJson(requestBody)
        val bodyRequest = BodyRequestFactory.get(RequestType.SALE_PRICE, pJsonData, pPageMode = "QM")
        repository
            .getSalesPrice(bodyRequest)
            .applyOn()
            .subscribe({
                salesPriceData.postValue(ResponseData.success(it))
            }, {
                salesPriceData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getSalesPriceInfo(priceCode: String) {
        salesPriceInfoData.postValue(ResponseData.loading(null))

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJsonData = gson.toJson(SalesPriceInfoRequest(priceCode))
        val bodyRequest = BodyRequestFactory.get(RequestType.SALE_PRICE_INFO, pJsonData)

        repository
            .getSalesPriceInfo(bodyRequest)
            .applyOn()
            .subscribe({
                salesPriceInfoData.postValue(ResponseData.success(it))
            }, {
                salesPriceInfoData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun applyFilter(itemControls: List<ItemControlForm>) {
        latestItemControls = itemControls
        requestBody.date = itemControls[0].getFilterValue()
        requestBody.businessUnit = itemControls[1].getFilterValue()
        requestBody.activeStatus = itemControls[2].getFilterValue()
        getSalesPriceList()
    }
}