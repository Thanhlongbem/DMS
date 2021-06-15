package com.ziperp.dms.main.itemmaster.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.common.paging.PagingParam
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.ItemMasterRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.itemmaster.model.*

class ItemMasterViewModel(private val itemMasterRepository: ItemMasterRepository) : BaseViewModel() {
    val itemMasterData = MutableLiveData<ResponseData<ItemMasterResponse>>()
    val itemMasterDetail = MutableLiveData<ResponseData<ItemMasterDetailResponse>>()
    val stockQtyData = MutableLiveData<ResponseData<StockQtyResponse>>()
    val stockQtyByLotSerialData = MutableLiveData<ResponseData<StockQtyByLotSerialResponse>>()
    val itemMasterImageData = MutableLiveData<ResponseData<ItemMasterImageResponse>>()
    var pagingParam = PagingParam()
    var requestBody = ItemMasterRequest()
    var stockQtyRequest = StockQtyRequest()
    var latestItemControls: List<ItemControlForm>? = null

    fun getItemMasterList(isLoadMore: Boolean = false) {
        itemMasterData.postValue(ResponseData.loading(null))

        requestBody.pageNumber = pagingParam.nextPage(isLoadMore)
        requestBody.rowspPage = 50

        val pJsonData = Gson().toJson(requestBody)
        val bodyRequest = BodyRequestFactory.get(RequestType.ITEM_MASTER, pJsonData)
        itemMasterRepository
            .getItemMaster(bodyRequest)
            .applyOn()
            .subscribe({
                itemMasterData.postValue(ResponseData.success(it))
                if(it.record.isNotEmpty()){
                    it.record[0].totalRecords.toInt().let {value ->
                        val totalPage = value/ 50 + 1
                        pagingParam.updateTotalPage(totalPage)
                    }
                }
                pagingParam.loadedPage()
            }, {
                itemMasterData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getItemMasterDetail(itemCode: String) {
        itemMasterDetail.postValue(ResponseData.loading(null))

        val pJsonData = Gson().toJson(ItemMasterDetailRequest(itemCode))
        val bodyRequest = BodyRequestFactory.get(RequestType.ITEM_MASTER_DETAIL, pJsonData)

        itemMasterRepository
            .getItemMasterDetail(bodyRequest)
            .applyOn()
            .subscribe({
                itemMasterDetail.postValue(ResponseData.success(it))
            }, {
                itemMasterDetail.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getFileOfItemMaster(itemCd: String) {
        itemMasterImageData.postValue(ResponseData.loading(null))

        val pJsonData = Gson().toJson(ItemMasterDetailRequest(itemCd))
        val bodyRequest = BodyRequestFactory.get(RequestType.ITEM_MASTER_FILE, pJsonData)

        itemMasterRepository
            .getFileOfItemMaster(bodyRequest)
            .applyOn()
            .subscribe({
                itemMasterImageData.postValue(ResponseData.success(it))
            }, {
                itemMasterImageData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }



    fun getStockQtyData(itemCd: String){
        stockQtyData.postValue(ResponseData.loading(null))
        stockQtyRequest.itemCd = itemCd
        val pJsonData = Gson().toJson(stockQtyRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.STOCK_QTY, pJsonData)
        itemMasterRepository
            .getStockQty(bodyRequest)
            .applyOn()
            .subscribe({
                stockQtyData.postValue(ResponseData.success(it))
            }, {
                stockQtyData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getStockQtyByLotSerial(itemCd: String){
        stockQtyByLotSerialData.postValue(ResponseData.loading(null))
        stockQtyRequest.itemCd = itemCd
        val pJsonData = Gson().toJson(stockQtyRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.STOCK_QTY_BY_LOT_SERIAL, pJsonData)
        itemMasterRepository
            .getStockQtyByLotSerial(bodyRequest)
            .applyOn()
            .subscribe({
                stockQtyByLotSerialData.postValue(ResponseData.success(it))
            }, {
                stockQtyByLotSerialData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun applyFilter(itemControls: List<ItemControlForm>) {
        latestItemControls = itemControls
        requestBody.itemCate = itemControls[0].getFilterValue()
        requestBody.itemGrp = itemControls[1].getFilterValue()
        requestBody.itemBrand = itemControls[2].getFilterValue()
        requestBody.itemModel = itemControls[3].getFilterValue()
        requestBody.vendor = itemControls[4].getFilterValue()
        requestBody.maker = itemControls[5].getFilterValue()
        getItemMasterList(false)
    }
}