package com.shoppingfoodcart.firstassigment.datasource.network

import android.annotation.SuppressLint
import android.content.Context
import androidx.viewbinding.BuildConfig
import com.shoppingfoodcart.firstassigment.utils.general.AppConstants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitClientInstance(ctx: Context) {
    private var retrofit: Retrofit? = null
    private val httpClient = OkHttpClient.Builder()
    var context: Context

    init {
        context = ctx
        if (retrofit == null) {
            initRetrofit()
        }
    }

    fun initRetrofit() {

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        httpClient.callTimeout(120, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val loggingIntercepter = getLoggingInterceptor()
            loggingIntercepter.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(loggingIntercepter)
        }
        retrofitBuilder.client(httpClient.build())
        retrofit = retrofitBuilder.build()
    }

    fun getService(): ApiService {
        return retrofit!!.create<ApiService>(ApiService::class.java!!)
    }


    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var singleInstance: RetrofitClientInstance? = null

        fun getInstance(context: Context): RetrofitClientInstance? {
            if (singleInstance == null)
                singleInstance =
                    RetrofitClientInstance(
                        context
                    )

            return singleInstance
        }
    }


}