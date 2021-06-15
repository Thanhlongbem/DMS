package com.ziperp.dms.main.salespricepromotion.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.PromotionRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.salespricepromotion.model.*

class PromotionViewModel(private val repository: PromotionRepository) : BaseViewModel() {
    val promotionListData = MutableLiveData<ResponseData<PromotionResponse>>()
    val promotionInfoData = MutableLiveData<ResponseData<PromotionInfoResponse>>()

    var requestBody = PromotionRequest()
    var latestItemControls: List<ItemControlForm>? = null

    fun getPromotionList() {
        promotionListData.postValue(ResponseData.loading(null))

        val pJsonData = Gson().toJson(requestBody)
        val bodyRequest = BodyRequestFactory.get(RequestType.PROMOTION, pJsonData, pPageMode = "QM")
        repository
            .getPromotions(bodyRequest)
            .applyOn()
            .subscribe({
                promotionListData.postValue(ResponseData.success(it))
            }, {
                promotionListData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getPromotionInfo(promoCode: String) {
        promotionInfoData.postValue(ResponseData.loading(null))

        val pJsonData = Gson().toJson(PromotionInfoRequest(promoCode))
        val bodyRequest = BodyRequestFactory.get(RequestType.PROMOTION_INFO, pJsonData)

        repository
            .getPromotionInfo(bodyRequest)
            .applyOn()
            .subscribe({
                promotionInfoData.postValue(ResponseData.success(it))
            }, {
                promotionInfoData.postValue(ResponseData.error("Something Went Wrong. Error message " + it.message, null))
            }).disposedBy(compositeDisposable)
    }

    fun applyFilter(itemControls: List<ItemControlForm>) {
        latestItemControls = itemControls
        requestBody.date = itemControls[0].getFilterValue()
        requestBody.businessUnit = itemControls[1].getFilterValue()
        requestBody.activeStatus = itemControls[2].getFilterValue()
        getPromotionList()
    }
}