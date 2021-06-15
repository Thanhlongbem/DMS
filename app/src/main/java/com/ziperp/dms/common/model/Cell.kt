package com.ziperp.dms.common.model

import com.evrencoskun.tableview.filter.IFilterableModel
import com.evrencoskun.tableview.sort.ISortableModel

open class Cell(private val mId: String, var data: Any?) :
    ISortableModel, IFilterableModel {
    private val mFilterKeyword: String = data.toString()

    /**
     * This is necessary for sorting process.
     * See ISortableModel
     */
    override fun getId(): String {
        return mId
    }

    /**
     * This is necessary for sorting process.
     * See ISortableModel
     */
    override fun getContent(): Any? {
        return data
    }

    override fun getFilterableKeyword(): String {
        return mFilterKeyword
    }

}
