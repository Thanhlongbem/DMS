package com.ziperp.dms.core.repository

import com.ziperp.dms.common.model.CUDResponse
import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.CustomerNoteService
import com.ziperp.dms.main.customer.customernote.model.CustomerNoteResponse
import com.ziperp.dms.main.customer.customernote.model.NoteInfoResponse
import io.reactivex.Single

class CustomerNoteRepository(private val customerNoteService: CustomerNoteService) {
    fun getCustomerNote(request: BodyRequest): Single<CustomerNoteResponse> {
        return customerNoteService.getCustomerNote(request)
    }

    fun getNoteInfo(request: BodyRequest): Single<NoteInfoResponse> {
        return customerNoteService.getNoteInfo(request)
    }

    fun cudNote(request: BodyRequest): Single<CUDResponse> {
        return customerNoteService.cudNote(request)
    }
}