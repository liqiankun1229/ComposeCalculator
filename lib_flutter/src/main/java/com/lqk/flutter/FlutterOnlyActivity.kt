package com.lqk.flutter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.lqk.flutter.call_flutter.FlutterCallNative
import com.lqk.flutter.call_flutter.NativeCallFlutter
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.JSONMessageCodec
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant


/**
 * @author LQK
 * @time 2020/4/3 1:49
 * @remark 全部界面为 flutter 实现
 */
class FlutterOnlyActivity : FlutterActivity() {
    companion object {

        const val TAG = "NativeFlutterActivity"
        fun show(context: Context) {
            context.startActivity(Intent(context, FlutterOnlyActivity::class.java))
        }
    }
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        // 注册引擎
        GeneratedPluginRegistrant.registerWith(flutterEngine)
        // 注册方法
        MethodChannel(
            flutterEngine.dartExecutor,
            FlutterCallNative.CHANNEL
        ).setMethodCallHandler { call, result ->
            run {
                Log.d(TAG, "onCreate: ${call.method}")
                when (call.method) {
                    "oneAct" -> {
                        val s = call.arguments.toString()
                        Toast.makeText(this@FlutterOnlyActivity, s, Toast.LENGTH_SHORT).show()
                    }
                    "twoAct" -> {
                        val s = call.arguments.toString()
                        Toast.makeText(this@FlutterOnlyActivity, s, Toast.LENGTH_SHORT).show()
                    }
                    "jump" -> {
                        Toast.makeText(this@FlutterOnlyActivity, "跳转", Toast.LENGTH_SHORT).show()
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
        // 调用传值到 Flutter
        EventChannel(
            flutterEngine.dartExecutor,
            NativeCallFlutter.CHANNEL
        ).setStreamHandler(object : EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                // 保存 events 对象, 用于给 Flutter 传值

            }

            override fun onCancel(arguments: Any?) {

            }
        })
        // 消息
        val basicMessageChannel = BasicMessageChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            "login_message",
            JSONMessageCodec.INSTANCE
        )
        basicMessageChannel.setMessageHandler { message, reply ->
            run {
                Log.d(TAG, "configureFlutterEngine: $message")
                reply.reply(message)
            }
        }
    }

    private fun sendMsg() {

    }

//    @SuppressLint("VisibleForTests")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_my_flutter)
//        val flutterView = FlutterView(this)
//        if (flutterView.parent == null) {
//            fl.removeAllViews()
//            fl.addView(flutterView)
//        }
//    }

}