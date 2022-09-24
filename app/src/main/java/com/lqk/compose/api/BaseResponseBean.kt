package com.lqk.compose.api

import java.io.Serializable

/**
 * @author LQK
 * @time 2022/9/24 21:57
 * 网络请求基类
 */
data class BaseResponseBean<T>(
    var code: Int,
    var msg: String,
    var data: T
) : Serializable