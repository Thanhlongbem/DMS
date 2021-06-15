package com.ziperp.dms.core.repository

import com.ziperp.dms.common.model.CUDResponse
import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.StockCountService
import com.ziperp.dms.main.visitcustomer.model.StockCountListResponse
import io.reactivex.Single

class StockCountRepository(private val itemCountService: StockCountService) {

    fun cudItemCount(request: BodyRequest): Single<CUDResponse> {
        return itemCountService.cudItemCount(request)
    }

    fun getStockCountList(request: BodyRequest): Single<StockCountListResponse> {
        return itemCountService.getStockCountList(request)
    }
}