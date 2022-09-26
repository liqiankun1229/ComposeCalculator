package com.lqk.base.helper

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * @author LQK
 * @time 2022/9/26 23:14
 * 全局唯一 Gson对象
 */
object GsonHelper {

    const val TAG = "GSON"

    private val gson: Gson by lazy {
        GsonBuilder().create()
    }

    fun instance(): Gson {
        return gson
    }

    fun <T> fromJson(jsonStr: String, t: Class<T>): T {
        return gson.fromJson(jsonStr, t)
    }

    fun <T> toJson(cls: Class<T>): String {
        return gson.toJson(cls)
    }
}