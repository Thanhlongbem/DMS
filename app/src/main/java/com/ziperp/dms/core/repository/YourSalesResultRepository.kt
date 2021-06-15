package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.YourSalesResultService
import com.ziperp.dms.main.personalreport.salesresult.model.CustomerResultResponse
import com.ziperp.dms.main.personalreport.salesresult.model.ItemResultResponse
import io.reactivex.Single

class YourSalesResultRepository(private val service: YourSalesResultService) {
    fun getCustomerResult(request: BodyRequest): Single<CustomerResultResponse> {
        return service.getCustomerResult(request)
    }

    fun getItemResult(request: BodyRequest): Single<ItemResultResponse> {
        return service.getItemResult(request)
    }
}