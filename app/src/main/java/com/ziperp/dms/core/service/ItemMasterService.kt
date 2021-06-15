package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.itemmaster.model.*
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface ItemMasterService {

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getItemMaster(
        @Body request: BodyRequest
    ): Single<ItemMasterResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getStockQty(
        @Body request: BodyRequest
    ): Single<StockQtyResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getStockQtyByLotSerial(
        @Body request: BodyRequest
    ): Single<StockQtyByLotSerialResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getItemMasterDetail(
        @Body request: BodyRequest
    ): Single<ItemMasterDetailResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getFileOfItemMaster(
        @Body request: BodyRequest
    ): Single<ItemMasterImageResponse>

}


