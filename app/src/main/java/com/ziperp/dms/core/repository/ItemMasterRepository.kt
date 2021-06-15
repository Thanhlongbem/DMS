package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.ItemMasterService
import com.ziperp.dms.main.itemmaster.model.*
import io.reactivex.Single

class ItemMasterRepository(private val itemMasterService: ItemMasterService) {

    fun getItemMaster(request: BodyRequest): Single<ItemMasterResponse> {
        return itemMasterService.getItemMaster(request)
    }

    fun getStockQty(request: BodyRequest): Single<StockQtyResponse> {
        return itemMasterService.getStockQty(request)
    }

    fun getStockQtyByLotSerial(request: BodyRequest): Single<StockQtyByLotSerialResponse>{
        return itemMasterService.getStockQtyByLotSerial(request)
    }

    fun getItemMasterDetail(request: BodyRequest): Single<ItemMasterDetailResponse> {
        return itemMasterService.getItemMasterDetail(request)
    }

    fun getFileOfItemMaster(request: BodyRequest): Single<ItemMasterImageResponse> {
        return itemMasterService.getFileOfItemMaster(request)
    }

}