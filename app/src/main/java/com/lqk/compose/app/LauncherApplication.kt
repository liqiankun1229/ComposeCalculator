package com.lqk.compose.app

import android.app.Application
import com.lqk.data.MMKVHelper
import com.lqk.flutter.FlutterModuleHelper

/**
 * @author LQK
 * @time 2022/9/25 22:00
 *
 */
class LauncherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化 mmkv
        MMKVHelper.initMMKV(this)
        // flutter 模块
        FlutterModuleHelper.initFlutter(this)
    }
}