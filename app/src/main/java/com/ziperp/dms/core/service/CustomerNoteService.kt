package com.ziperp.dms.core.service

import com.ziperp.dms.common.model.CUDResponse
import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.main.customer.customernote.model.CustomerNoteResponse
import com.ziperp.dms.main.customer.customernote.model.NoteInfoResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomerNoteService {
    @POST(Constants.API.PATH_QUERY_DATA)
    fun getCustomerNote(
        @Body request: BodyRequest
    ): Single<CustomerNoteResponse>

    @POST(Constants.API.PATH_QUERY_DATA)
    fun getNoteInfo(
        @Body request: BodyRequest
    ): Single<NoteInfoResponse>

    @POST(Constants.API.PATH_CRUD_FORM)
    fun cudNote(
        @Body request: BodyRequest
    ): Single<CUDResponse>
}