package com.ziperp.dms.core.service

import com.ziperp.dms.common.model.CUDResponse
import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.customer.customerdetail.model.CUDCustomerResponse
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetailResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomerDetailService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getCustomerDetail(
        @Body request: BodyRequest
    ): Single<CustomerDetailResponse>

    @POST(Constants.API.PATH_CRUD_FORM)
    fun cudCustomer(
        @Body request: BodyRequest
    ): Single<CUDCustomerResponse>
}