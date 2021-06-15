package com.ziperp.dms.core.service

import com.ziperp.dms.main.login.model.ListServerResponse
import com.ziperp.dms.main.login.model.UserInfoRequest
import com.ziperp.dms.main.login.model.UserResponse
import com.ziperp.dms.main.login.model.UserToken
import io.reactivex.Single
import retrofit2.http.*

interface LoginService{

    @FormUrlEncoded
    @POST("/token")
    fun loginForm(
        @Field("grant_type") grantType: String = "password",
        @Field("username") userName: String,
        @Field("password") password: String
    ): Single<UserToken>

    @GET("/api/MainFrame/GetListDomain")
    fun getListServer(
        @Query("pLangId") pLangId: Int
    ): Single<ListServerResponse>

    @POST("/api/Account/GetUserAuthenticatedInfo")
    fun getUserInfo(
        @Body userName: UserInfoRequest
    ): Single<UserResponse>
}