package com.ziperp.dms.utils

import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.ziperp.dms.App

object NetWorkConnection{
    fun isNetworkAvailable(): Boolean {
    val connectivityManager = App.shared().applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    connectivityManager.apply {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }
}

}