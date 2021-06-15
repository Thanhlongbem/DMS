package com.ziperp.dms.core.rest

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object RestService {
    private var retrofit: Retrofit? = null

    fun initRetrofit(){
        retrofit = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_PATH)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(OkInterceptor.gsonConverter)
            .client(OkInterceptor.client)
            .build()
    }
    fun <T> createService(interfaceClazz: Class<T>): T {
        if(retrofit == null){
            initRetrofit()
        }
        return retrofit!!.create(interfaceClazz)
    }
}

object RestServiceWithCache {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.API_BASE_PATH)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(OkInterceptor.gsonConverter)
            .client(OkInterceptor.clientCache)
            .build()
    }

    fun <T> createService(interfaceClazz: Class<T>): T {
        return retrofit.create(interfaceClazz)
    }
}