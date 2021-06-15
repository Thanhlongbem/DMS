package com.ziperp.dms.core.service

import com.ziperp.dms.common.model.CUDResponse
import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.saleorder.model.*
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface SaleOrderService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getSaleOrder(
        @Body request: BodyRequest
    ): Single<SalesOrderResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getSaleOrderInfo(
        @Body request: BodyRequest
    ): Single<SaleOrderDetailResponse>

    @POST(Constants.API.PATH_CRUD_FORM)
    fun cudSalesOrder(
        @Body request: BodyRequest
    ): Single<CUDSalesOrderResponse>

    @POST(Constants.API.PATH_CRUD_FORM)
    fun confirmSalesOrder(
        @Body request: BodyRequest
    ): Single<ConfirmSaleOrderResponse>

    @POST(Constants.API.PATH_CRUD_FORM)
    fun applyPromotion(
        @Body request: BodyRequest
    ): Single<ApplyPromotionResponse>

    @POST(Constants.API.PATH_CRUD_FORM)
    fun cudSalesOrderLines(
        @Body request: BodyRequest
    ): Single<CUDResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getUnitPriceOfItem(
        @Body request: BodyRequest
    ): Single<UnitPriceResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getAvailableStockOfItem(
        @Body request: BodyRequest
    ): Single<AvailableStockQtyResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getExchangeRate(
        @Body request: BodyRequest
    ): Single<ExchangeRateResponse>
}


