package com.lqk.flutter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lqk.flutter.databinding.ActivityFlutterNativeBinding

class FlutterNativeActivity : AppCompatActivity(), OnActivityMsgListener {

    companion object {
        const val TAG = "MyFlutterActivity"
    }
    override fun <T> send(a: T) {

    }

    private lateinit var viewBinding: ActivityFlutterNativeBinding

    private var fr = NativeFlutterFragment(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setLightMode(this)
        viewBinding = ActivityFlutterNativeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        Log.d(TAG, "onCreate: ")
        initListener()
        initEvent()

        // 添加 flutter fragment 到
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fl, fr)
                .commit()
    }

    private fun initEvent() {
        LiveEventBus.get(TAG, String::class.java).observeForever {
            Log.d(TAG, "initEvent: $it")
            if (it == "succeed") {
                callPhone()
            }
        }
    }

    private fun callPhone() {

    }

    private fun initListener() {
        viewBinding.tv.setOnClickListener {
            FlutterOnlyActivity.show(this)
        }
        viewBinding.btnSendSucceed.setOnClickListener {
            fr.sendToFlutterSuccess("成功")
        }
        viewBinding.callStart.setOnClickListener {
            fr.sendToFlutterError("500", "未知错误", "网络请求失败")
        }
        viewBinding.callStop.setOnClickListener {
            fr.handleOnMsg()
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: ")
    }

    inner class Time {
        var time = ""
    }
}