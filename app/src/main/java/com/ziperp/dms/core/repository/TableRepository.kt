package com.ziperp.dms.core.repository

import com.ziperp.dms.core.service.TableAllService
import com.ziperp.dms.common.table.model.TableAllViewModelResponse
import io.reactivex.Single

class TableRepository(private val tableAllService: TableAllService) {

    fun getAllTable(
        pPageMode: String,
        pFormId: String,
        pUserId: String,
        pLangId: Int,
        pConnStr: String
    ): Single<TableAllViewModelResponse> {
        return tableAllService.getAllTable(pPageMode, pFormId, pUserId, pLangId, pConnStr)
    }


}