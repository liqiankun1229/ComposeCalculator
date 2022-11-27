package com.lqk.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

/**
 * @author LQK
 * @time 2022/11/6 22:12
 *
 */
object SPHelper {
    fun initSP(application: Application) {
        var shared = application.getSharedPreferences("APP", Context.MODE_PRIVATE)
    }
}