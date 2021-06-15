package com.ziperp.dms.main.customer.customernote.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.common.model.CUDResponse
import com.ziperp.dms.core.repository.CustomerNoteRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetailRequest
import com.ziperp.dms.main.customer.customernote.model.CUDNoteRequest
import com.ziperp.dms.main.customer.customernote.model.CustomerNoteResponse
import com.ziperp.dms.main.customer.customernote.model.NoteInfoRequest
import com.ziperp.dms.main.customer.customernote.model.NoteInfoResponse

class CustomerNoteViewModel(private val customerNoteRepository: CustomerNoteRepository): BaseViewModel() {
    val listNoteLiveData = MutableLiveData<ResponseData<CustomerNoteResponse>>()
    val noteInfoLiveData = MutableLiveData<ResponseData<NoteInfoResponse>>()
    val cudRequestStatus = MutableLiveData<ResponseData<CUDResponse>>()

    fun getCustomerNotes(customerCode: String) {
        listNoteLiveData.postValue(ResponseData.loading(null))

        val pJsonData = Gson().toJson(CustomerDetailRequest(customerCode))
        val bodyRequest = BodyRequestFactory.get(RequestType.CUSTOMER_NOTE, pJsonData)

        customerNoteRepository
            .getCustomerNote(bodyRequest)
            .applyOn()
            .subscribe({
                listNoteLiveData.postValue(ResponseData.success(it))
            }, {
                listNoteLiveData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getNoteInfo(nodeId: String) {
        noteInfoLiveData.postValue(ResponseData.loading(null))

        val pJsonData = Gson().toJson(NoteInfoRequest(nodeId))
        val bodyRequest = BodyRequestFactory.get(RequestType.NOTE_INFO, pJsonData)

        customerNoteRepository
            .getNoteInfo(bodyRequest)
            .applyOn()
            .subscribe({
                noteInfoLiveData.postValue(ResponseData.success(it))
            }, {
                noteInfoLiveData.postValue(ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun CUDNote(type: String, request: CUDNoteRequest) {
        cudRequestStatus.value = ResponseData.loading(null)
        val pJsonData = Gson().toJson(request)
        val bodyRequest = BodyRequestFactory.get(RequestType.CUD_NOTE, pJsonData, pPageMode = type)

        customerNoteRepository.cudNote(bodyRequest)
            .applyOn()
            .subscribe({
            cudRequestStatus.value = ResponseData.success(it)
        }, {
            cudRequestStatus.value = ResponseData.error(it.message!!, null)
        }).disposedBy(compositeDisposable)
    }

    companion object {
        const val TYPE_CREATE = "A"
        const val TYPE_UPDATE = "U"
        const val TYPE_DELETE = "D"

    }

}