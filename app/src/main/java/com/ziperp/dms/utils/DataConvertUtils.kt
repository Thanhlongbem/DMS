package com.ziperp.dms.utils

import com.ziperp.dms.R
import com.ziperp.dms.extensions.getString

class DataConvertUtils {
    companion object {

        fun convertTitle(title: String): String {
            val result = title.split("#")[0].trim()
            return if (result.isEmpty()) "N/A" else result
        }

        fun divideTime(time: String): String {
            return if (time.length > 5) "${time.substring(0,2)}:${time.substring(2,4)}:${time.substring(4,6)}" else time
        }
    }

}