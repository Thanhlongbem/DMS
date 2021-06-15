package com.ziperp.dms.core.token

import android.text.TextUtils
import com.google.gson.Gson
import com.ziperp.dms.main.login.model.UserInfo
import com.ziperp.dms.main.login.model.UserToken
import com.ziperp.dms.utils.DMSPreference
import com.ziperp.dms.utils.LogUtils

object DmsUserManager {

    private const val KEY_USER_INFO = "KEY_USER_INFO"
    private const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"

    private var userInfoDisk: UserInfo? = null
    private var accessTokenDisk: String? = null

    var userInfo: UserInfo = UserInfo()
        get() {
            if (userInfoDisk != null && userInfoDisk!!.userId.isNotEmpty()) {
                return userInfoDisk!!
            }
            val json = DMSPreference.getString(KEY_USER_INFO)
            if (!json.isNullOrEmpty()) {
                try {
                    val userCache = Gson().fromJson(json, UserInfo::class.java)
                    if (userCache != null && userCache.userId.isNotEmpty()) {
                        userInfoDisk = userCache
                        return userCache
                    }
                } catch (e: Exception) {
                    LogUtils.d("Could not parse")
                }
            }

            return UserInfo()
        }

    var accessToken: String? = null
        get() {
            if (accessTokenDisk == null) {
                accessTokenDisk = getUserToken()?.accessToken
            }
            return accessTokenDisk
        }

    fun isValidUserInfo(): Boolean{
        return userInfo != null && !(userInfo!!.userId.isNullOrEmpty()) && accessToken != null
    }

    fun updateUserInfo(userInfo: UserInfo) {
        val json = Gson().toJson(userInfo)
        DMSPreference.setString(KEY_USER_INFO, json)
    }

    fun getUserToken(): UserToken? {
        val value = DMSPreference.getString(KEY_ACCESS_TOKEN)
        if (!TextUtils.isEmpty(value)) {
            try{
                var userToken = Gson().fromJson(value, UserToken::class.java)
                if (userToken != null && !TextUtils.isEmpty(userToken.accessToken)) {
                    return userToken
                }
            }catch (e: Exception){
                LogUtils.d("Could not parse")
            }
        }
        return null
    }

    fun setUserToken(userToken: UserToken) {
        if (!TextUtils.isEmpty(userToken.accessToken)) {
            DMSPreference.setString(KEY_ACCESS_TOKEN, Gson().toJson(userToken))
        }
    }

    fun logout(){
        clearUserInfo()
        clearUserToken()
        userInfoDisk = null
        accessTokenDisk = null
    }

    fun clearUserInfo() {
        DMSPreference.setString(KEY_USER_INFO, "")
    }

    fun clearUserToken() {
        DMSPreference.setString(KEY_ACCESS_TOKEN, "")
    }

}