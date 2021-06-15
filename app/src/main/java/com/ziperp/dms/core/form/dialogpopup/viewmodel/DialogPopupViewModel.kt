package com.ziperp.dms.core.form.dialogpopup.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.common.paging.PagingParam
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.form.model.KeyValue
import com.ziperp.dms.core.repository.DialogPopupRepository
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.utils.LogUtils

class DialogPopupViewModel(private val dialogPopupRepository: DialogPopupRepository) :
    BaseViewModel() {

    //paging
    val dialogLiveData = MutableLiveData<ResponseData<List<KeyValue>>>()
    var pagingParam = PagingParam()
    var lookUpValue = ""
    lateinit var itemControl: ItemControlForm


    fun getDialogData(isLoadMore: Boolean = false) {
        if(itemControl == null) return

        val nextPage = pagingParam.nextPage(isLoadMore)

        dialogLiveData.postValue(ResponseData.loading())
        dialogPopupRepository
            .getDialogPopupData(itemControl, nextPage, lookUpValue, pagingParam)
            .applyOn()
            .subscribe({ result ->
                dialogLiveData.postValue(ResponseData.success(result))
            }, {
                LogUtils.d("error ${it.message}")
                dialogLiveData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message ${it.message}",
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

}