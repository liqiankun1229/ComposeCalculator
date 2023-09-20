package com.lqk.flutter.call_flutter

import android.content.Context
import android.util.Log
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry

/**
 * @author LQK
 * @time 2019/8/10 10:53
 * @remark Flutter 界面 -> 原生 数据交互
 */
class FlutterCallNative(private var activity: Context) : MethodChannel.MethodCallHandler {

    companion object {
        const val TAG = "FlutterCallNative"
        const val CHANNEL = "plugin_call_native"

        fun registerWith(register: PluginRegistry.Registrar) {
//            var channel = MethodChannel(register.messenger(), CHANNEL)
//            val instance = FlutterCallNative(register.activity())
//            channel.setMethodCallHandler(instance)
        }

        fun registerWith(binding: FlutterPlugin.FlutterPluginBinding) {
            var channel = MethodChannel(binding.binaryMessenger, CHANNEL)
            val instance = FlutterCallNative(binding.applicationContext)
            channel.setMethodCallHandler(instance)
        }

//        fun registerWith(plugin:FlutterPlugin){
//            channel = MethodChannel()
//        }
    }

    /**
     * 接收 Flutter 传递给原生的函数调用
     */
    override fun onMethodCall(methodCall: MethodCall, result: MethodChannel.Result) {
        when (methodCall.method) {
            "oneAct" -> {
                Log.d(TAG, "oneAct Response")
                result.success("One Success")
            }
            "twoAct" -> {
                Log.d(TAG, "twoAct Response")
                val json = methodCall.argument<String>("data")
                Log.d(TAG, "$json")
                result.success("Two Success")
            }
            "jump" -> {
                Log.d(TAG, "Flutter Call jump")
                result.success("Jump Success")
            }
            "jumpLogin" -> {
                result.success("正在跳转 jumpLogin")
            }
            "jumpMethod" -> {
                result.error("500", "正在跳转 jumpMethod", null)
            }
            else -> {
                result.notImplemented()
            }
        }
    }
}