package com.ziperp.dms.core.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ziperp.dms.common.paging.PagingParam
import com.ziperp.dms.core.form.model.DialogDataResponse
import com.ziperp.dms.core.form.model.DialogMetaResponse
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.form.model.KeyValue
import com.ziperp.dms.core.rest.FormParamFactory
import com.ziperp.dms.core.service.DialogPopupService
import com.ziperp.dms.dao.FormControl
import com.ziperp.dms.dao.FormControlDao
import com.ziperp.dms.extensions.execute
import com.ziperp.dms.utils.LogUtils
import com.ziperp.dms.utils.NetWorkConnection
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

class DialogPopupRepository(private val dialogPopupService: DialogPopupService, private val controlDao: FormControlDao) {

    fun getDialogPopupData(
        itemControl: ItemControlForm, page: Int, query: String = "", pagingParam: PagingParam
    ): Single<List<KeyValue>> {
        val useCached = itemControl.isUsedCached()

        return if(useCached){
            getDialogPopupDataFromDb(itemControl)
                .filter { it.isNotEmpty() }
                .map {
                    return@map if(query.isNotBlank()) {
                        it.filter {it.valueName.contains(query)}
                    }else it
                }
                .switchIfEmpty(getDialogPopupDataFromApi(itemControl, 0))
        }else{
            getDialogPopupDataFromApi(itemControl, page, query, pagingParam)
        }
    }

    fun getDialogPopupDataFromApi(itemControl: ItemControlForm, page: Int,
                                  query: String = "", pagingParam: PagingParam? = null): Single<List<KeyValue>> {
        val param = FormParamFactory.getDialogMetaParam(itemControl)
        return dialogPopupService
            .getDialogMeta(param)
            .flatMap {meta ->
                val param2 = FormParamFactory.getDialogDataParam(itemControl, meta, page, query)
                dialogPopupService.getDialogData(param2)
                    .doOnSuccess {
                        if(it.record.isNotEmpty()){
                            val totalPage = it.record[0].totalRecords/50 + 1
                            pagingParam?.updateTotalPage(totalPage)
                            pagingParam?.updateTotalItems(it.record[0].totalRecords)
                        }
                        pagingParam?.loadedPage()
                    }
                    .map { response ->  return@map response.table.mapNotNull { mapJsonToKV(it, meta) }}
            }
            .doOnSuccess{
                storeDialogInDb(itemControl, it)
            }
    }

    private fun getDialogPopupDataFromDb(itemControl: ItemControlForm): Single<List<KeyValue>> {
        return controlDao
            .getFormControl(itemControl.getControlFormId())
            .map {
                return@map Gson().fromJson(it.data, Array<KeyValue>::class.java).asList()
            }.onErrorResumeNext{Single.just(arrayListOf())}
    }

    private fun mapJsonToKV(json: JsonObject, dialogMeta: DialogMetaResponse): KeyValue? {
        if (json.entrySet().size > 0) {
            val data = KeyValue()
            dialogMeta.selections[0].apply {
                data.keyCode = columnCode
                data.valueCode = json.getAsJsonPrimitive(columnCode).asString
                data.keyName = columnValue
                data.valueName = json.getAsJsonPrimitive(columnValue).asString
                data.jsonData = json.toString()
                return data
            }
        }
        return null
    }

    private fun storeDialogInDb(itemControl: ItemControlForm, keyValues: List<KeyValue>) {
        if(keyValues.isEmpty()) return
        LogUtils.d("Save dialog to db")
        val data = Gson().toJson(keyValues)
        val formControl = FormControl(itemControl.getControlFormId(), data)

        Observable.fromCallable { controlDao.insert(formControl) }
            .execute("Store Dialog data")
    }

}