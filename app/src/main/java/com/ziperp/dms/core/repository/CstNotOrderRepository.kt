package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.CstNotOrderService
import com.ziperp.dms.main.trackingreports.customernotorder.model.CustomerNotOrderResponse
import com.ziperp.dms.main.trackingreports.customernotorder.model.NewCustomerResponse
import io.reactivex.Single

class CstNotOrderRepository(private val service: CstNotOrderService) {
    fun getNewCustomerList(request: BodyRequest): Single<NewCustomerResponse> {
        return service.getNewCustomer(request)
    }

    fun getCustomerNotOrder(request: BodyRequest): Single<CustomerNotOrderResponse> {
        return service.getCustomerNotOrder(request)
    }
}