package com.ziperp.dms.core.service

import com.ziperp.dms.common.table.model.TableAllViewModelResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TableAllService {
    @GET("/api/FormConfig/GetInquiryFormMetaItem")
    fun getAllTable(
        @Query("pPageMode") pPageMode: String,
        @Query("pFormId") pFormId: String,
        @Query("pUserId") pUserId: String,
        @Query("pLangId") pLangId: Int,
        @Query("pConnStr") pConnStr: String
    ): Single<TableAllViewModelResponse>
}