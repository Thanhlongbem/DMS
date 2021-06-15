package com.ziperp.dms.core.repository

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.CustomerListService
import com.ziperp.dms.main.customer.list.model.CustomerListResponse
import io.reactivex.Single

class AccountListRepository(private val customerListService: CustomerListService) {

    fun getAccountList(request: BodyRequest): Single<CustomerListResponse> {
        return customerListService.getAccountList(request)
    }

}