package com.ziperp.dms.core.service

import com.ziperp.dms.core.form.model.ComboDataResponse
import com.ziperp.dms.core.form.model.ComboMetaResponse
import com.ziperp.dms.core.rest.Constants
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ComboPopupService {

    @GET(Constants.API.PATH_GET_COMBO_META)
    fun getComboMeta(
        @QueryMap queryMap: Map<String, String>
    ): Single<ComboMetaResponse>

    @GET(Constants.API.PATH_GET_COMBO_DATA)
    fun getComboData(
        @QueryMap queryMap: Map<String, String>
    ): Single<ComboDataResponse>


}