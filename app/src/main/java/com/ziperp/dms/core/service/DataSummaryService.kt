package com.ziperp.dms.core.service

import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.personalreport.datasummary.model.DataSummaryResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface DataSummaryService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getDataSummary(
        @Body request: BodyRequest
    ): Single<DataSummaryResponse>
}