package com.ziperp.dms.core.cache

import com.ziperp.dms.App
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.ComboPopupRepository
import com.ziperp.dms.core.repository.DialogPopupRepository
import com.ziperp.dms.core.rest.RestService
import com.ziperp.dms.core.service.ComboPopupService
import com.ziperp.dms.core.service.DialogPopupService
import com.ziperp.dms.extensions.execute
import com.ziperp.dms.utils.LogUtils
import com.ziperp.dms.utils.NetWorkConnection
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_customer_modify.*

object CacheManager {

    const val REQUEST_ID_CUSTOMER = 0
    const val REQUEST_ID_TIMEKEEPING = 1
    const val REQUEST_ID_VISIT_CUSTOMER = 2
    const val REQUEST_ID_TRACKING = 3

    fun preload() {
        if(NetWorkConnection.isNetworkAvailable()){
            val comboRepository = ComboPopupRepository(
                RestService.createService(ComboPopupService::class.java),
                App.shared().appDatabase.formControlDao()
            )
            val dialogRepository = DialogPopupRepository(
                RestService.createService(DialogPopupService::class.java),
                App.shared().appDatabase.formControlDao()
            )

            preloadCacheDialog().forEach{
                dialogRepository.getDialogPopupDataFromApi(it, 0).execute("Cache Dialog")// Load all no need paging
            }

            preloadCacheCombo().forEach {
                comboRepository.getComboPopupDataFromApi(it).execute("Cache Combo")
            }
        }
    }

    fun preloadCacheCombo(): ArrayList<ItemControlForm> {
        return arrayListOf(
            ItemControlForm(
                formId = Form.FORM_ID_CUSTOMER,
                controlId = "cmbAccType",
                controlName = "Customer Type",
                lookUpCode = "0018",
                lookUpType = Form.LookUpType.COMBO,
                findType = Form.FindType.CODE
            ),
            ItemControlForm(
                formId = Form.FORM_ID_CUSTOMER,
                controlId = "cmbGender",
                controlName = "Gender",
                lookUpCode = "0074",
                lookUpType = Form.LookUpType.COMBO,
                findType = Form.FindType.CODE
            )
        )
    }

    fun preloadCacheDialog(): ArrayList<ItemControlForm> {
        return arrayListOf(
            ItemControlForm(
                formId = Form.FORM_ID_CUSTOMER,
                controlId = "txtRegionCd",
                controlName = "City",
                lookUpCode = "0054",
                lookUpType = Form.LookUpType.DIALOG,
                findType = Form.FindType.CODE
            ),
            ItemControlForm(
                formId = Form.FORM_ID_VISIT_CUSTOMER,
                controlId = "txtRoute",
                controlName = "Route",
                lookUpCode = "ROUT",
                lookUpType = Form.LookUpType.DIALOG_MUL,
                findType = Form.FindType.NAME
            ).apply {
                param.item1 = "1"
            }
        )

    }

    fun clearAllData(){
        Single.fromCallable{App.shared().appDatabase.clearAllTables()}
            .execute()

    }


}