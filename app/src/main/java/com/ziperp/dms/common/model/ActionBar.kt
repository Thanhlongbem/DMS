package com.ziperp.dms.common.model

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.ziperp.dms.R
import kotlinx.android.synthetic.main.action_bar.view.*

class ActionBar(context: Context?) : RelativeLayout(context) {

    var showBackButton = false
    var showMoreButton = false
    var showSettingButton = false
    var showFilterButton = false
    var showLocationButton = false
    var showReportButton = false

    var titlePage: String? = ""

    constructor(context: Context?, attrs: AttributeSet) : this(context) {
        intiView(context, attrs)
    }

    private fun intiView(context: Context?, attrs: AttributeSet) {

        LayoutInflater.from(context).inflate(R.layout.action_bar, this, true)

        val typedArray: TypedArray =
            getContext().obtainStyledAttributes(attrs, R.styleable.ActionBar)
        showBackButton = typedArray.getBoolean(R.styleable.ActionBar_showBtnBack, false)
        showMoreButton = typedArray.getBoolean(R.styleable.ActionBar_showBtnMore, false)
        showSettingButton = typedArray.getBoolean(R.styleable.ActionBar_showBtnSetting, false)
        showFilterButton = typedArray.getBoolean(R.styleable.ActionBar_showBtnFilter, false)
        showLocationButton = typedArray.getBoolean(R.styleable.ActionBar_showBtnLocation, false)
        showReportButton = typedArray.getBoolean(R.styleable.ActionBar_showBtnReport, false)
        titlePage = getMultipleAttr(context, typedArray, R.styleable.ActionBar_titlePage)
        typedArray.recycle()

        btn_back.visibility = if (showBackButton) View.VISIBLE else View.GONE
        btn_more.visibility = if (showMoreButton) View.VISIBLE else View.GONE
        btn_setting.visibility = if (showSettingButton) View.VISIBLE else View.GONE
        btn_filter.visibility = if (showFilterButton) View.VISIBLE else View.GONE
        btn_location.visibility = if (showLocationButton) View.VISIBLE else View.GONE
        btn_report.visibility = if (showReportButton) View.VISIBLE else View.GONE
        tv_title.text = titlePage
    }

    private fun getMultipleAttr(context: Context?, typed: TypedArray, index: Int): String? {
        val value = TypedValue()
        typed.getValue(index, value)
        return if (value.type == TypedValue.TYPE_REFERENCE) {
            context?.getString(typed.getResourceId(index, 0))
        } else {
            typed.getString(index)
        }
    }
}