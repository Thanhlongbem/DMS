package com.ziperp.dms.utils

import android.util.Log

object LogUtils {

    private const val TAG = "DMS_Logger"

    fun d(message: String) {
        val ste = Throwable().stackTrace
        val text = "[" + ste[1].fileName + ":" + ste[1].lineNumber + ":" + ste[1].methodName + "()]"
        Log.d(TAG, text + message)
    }

    fun i(message: String) {
        val ste = Throwable().stackTrace
        val text = "[" + ste[1].fileName + ":" + ste[1].lineNumber + ":" + ste[1].methodName + "()]"
        Log.i(TAG, text + message)
    }

    fun e(message: String) {
        val ste = Throwable().stackTrace
        val text =
            "[" + ste[1].fileName + ":" + ste[1].lineNumber + ":" + ste[1].methodName + "()] !!!WARNING "
        Log.e(TAG, text + message)
    }

}