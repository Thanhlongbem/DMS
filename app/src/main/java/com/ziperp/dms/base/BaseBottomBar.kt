package com.ziperp.dms.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.ziperp.dms.R
import com.ziperp.dms.extensions.getColor
import kotlinx.android.synthetic.main.bottom_bar_layout.view.*
import kotlinx.android.synthetic.main.item_bottom_bar.view.*

class BaseBottomBar : LinearLayout, BaseWidget{

    var onBottomItemClickListener: ((Int) -> Unit)? = null
    private var view: View? = null
    var items = arrayListOf<BottomBarItem>()
        set(value) {
            field = value
            updateUI()
        }

    private var itemLayouts = arrayListOf<View>()


    constructor(context: Context?) : super(context) {
        init(context, null, -1)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, -1)
    }

    override fun init(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
        view = LayoutInflater.from(context).inflate(R.layout.bottom_bar_layout, this, true)
        updateUI()
    }

    @SuppressLint("ResourceAsColor")
    fun updateUI() {
        itemLayouts.clear()
        root_content.removeAllViews()
        if (items.isNotEmpty()) {
            (root_content.layoutParams as LayoutParams).weight = items.size * 1.0f
            for (item in items) {
                val layout = LayoutInflater.from(context)
                    .inflate(R.layout.item_bottom_bar, root_content, false)
                val layoutParam = LayoutParams(0, LayoutParams.MATCH_PARENT)
                layoutParam.weight = 1.0f
                layout.apply {
                    tv_name.text = item.title
                    if(item.drawable != null){
                        img_icon.setImageResource(item.drawable!!)
                    } else {
                        img_icon.visibility = View.INVISIBLE
                    }

                    if(item.color != null){
                        img_back.backgroundTintList = ColorStateList.valueOf(item.color!!.getColor())
                    } else{
                        img_back.visibility = View.INVISIBLE
                    }
                }
                itemLayouts.add(layout)
                root_content.addView(layout, layoutParam)
            }
        }
        for (i in 0 until items.size) {
            itemLayouts[i].setOnClickListener {
                for (j in 0 until itemLayouts.size) {
                    itemLayouts[j].isSelected = j == i
                }
                onBottomItemClickListener?.invoke(i)
            }
        }
        invalidate()
    }

}

data class BottomBarItem(
    val title: String,
    @DrawableRes var drawable: Int?= null,
    @ColorRes var color: Int?= null
)