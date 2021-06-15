package com.ziperp.dms.core.form.combopopup.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.core.form.model.ComboDataResponse
import com.ziperp.dms.core.form.model.ComboMetaResponse
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.form.model.KeyValue
import com.ziperp.dms.core.repository.ComboPopupRepository
import com.ziperp.dms.core.rest.FormParamFactory
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.dao.FormControlDao
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.utils.LogUtils

class ComboPopupViewModel(private val comboPopupRepository: ComboPopupRepository) :
    BaseViewModel() {
    val comboLiveData = MutableLiveData<ResponseData<List<KeyValue>>>()

    fun getComboData(itemControl: ItemControlForm) {
        comboLiveData.postValue(ResponseData.loading(null))
        comboPopupRepository
            .getComboPopupData(itemControl)
            .applyOn()
            .subscribe({ result ->
                comboLiveData.postValue(ResponseData.success(result))
            }, {
                LogUtils.d("error ${it.message}")
                comboLiveData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message ${it.message}",
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

}