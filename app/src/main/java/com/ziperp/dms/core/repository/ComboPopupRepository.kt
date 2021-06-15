package com.ziperp.dms.core.repository

import com.google.gson.Gson
import com.ziperp.dms.core.form.model.ComboDataResponse
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.form.model.KeyValue
import com.ziperp.dms.core.rest.FormParamFactory
import com.ziperp.dms.core.service.ComboPopupService
import com.ziperp.dms.dao.FormControl
import com.ziperp.dms.dao.FormControlDao
import com.ziperp.dms.extensions.execute
import com.ziperp.dms.utils.LogUtils
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ComboPopupRepository(
    private val comboPopupService: ComboPopupService,
    private val controlDao: FormControlDao
) {

    fun getComboPopupData(itemControl: ItemControlForm): Single<List<KeyValue>> {
        return getComboPopupDataFromDb(itemControl)
            .doOnSuccess{
                //Update new data
                LogUtils.d("Request api to update data")
                getComboPopupDataFromApi(itemControl)
                    .execute()
            }
            .filter{it.isNotEmpty()}
            .switchIfEmpty(getComboPopupDataFromApi(itemControl))

    }

    fun getComboPopupDataFromApi(itemControl: ItemControlForm): Single<List<KeyValue>> {
        val param = FormParamFactory.getComboMetaParam(itemControl)
        return comboPopupService
            .getComboMeta(param).flatMap {
                val param2 = FormParamFactory.getComboDataParam(itemControl, it)
                comboPopupService.getComboData(param2)
            }
            .map { response ->  return@map response.table.mapNotNull { mapJsonToKV(it) }}
            .doOnSuccess{ storeComboInDb(itemControl, it) }
    }

    private fun getComboPopupDataFromDb(itemControl: ItemControlForm): Single<List<KeyValue>> {
        return controlDao
            .getFormControl(itemControl.getControlFormId())
            .map {
                return@map Gson().fromJson(it.data, Array<KeyValue>::class.java).asList()
            }.onErrorResumeNext{Single.just(arrayListOf())}
    }

    private fun mapJsonToKV(item: ComboDataResponse.TableItem): KeyValue? {
        val data = KeyValue()
        data.keyCode = "CmbVal"
        data.valueCode = item.cmbVal
        data.keyName = "CmbText"
        data.valueName = item.cmbText
        return data
    }


    private fun storeComboInDb(itemControl: ItemControlForm, keyValues: List<KeyValue>) {
        val data = Gson().toJson(keyValues)
        val formControl = FormControl(itemControl.getControlFormId(), data)

        Observable.fromCallable { controlDao.insert(formControl) }
            .execute()
    }

}