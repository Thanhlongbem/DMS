package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.customer.list.model.CustomerListResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomerListService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getAccountList(
        @Body request: BodyRequest
    ): Single<CustomerListResponse>

}

