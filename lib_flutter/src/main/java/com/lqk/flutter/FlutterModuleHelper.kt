package com.lqk.flutter

import android.app.Application
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.embedding.engine.loader.FlutterLoader

/**
 * @author LQK
 * @time 2023/2/6 22:31
 *
 */
object FlutterModuleHelper {
    const val engineId = "my_engine_id"

    private lateinit var flutterEngine: FlutterEngine
    fun initFlutter(application: Application) {
        // 初始化 flutter
        val loader = FlutterLoader()
        val loaderSetting = FlutterLoader.Settings()
        loaderSetting.logTag = "MyFlutter"

        flutterEngine = FlutterEngine(application)

        flutterEngine.navigationChannel.setInitialRoute("flutter_login_activity")

        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        FlutterEngineCache.getInstance().put(engineId, flutterEngine)
        loader.startInitialization(application, loaderSetting)
    }
}