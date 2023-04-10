package com.lqk.flutter

import android.util.Log
import android.widget.Toast
import com.lqk.flutter.call_flutter.FlutterCallNative
import com.lqk.flutter.call_flutter.NativeCallFlutter
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.JSONMessageCodec
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

/**
 * @author LQK
 * @time 2022/1/19 21:09
 * @remark
 */
class NativeFlutterFragment : FlutterFragment {

    private var activityMsg: OnActivityMsgListener? = null

    constructor() : this(null)

    constructor(activityMsg: OnActivityMsgListener?) {
        this.activityMsg = activityMsg
    }

    companion object {
        const val KEY_SEND_TO_FLUTTER = "KEY_SEND_TO_FLUTTER"
        private const val TAG = "NativeFlutterFragment"
    }

    fun sendToFlutterSuccess(str: String) {
        Log.d(TAG, "sendFlutter: $str")
        nativeCallFlutter?.success(str)
    }

    /**
     * 给 flutter 发送事件
     */
    fun sendToFlutterError(errCode: String, errMsg: String, errDesc: Any?) {
        Log.d(TAG, "sendToFlutter: $errCode : $errMsg : $errDesc")
        nativeCallFlutter?.error(errCode, errMsg, errDesc)
    }

    /**
     * 发送消息
     */
    fun handleOnMsg() {
        basicMessageChannel?.send("原生发送消息")
//        msgHandler?.onMessage(ByteBuffer.wrap("{\"name\":\"lqk\"}".toByteArray())) {
//            Toast.makeText(
//                this@NativeFlutterFragment.requireContext(),
//                "setMessageHandler:onMessage:$it",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
    }

    // 消息渠道
    private var basicMessageChannel: BasicMessageChannel<Any>? = null

    // 通道
    private var nativeCallFlutter: EventChannel.EventSink? = null

    ///<editor-fold desc="message 监听">
//    object : BinaryMessenger {
//        override fun send(channel: String, message: ByteBuffer?) {
//            Toast.makeText(
//                this@NativeFlutterFragment.requireContext(),
//                "send(2):$channel;$message",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//
//        override fun send(
//            channel: String,
//            message: ByteBuffer?,
//            callback: BinaryMessenger.BinaryReply?
//        ) {
//            Toast.makeText(
//                this@NativeFlutterFragment.requireContext(),
//                "send(3):$channel;$message",
//                Toast.LENGTH_SHORT
//            ).show()
//            callback?.reply(ByteBuffer.wrap("消息:send(3)".toByteArray()))
//        }
//
//        override fun setMessageHandler(
//            channel: String,
//            handler: BinaryMessenger.BinaryMessageHandler?
//        ) {
//            this@NativeFlutterFragment.msgHandler = handler
//            Toast.makeText(
//                this@NativeFlutterFragment.requireContext(),
//                "setMessageHandler:$channel;",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
    ///</editor-fold>

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        // 注册引擎
        GeneratedPluginRegistrant.registerWith(flutterEngine)
        // 注册方法 Flutter -> 原生 BinaryMessenger
        MethodChannel(
            flutterEngine.dartExecutor,
            FlutterCallNative.CHANNEL
        ).setMethodCallHandler { call, result ->
            run {
                Log.d(FlutterOnlyActivity.TAG, "onCreate: ${call.method}")
                when (call.method) {
                    "oneAct" -> {
                        val s = call.arguments.toString()
                        Toast.makeText(
                            this@NativeFlutterFragment.requireContext(),
                            s,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    "twoAct" -> {
                        val s = call.arguments.toString()
                        Toast.makeText(
                            this@NativeFlutterFragment.requireContext(),
                            s,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    "jump" -> {
                        Toast.makeText(
                            this@NativeFlutterFragment.requireContext(),
                            "跳转",
                            Toast.LENGTH_SHORT
                        ).show()
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
        // 调用传值 原生 -> Flutter BinaryMessenger
        EventChannel(
            flutterEngine.dartExecutor,
            NativeCallFlutter.CHANNEL
        ).setStreamHandler(object : EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                this@NativeFlutterFragment.nativeCallFlutter = events
                Log.d(TAG, "onListen: $arguments ")
            }

            override fun onCancel(arguments: Any?) {
                Log.d(TAG, "onCancel: $arguments")
            }
        })
        // message 消息互通 BinaryMessenger
        basicMessageChannel = BasicMessageChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            "login_message",
            JSONMessageCodec.INSTANCE
        )
        // 接受消息 reply 进行回复
        basicMessageChannel?.setMessageHandler { message, reply ->
            run {
                Log.d(TAG, "configureFlutterEngine: $message")
                reply.reply(message)
            }
        }
    }
}