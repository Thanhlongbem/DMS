package com.ziperp.dms.core.repository

import com.ziperp.dms.common.model.CUDResponse
import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.SaleOrderService
import com.ziperp.dms.main.saleorder.model.*
import io.reactivex.Single

class SaleOrderRepository(private val saleOrderService: SaleOrderService) {

    fun getSaleOrder(request: BodyRequest): Single<SalesOrderResponse> {
        return saleOrderService.getSaleOrder(request)
    }

    fun getSalesOrderInfo(request: BodyRequest): Single<SaleOrderDetailResponse> {
        return saleOrderService.getSaleOrderInfo(request)
    }

    fun cudSalesOrder(request: BodyRequest): Single<CUDSalesOrderResponse> {
        return saleOrderService.cudSalesOrder(request)
    }

    fun confirmSalesOrder(request: BodyRequest): Single<ConfirmSaleOrderResponse> {
        return saleOrderService.confirmSalesOrder(request)
    }

    fun applyPromotion(request: BodyRequest): Single<ApplyPromotionResponse> {
        return saleOrderService.applyPromotion(request)
    }

    fun cudSalesOrderLine(request: BodyRequest): Single<CUDResponse> {
        return saleOrderService.cudSalesOrderLines(request)
    }

    fun getUnitPriceOfItem(request: BodyRequest): Single<UnitPriceResponse> {
        return saleOrderService.getUnitPriceOfItem(request)
    }

    fun getAvailableStockOfItem(request: BodyRequest): Single<AvailableStockQtyResponse> {
        return saleOrderService.getAvailableStockOfItem(request)
    }

    fun getExchangeRate(request: BodyRequest): Single<ExchangeRateResponse> {
        return saleOrderService.getExchangeRate(request)
    }

}