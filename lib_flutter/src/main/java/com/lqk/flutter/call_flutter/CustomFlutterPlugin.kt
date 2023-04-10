package com.lqk.flutter.call_flutter

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

/**
 * 插件开发
 */
class CustomFlutterPlugin : FlutterPlugin, ActivityAware {
    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {

    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {

    }


    // 与UI 进行交互 请求权限或更改 Android UI
    override fun onAttachedToActivity(binding: ActivityPluginBinding) {

    }

    override fun onDetachedFromActivityForConfigChanges() {

    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {

    }

    override fun onDetachedFromActivity() {

    }
}