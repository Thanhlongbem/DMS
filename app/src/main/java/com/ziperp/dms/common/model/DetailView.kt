package com.ziperp.dms.common.model

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.utils.AppUtil
import kotlinx.android.synthetic.main.layout_detail_view.view.*

class DetailView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var title: String? = ""
    var content: String? = ""

    var isPhone = false
    var isMail = false

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_detail_view, this, true)

        val typedArray: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DetailView)
        title = AppUtil.getMultipleAttr(context, typedArray, R.styleable.DetailView_detail_title)
        content = AppUtil.getMultipleAttr(context, typedArray, R.styleable.DetailView_detail_content)
        isPhone = typedArray.getBoolean(R.styleable.DetailView_isPhone, false)

        tv_title.text = title
        tv_content.text = content


        if (isPhone) {
            if(context is BaseActivity) { context.registerForContextMenu(this)}
            if (context is BaseFragment) { context.registerForContextMenu(this)}
        }
    }

}