package com.ziperp.dms.common.table.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.core.repository.SaleReportRepository
import com.ziperp.dms.core.repository.TableRepository
import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.common.table.model.SaleReportRequest
import com.ziperp.dms.common.table.model.SaleReportResponse
import com.ziperp.dms.common.table.model.TableAllViewModelResponse

class TableAllViewModel(
    private val tableRepository: TableRepository,
    private val saleReportRepository: SaleReportRepository
) : BaseViewModel() {
    val tableLiveData = MutableLiveData<ResponseData<TableAllViewModelResponse>>()
    val saleReportData = MutableLiveData<ResponseData<SaleReportResponse>>()

    fun getAllTableData() {
        tableLiveData.postValue(ResponseData.loading(null))

        tableRepository
            .getAllTable("Q",
                "qrySSalesAnalyMnItem",
                DmsUserManager.userInfo.userId,
                0,
                "")
            .applyOn()
            .subscribe({
                tableLiveData.postValue(ResponseData.success(it))
            }, {
                tableLiveData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getSaleReport() {
        saleReportData.postValue(ResponseData.loading(null))
        val request = SaleReportRequest(
            calculateBy = "1",
            database = "1",
            month = "20200601",
            pageNumber = 1,
            rowspPage = 50
        )
        val bodyRequest = BodyRequest(
            "SP_Get_qrySSalesAnalyMnItem_Mobile",
            DmsUserManager.userInfo.userId,
            "0",
            "",
            "qrySSalesAnalyMnItem",
            "Q",
            Gson().toJson(request)
        )
        saleReportRepository
            .getSaleReport(bodyRequest)
            .applyOn()
            .subscribe({
                saleReportData.postValue(ResponseData.success(it))
            }, {
                saleReportData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }
}