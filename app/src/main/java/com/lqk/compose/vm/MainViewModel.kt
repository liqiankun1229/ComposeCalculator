package com.lqk.compose.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import com.lqk.compose.api.ServiceAPI
import com.lqk.net.RetrofitHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

/**
 * @author LQK
 * @time 2022/9/24 21:58
 *
 */
class MainViewModel : ViewModel() {
    companion object {
        const val TAG = "MainViewModel"
    }

    fun loadPackage() {
        viewModelScope.launch {
            try {
                val result = RetrofitHelper.instance.initService(ServiceAPI::class.java).initPackageList()
                result.data.forEach {
                    Log.d(TAG, "loadPackage: ${it.name} : ${it.url}")
                }
            } catch (e: Throwable) {
                val msg = when (e) {
                    is CancellationException -> {
                        // 请求被关闭
                        "请求被关闭"
                    }
                    is SocketTimeoutException -> {
                        // 链接超时
                        "链接超时"
                    }
                    is JsonParseException -> {
                        // 数据解析失败
                        "数据解析失败"
                    }
                    is HttpException -> {
                        when (e.code()) {
                            405 -> {
                                "链接不存在"
                            }
                            else -> {
                                "未知错误"
                            }
                        }
                    }
                    else -> {
                        // 未知错误
                        "未知错误"
                    }
                }
                Log.d("error", "upData2: ${e.message}")
                Log.d("error", "upData2: ${e.cause}")
                e.printStackTrace()
            }
        }
    }
}