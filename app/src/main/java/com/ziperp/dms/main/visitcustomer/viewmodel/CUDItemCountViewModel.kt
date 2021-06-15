package com.ziperp.dms.main.visitcustomer.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.common.model.CUDResponse
import com.ziperp.dms.core.repository.StockCountRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.visitcustomer.model.StockCountInfo
import com.ziperp.dms.main.visitcustomer.model.StockCountListResponse
import com.ziperp.dms.main.visitcustomer.model.StockCountRequest

class CUDItemCountViewModel(private val itemCountRepository: StockCountRepository) : BaseViewModel() {

    val cudRequestStatus = MutableLiveData<ResponseData<CUDResponse>>()
    val stockCountList = MutableLiveData<ResponseData<StockCountListResponse>>()

    fun cudItemCount(type: String, request: StockCountRequest){
        cudRequestStatus.value = ResponseData.loading(null)
        val pJsonData = Gson().toJson(request)
        val bodyRequest = BodyRequestFactory.get(RequestType.CUD_ITEM_COUNT, pJsonData, pPageMode = type)
        itemCountRepository.cudItemCount(bodyRequest).applyOn().subscribe({
            cudRequestStatus.value = ResponseData.success(it)
        }, {
            cudRequestStatus.value = ResponseData.error(it.message!!, null)
        }).disposedBy(compositeDisposable)
    }

    fun getStockCountList(customerNo: String){
        stockCountList.postValue(ResponseData.loading(null))
        val pJsonData = Gson().toJson(StockCountInfo(CstVisitNo = customerNo))
        val bodyRequest = BodyRequestFactory.get(RequestType.ITEM_COUNT_INFO, pJsonData)
        itemCountRepository.getStockCountList(bodyRequest).applyOn().subscribe({
            stockCountList.postValue(ResponseData.success(it))
        }, {
            stockCountList.postValue(ResponseData.error("Something Went Wrong. Error message " + it.message, null))
        }).disposedBy(compositeDisposable)
    }

}