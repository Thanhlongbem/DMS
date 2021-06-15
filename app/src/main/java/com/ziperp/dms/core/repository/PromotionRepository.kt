package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.PromotionService
import com.ziperp.dms.main.salespricepromotion.model.PromotionInfoResponse
import com.ziperp.dms.main.salespricepromotion.model.PromotionResponse
import io.reactivex.Single

class PromotionRepository(private val service: PromotionService) {

    fun getPromotions(request: BodyRequest): Single<PromotionResponse> {
        return service.getPromotions(request)
    }

    fun getPromotionInfo(request: BodyRequest): Single<PromotionInfoResponse> {
        return service.getPromotionInfo(request)
    }

}