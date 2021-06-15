package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.trackingreports.newpoint.model.NewSalesPointResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface NewSalesPointService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getSalesPoint(
        @Body request: BodyRequest
    ): Single<NewSalesPointResponse>
}