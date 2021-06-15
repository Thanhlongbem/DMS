package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.ItemMasterService
import com.ziperp.dms.core.service.SalesPriceService
import com.ziperp.dms.main.itemmaster.model.*
import com.ziperp.dms.main.salespricepromotion.model.SalesPriceInfoResponse
import com.ziperp.dms.main.salespricepromotion.model.SalesPriceResponse
import io.reactivex.Single

class SalesPriceRepository(private val service: SalesPriceService) {

    fun getSalesPrice(request: BodyRequest): Single<SalesPriceResponse> {
        return service.getSalesPrice(request)
    }

    fun getSalesPriceInfo(request: BodyRequest): Single<SalesPriceInfoResponse> {
        return service.getSalesPriceInfo(request)
    }

}