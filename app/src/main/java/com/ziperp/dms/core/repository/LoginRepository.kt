package com.ziperp.dms.core.repository

import com.ziperp.dms.core.service.LoginService
import com.ziperp.dms.main.login.model.ListServerResponse
import com.ziperp.dms.main.login.model.UserInfoRequest
import com.ziperp.dms.main.login.model.UserResponse
import com.ziperp.dms.main.login.model.UserToken
import io.reactivex.Single

class LoginRepository(private val loginService: LoginService) {

    fun loginForm(username: String, password: String): Single<UserToken> {
        return loginService
            .loginForm("password", username, password);
    }

    fun getListServer(pLangId: Int): Single<ListServerResponse> {
        return loginService
            .getListServer(pLangId);
    }

    fun getUserInfo(bodyRequest: UserInfoRequest): Single<UserResponse>{
        return loginService.getUserInfo(bodyRequest);
    }


}