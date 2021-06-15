package com.ziperp.dms.core.rest

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.gson.GsonBuilder
import com.ziperp.dms.App
import com.ziperp.dms.BuildConfig
import com.ziperp.dms.core.token.DmsUserManager
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit

object OkInterceptor {
    private var mClient: OkHttpClient? = null
    private var mClientCache: OkHttpClient? = null
    private var mGsonConverter: GsonConverterFactory? = null
    const val TIMEOUT_TIME = 20
    const val AUTHORIZATION = "Authorization"
    const val ROUTER_AUTHORIZATION = "Access-Token"
    const val APPLICATION_JON = "application/json"

    /**
     * Don't forget to remove Interceptors (or change Logging Level to NONE)
     * in production! Otherwise people will be able to see your request and response on Log Cat.
     */
    val client: OkHttpClient
        @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
        get() {
            if (mClient == null) {
                val httpBuilder = OkHttpClient.Builder()
                httpBuilder
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)

                httpBuilder.addInterceptor(OAuthInterceptor())

                if (BuildConfig.DEBUG) {
                    val interceptor = HttpLoggingInterceptor()
                    interceptor.level = HttpLoggingInterceptor.Level.BODY
                    httpBuilder.addInterceptor(interceptor)  /// show all JSON in logCat
                }

                mClient = httpBuilder.build()

            }
            return mClient!!
        }


    val clientCache: OkHttpClient
        @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
        get() {
            if (mClientCache == null) {

                val cacheSize = (5 * 1024 * 1024).toLong()
                val myCache = Cache(App.shared().cacheDir, cacheSize)

                val httpBuilder = OkHttpClient.Builder()
                httpBuilder
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    // Specify the cache we created earlier.
                    .cache(myCache)


                httpBuilder.addInterceptor(CacheInterceptor())

                if (BuildConfig.DEBUG) {
                    val interceptor = HttpLoggingInterceptor()
                    interceptor.level = HttpLoggingInterceptor.Level.BODY
                    httpBuilder.addInterceptor(interceptor)  /// show all JSON in logCat
                }

                mClientCache = httpBuilder.build()

            }
            return mClientCache!!
        }


    class CacheInterceptor() : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            // Get the request from the chain.
            var request = chain.request()
            request = if (hasNetwork(App.shared())){
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
            }else{
                request.newBuilder().header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                ).build()
            }

            // End of if-else statement
            val response: Response = chain.proceed(request)
            if (response.cacheControl != null) {
                // from cache
            } else if (response.networkResponse != null) {
                // from network
            }
            // Add the modified request to the chain.
            return response
        }
    }

    fun hasNetwork(context: Context): Boolean {
        var isConnected: Boolean = false // Initial Value
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

    class OAuthInterceptor() : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", APPLICATION_JON)
                .addHeader("Accept", APPLICATION_JON)


            DmsUserManager.accessToken?.let {
                request.addHeader("Authorization", "Bearer $it")
            }

            return chain.proceed(request.build())
        }
    }

    val gsonConverter: GsonConverterFactory
        get() {
            if (mGsonConverter == null) {
                mGsonConverter = GsonConverterFactory
                    .create(
                        GsonBuilder()
                            .setLenient()
                            .disableHtmlEscaping()
                            .create()
                    )
            }
            return mGsonConverter!!
        }
}