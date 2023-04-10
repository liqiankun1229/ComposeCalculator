package com.lqk.flutter.call_flutter

import android.content.Context
import android.util.Log
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel

/**
 * @author LQK
 * @time 2019/8/10 10:24
 * @remark 原生 -> Flutter 界面 数据交互
 */
class NativeCallFlutter(private var activity: Context) : EventChannel.StreamHandler {


    companion object {
        const val TAG = "NativeCallFlutter"
        const val CHANNEL = "plugin_call_flutter"
        var channel: EventChannel? = null
        var event: EventChannel.EventSink? = null

//        fun registerWith(plugin: PluginRegistry.Registrar) {
//            channel = EventChannel(plugin.messenger(), CHANNEL)
//            val instance = NativeCallFlutter(plugin.activity())
//            channel?.setStreamHandler(instance)
//        }

        fun registerWith(binding: FlutterPlugin.FlutterPluginBinding) {
            channel = EventChannel(binding.binaryMessenger, CHANNEL)
            val instance = NativeCallFlutter(binding.applicationContext)
            channel?.setStreamHandler(instance)
        }

        fun registerWith(plugin :FlutterPlugin){
//            this.channel = EventChannel()
        }
    }

    override fun onListen(arguments: Any?, eventChannel: EventChannel.EventSink?) {
        // 赋值, 保存通道
        event = eventChannel
//        Observable.interval(1000, TimeUnit.MILLISECONDS)
//                .subscribe(object : Observer<Long> {
//                    override fun onComplete() {
//
//                    }
//
//                    override fun onSubscribe(d: Disposable) {
//
//                    }
//
//                    override fun onNext(t: Long) {
//                        Run.onUiAsync(object : Action {
//                            override fun call() {
//                                eventChannel?.success(t)
//                            }
//                        })
//                    }
//
//                    override fun onError(e: Throwable) {
//                        eventChannel?.error("交互异常", "异常", e.message)
//                    }
//                })
    }

    override fun onCancel(p0: Any?) {
        Log.d("Plugin", CHANNEL)
    }

    fun sendNativeMessage(arguments: Any?) {
        event?.success(arguments)
    }

}