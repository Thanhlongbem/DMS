package com.ziperp.dms.main.login.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.core.repository.LoginRepository
import com.ziperp.dms.core.rest.Constants.PREF_CURRENT_LANGUAGE
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.login.model.ListServerResponse
import com.ziperp.dms.main.login.model.UserInfoRequest
import com.ziperp.dms.main.login.model.UserResponse
import com.ziperp.dms.utils.DMSPreference
import com.ziperp.dms.utils.LogUtils
import io.reactivex.schedulers.Schedulers

class LoginViewModel(private val loginRepository: LoginRepository) : BaseViewModel() {

    val userInfoLiveData = MutableLiveData<ResponseData<UserResponse>>()
    val listServerLiveData = MutableLiveData<ResponseData<ListServerResponse>>()

    fun login(userName: String, password: String) {
        userInfoLiveData.postValue(ResponseData.loading(null))
        loginRepository.loginForm(userName, password)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { userToken ->
                DmsUserManager.setUserToken(userToken)
            }
            .flatMap {
                loginRepository.getUserInfo(UserInfoRequest(userName))
            }
            .subscribe({
                LogUtils.d("Save user info ${it.table[0].staffNm}")
                DmsUserManager.updateUserInfo(it.table[0])
                userInfoLiveData.postValue(ResponseData.success(it))
            }, {
                userInfoLiveData.postValue(
                    ResponseData.error(
                        "Error login: " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getUserInfo(userName: String){
        userInfoLiveData.postValue(ResponseData.loading(null))
        loginRepository.getUserInfo(UserInfoRequest(userName))
            .subscribeOn(Schedulers.io())
            .subscribe({
                userInfoLiveData.postValue(ResponseData.success(it))
            }, {
                userInfoLiveData.postValue(
                    ResponseData.error(
                        "Error getServer: " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun getListServer() {
        listServerLiveData.postValue(ResponseData.loading(null))
        loginRepository.getListServer(DMSPreference.getInt(PREF_CURRENT_LANGUAGE, 1))
            .subscribeOn(Schedulers.io())
            .subscribe({
                listServerLiveData.postValue(ResponseData.success(it))
            }, {
                listServerLiveData.postValue(
                    ResponseData.error(
                        "Error getServer: " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }
}