package com.ziperp.dms.core.service

import com.ziperp.dms.core.form.model.DialogDataResponse
import com.ziperp.dms.core.form.model.DialogMetaResponse
import com.ziperp.dms.core.rest.Constants
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface DialogPopupService {

    @GET(Constants.API.PATH_GET_DIALOG_META)
    fun getDialogMeta(
        @QueryMap queryMap: Map<String, String>
    ): Single<DialogMetaResponse>

    @GET(Constants.API.PATH_GET_DIALOG_DATA)
    fun getDialogData(
        @QueryMap queryMap: Map<String, String>
    ): Single<DialogDataResponse>


}