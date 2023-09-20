package com.lqk.compose.ui

import android.util.Log
import kotlin.concurrent.thread
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume

/**
 * @author LQK
 * @time 2022/8/22 9:11
 *
 */
object URL {
    private const val TAG = "URL"
    const val BASE_URL = "http://10.100.158.101:8080"


    /**
     * 创建协程
     */
    fun created() {
    }
}