package com.ziperp.dms.main.saleorder.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.common.model.CUDResponse
import com.ziperp.dms.common.paging.PagingParam
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.SaleOrderRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.saleorder.model.*
import java.text.SimpleDateFormat
import java.util.*


class SaleOrderViewModel(private val saleOrderRepository: SaleOrderRepository) : BaseViewModel() {
    val saleOrderLiveData = MutableLiveData<ResponseData<SalesOrderResponse>>()
    val saleOrderInfoLiveData = MutableLiveData<ResponseData<SaleOrderDetailResponse>>()
    var latestItemControls: List<ItemControlForm>? = null
    val cudSaleOrderLiveData = MutableLiveData<ResponseData<CUDSalesOrderResponse>>()
    val confirmSaleOrderLiveData = MutableLiveData<ResponseData<ConfirmSaleOrderResponse>>()
    val applyPromotionLiveData = MutableLiveData<ResponseData<ApplyPromotionResponse>>()
    val cudLinesStatus = MutableLiveData<ResponseData<CUDResponse>>()

    //utils api
    val itemPriceLiveData = MutableLiveData<ResponseData<UnitPriceResponse>>()
    val availableStockLiveData = MutableLiveData<ResponseData<AvailableStockQtyResponse>>()
    val exchangeRateLiveData = MutableLiveData<ResponseData<ExchangeRateResponse>>()

    var pagingParam = PagingParam()
    val saleOrderRequest = SaleOrderRequest()

    fun getSaleOrder(isLoadMore: Boolean = false) {
        saleOrderLiveData.postValue(ResponseData.loading(null))

        saleOrderRequest.pageNumber = pagingParam.nextPage(isLoadMore)
        saleOrderRequest.rowspPage = 50

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJsonData = gson.toJson(saleOrderRequest)
        val bodyRequest = BodyRequestFactory.get(RequestType.SALE_ORDER, pJsonData)

        saleOrderRepository
            .getSaleOrder(bodyRequest)
            .applyOn()
            .subscribe({
                saleOrderLiveData.postValue(ResponseData.success(it))
                if (it.record.isNotEmpty()) {
                    it.record[0].totalRecords.toInt().let { value ->
                        val totalPage = value / 50 + 1
                        pagingParam.updateTotalPage(totalPage)
                    }
                    pagingParam.loadedPage()
                }
            }, {
                saleOrderLiveData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getSaleOrderInfo(orderNo: String) {
        saleOrderInfoLiveData.postValue(ResponseData.loading(null))

        val request = SaleOrderDetailRequest()
        request.orderNo = orderNo

        val pJsonData = Gson().toJson(request)
        val bodyRequest = BodyRequestFactory.get(RequestType.SALE_ORDER_INFO, pJsonData)

        saleOrderRepository
            .getSalesOrderInfo(bodyRequest)
            .applyOn()
            .subscribe({
                saleOrderInfoLiveData.postValue(ResponseData.success(it))
            }, {
                saleOrderInfoLiveData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getUnitPriceOfItem(
        item: SaleOrderItem,
        saleOrderRequest: CUDSalesOrderRequest,
        uoMCd: String,
        orderQty: String
    ) {
        itemPriceLiveData.postValue(ResponseData.loading())

        val infoLine = "P1&${item.itemCd}_/P2&${" "}_/P3&${uoMCd}_/P4&${orderQty}_/P5&${" "}_/P6&${saleOrderRequest.poNo}"
        val request = UnitPriceRequest(
            priceMode = "S",
            cstCd = saleOrderRequest.cstCd,
            currencyCd = saleOrderRequest.currencyCd,
            date = saleOrderRequest.orderDate,
            masterLocCd = saleOrderRequest.masterLocCd,
            deptCd = saleOrderRequest.deptCd,
            staffId = saleOrderRequest.staffId,
            strData = infoLine
        )

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJsonData = gson.toJson(request)
        val bodyRequest = BodyRequestFactory.get(RequestType.UNIT_PRICE, pJsonData)
        saleOrderRepository.getUnitPriceOfItem(bodyRequest)
            .applyOn()
            .subscribe({
                itemPriceLiveData.postValue(ResponseData.success(it))
            }, {
                itemPriceLiveData.postValue(ResponseData.error("Error message " + it.message))
            }).disposedBy(compositeDisposable)
    }


    fun getAvailableStockOfItem(
        item: SaleOrderItem,
        saleOrderRequest: CUDSalesOrderRequest) {
        availableStockLiveData.postValue(ResponseData.loading())

        val ownShip = when (saleOrderRequest.whType){
            "C" -> "C"
            "D" -> "T"
            else -> ""
        }

        val currentDate =  SimpleDateFormat("yyyyMMdd").format(Date())
        val request = AvailableStockQtyRequest(
            rowCnt = 1,
            rowNo = "0",
            itemCd = item.itemCd,
            whCd = item.whCd,
            staffId = saleOrderRequest.staffId,
            ownShip = ownShip,
            currDate = currentDate,
            masterLocCd = saleOrderRequest.masterLocCd,
            sepaStr = "!~"
        )

        val gson = GsonBuilder().disableHtmlEscaping().create()
        val pJsonData = gson.toJson(request)
        val bodyRequest = BodyRequestFactory.get(RequestType.AVAILABLE_STOCK, pJsonData)
        saleOrderRepository.getAvailableStockOfItem(bodyRequest)
            .applyOn()
            .subscribe({
                availableStockLiveData.postValue(ResponseData.success(it))
            }, {
                availableStockLiveData.postValue(ResponseData.error("Error message " + it.message))
            }).disposedBy(compositeDisposable)
    }

    fun getExchangeRate(currency: String, masterLocCd: String){
        exchangeRateLiveData.postValue(ResponseData.loading())

        val currentDate =  SimpleDateFormat("yyyyMMdd").format(Date())

        val request = ExchangeRateRequest(
            currCd = currency,
            date =  currentDate,
            masterLocCd = masterLocCd
        )

        val pJsonData = Gson().toJson(request)
        val bodyRequest = BodyRequestFactory.get(RequestType.EXCHANGE_RATE, pJsonData)
        saleOrderRepository.getExchangeRate(bodyRequest)
            .applyOn()
            .subscribe({
                exchangeRateLiveData.postValue(ResponseData.success(it))
            }, {
                exchangeRateLiveData.postValue(ResponseData.error("Error message " + it.message))
            }).disposedBy(compositeDisposable)
    }


    fun cudSalesOrder(type: String, request: CUDSalesOrderRequest) {
        cudSaleOrderLiveData.postValue(ResponseData.loading())

        val pJsonData = Gson().toJson(request)
        val bodyRequest =
            BodyRequestFactory.get(RequestType.CUD_SALES_ORDER, pJsonData, pPageMode = type)
        saleOrderRepository.cudSalesOrder(bodyRequest)
            .applyOn()
            .subscribe({
                cudSaleOrderLiveData.postValue(ResponseData.success(it))
            }, {
                cudSaleOrderLiveData.postValue(ResponseData.error("Error message " + it.message))
            }).disposedBy(compositeDisposable)
    }

    fun cudSalesOrderLines(type: String, request: CUDLinesRequest) {
        cudLinesStatus.value = ResponseData.loading(null)
        val pJsonData = Gson().toJson(request)
        val bodyRequest = BodyRequestFactory.get(RequestType.CUD_SALES_ORDER_LINES, pJsonData, type)
        saleOrderRepository.cudSalesOrderLine(bodyRequest).applyOn().subscribe({
            cudLinesStatus.value = ResponseData.success(it)
        }, {
            cudLinesStatus.value = ResponseData.error(it.message?: "Something went wrong!", null)
        }).disposedBy(compositeDisposable)
    }

    fun confirmSalesOrder(request: ConfirmSaleOderRequest) {
        confirmSaleOrderLiveData.postValue(ResponseData.loading())

        val pJsonData = Gson().toJson(request)
        val bodyRequest =
            BodyRequestFactory.get(RequestType.CONFIRM_SALE_ORDER, pJsonData, pPageMode = "A")
        saleOrderRepository.confirmSalesOrder(bodyRequest)
            .applyOn()
            .subscribe({
                confirmSaleOrderLiveData.postValue(ResponseData.success(it))
            }, {
                confirmSaleOrderLiveData.postValue(ResponseData.error("Error message " + it.message))
            }).disposedBy(compositeDisposable)
    }

    fun applyPromotion(request: ApplyPromotionRequest) {
        applyPromotionLiveData.postValue(ResponseData.loading())

        val pJsonData = Gson().toJson(request)
        val bodyRequest =
            BodyRequestFactory.get(RequestType.APPLY_PROMOTION, pJsonData, pPageMode = "A")
        saleOrderRepository.applyPromotion(bodyRequest)
            .applyOn()
            .subscribe({
                applyPromotionLiveData.postValue(ResponseData.success(it))
            }, {
                applyPromotionLiveData.postValue(ResponseData.error("Error message " + it.message))
            }).disposedBy(compositeDisposable)
    }



    fun applyFilter(itemControls: List<ItemControlForm>) {
        latestItemControls = itemControls

        saleOrderRequest.orderDateFr = itemControls[0].getFilterValue()
        saleOrderRequest.orderDateTo = itemControls[1].getFilterValue()
        saleOrderRequest.salesman = itemControls[2].getFilterValue()
        saleOrderRequest.salesDept = itemControls[3].getFilterValue()
        saleOrderRequest.cstCd = itemControls[4].getFilterValue()
        saleOrderRequest.businessUnit = itemControls[5].getFilterValue()
        saleOrderRequest.orderStatus = itemControls[6].getFilterValue()

        getSaleOrder(false)
    }
}