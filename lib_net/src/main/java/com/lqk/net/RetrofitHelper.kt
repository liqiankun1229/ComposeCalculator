package com.lqk.net

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.SECONDS

/**
 * @author LQK
 * @time 2022/9/24 21:07
 *
 */
class RetrofitHelper private constructor() {
    private val retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.31.71:5000")
            .client(initOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())

            .build()
    }

    companion object {
        const val TAG = "RetrofitHelper"
        val instance: RetrofitHelper by lazy {
            RetrofitHelper()
        }
    }

    private fun initHeadersInterceptor() = Interceptor { chain ->
        // headers
        val request = chain.request()
            .newBuilder()
            // Headers 要可以提供给外部使用
            .also {
                initHeaderMap().forEach { entry ->
                    it.addHeader(entry.key, entry.value)
                }
            }
            .build()
        return@Interceptor chain.proceed(request)
    }

    private fun initHeaderMap() = hashMapOf<String, String>()

    /**
     * 日志拦截器
     */
    private fun initLogInterceptor() = HttpLoggingInterceptor {
        run {
            Log.d(TAG, "initLogInterceptor: ")
        }
    }.also {
        it.level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * 请求 OkHttp Client
     */
    private fun initOkHttpClient() = Builder()
        // 链接超时时间
        .connectTimeout(5, SECONDS)
        // 读取超时时间
        .readTimeout(5, SECONDS)
        // 添加 Headers
        .addInterceptor(initHeadersInterceptor())
        // 日志拦截器
        .addInterceptor(initLogInterceptor())
        .build()

    /**
     * 实例化
     */
    fun <T> initService(cls: Class<T>): T = retrofit.create(cls)
}