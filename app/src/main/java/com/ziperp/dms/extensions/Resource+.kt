package com.ziperp.dms.extensions

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import com.ziperp.dms.App


@ColorInt
fun Int.getColor(): Int {
    return ResourcesCompat.getColor(App.shared().resources, this, null)
}

fun Int.getString(): String{
    return App.shared().getString(this)
}

fun Int.getDrawable(): Drawable? {
    return ResourcesCompat.getDrawable(App.shared().resources, this, null)
}
