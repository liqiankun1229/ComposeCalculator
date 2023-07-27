package com.lqk.web.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import com.bumptech.glide.Glide
import com.lqk.web.log.ErrorHandle
import com.lqk.web.utils.MMKVUtils
import com.lqk.web.weex.EventModule
import com.lqk.web.weex.component.NativeRichText
import com.taobao.weex.InitConfig
import com.taobao.weex.InitConfig.Builder
import com.taobao.weex.WXEnvironment
import com.taobao.weex.WXSDKEngine
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsListener

/**
 * @author LQK
 * @time 2019/1/23 21:14
 * @remark
 */
class WebApplication : Application() {
    
    companion object {
        
        const val TAG = "WebApplication"
        private lateinit var instance: Application
        
        fun getApplication(): Application {
            return instance
        }
    }
    
    var topActivity: Activity? = null
    private val mRegisterLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            Log.d("lifecycle", "onActivityCreated: ${activity.localClassName}")
        }
        
        override fun onActivityStarted(activity: Activity) {
            Log.d("lifecycle", "onActivityStarted: ${activity.localClassName}")
        }
        
        override fun onActivityResumed(activity: Activity) {
            // 重新赋值 正在显示的 Activity
            (WebApplication.getApplication() as WebApplication).topActivity = activity
            Log.d("lifecycle", "onActivityResumed: ${activity.localClassName}")
        }
        
        override fun onActivityPaused(activity: Activity) {
            Log.d("lifecycle", "onActivityPaused: ${activity.localClassName}")
        }
        
        override fun onActivityStopped(activity: Activity) {
            Log.d("lifecycle", "onActivityStopped: ${activity.localClassName}")
        }
        
        override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
            Log.d("lifecycle", "onActivitySaveInstanceState: ${activity.localClassName}")
        }
        
        override fun onActivityDestroyed(activity: Activity) {
            Log.d("lifecycle", "onActivityDestroyed: ${activity.localClassName}")
        }
    }
    
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Log.d("start_time", "attachBaseContext: ${System.currentTimeMillis()}")
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        // 全局异常
        Thread {
            ErrorHandle.getInstance().initError(this)
        }.start()
        // 生命周期
        registerActivityLifecycleCallbacks(mRegisterLifecycleCallbacks)
        // mmkv
        MMKVUtils.initMMKV(this)
        // tbs
        initTbs()
        // Weex
        initWeex()
    }
    
    private fun initTbs() {
        // 初始化
        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
        // 初始化X5内核
        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.setTbsListener(
            object : TbsListener {
                override fun onDownloadFinish(p0: Int) {
                    Log.d("QbSdk", "onDownloadFinish -->下载X5内核完成: $p0")
                }
                
                override fun onInstallFinish(p0: Int) {
                    Log.d("QbSdk", "onInstallFinish -->安装X5内核进度: $p0")
                }
                
                override fun onDownloadProgress(p0: Int) {
                    Log.d("QbSdk", "onDownloadProgress -->下载X5内核进度: $p0")
                }
            })
        QbSdk.initX5Environment(this, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                // 内核初始化完成 可能是系统内核, 也可能是X5 内核
                Log.d(TAG, "onCoreInitFinished: ")
            }
            
            override fun onViewInitFinished(p0: Boolean) {
                // 预初始化完成
                Log.d(TAG, "onViewInitFinished: 内核加载 $p0")
            }
        })
        if (Build.VERSION.SDK_INT >= 29 && QbSdk.getTbsVersion(this) < 45114) {
            Log.d("Application", "onCreate: Build.VERSION.SDK_INT ${Build.VERSION.SDK_INT}")
            Log.d("Application", "onCreate: QbSdk.getTbsVersion ${QbSdk.getTbsVersion(this)}")
            Log.d("Application", "onCreate: QbSdk.VERSION ${QbSdk.VERSION}")
            
            QbSdk.forceSysWebView()
        }
    }
    
    /**
     * 初始化 Weex
     */
    private fun initWeex() {
        //Android 5.0 6.0不会出现问题，但是
        //Android 7.0以上，使用file:///会失败，通过这个判断去掉限制
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            val builder = VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
        }
        
        WXSDKEngine.addCustomOptions("appName", "WXSample")
        val config: InitConfig = Builder() //图片库接口
            .setImgAdapter { url, imageView, wxImageQuality, wxImageStrategy ->
                run {
                    Log.d(TAG, "setImage: $url")
                    Glide.with(this@WebApplication).load(url).into(imageView)
                }
            } //网络库接口
            .setHttpAdapter { wxRequest, adapterListener -> // 网络适配
                run {
                    Log.d(TAG, "sendRequest: ${wxRequest?.url}")
                }
            }
            .build()
        WXSDKEngine.initialize(this, config)
        // module 注册
        WXSDKEngine.registerModule(EventModule.MODULE_NAME, EventModule::class.java)
        
        // component 注册
        WXSDKEngine.registerComponent("nativerichtext", NativeRichText::class.java)
        
        // 开启调试
        WXEnvironment.sRemoteDebugMode = true
        WXEnvironment.sRemoteDebugProxyUrl = "ws://192.168.137.1:8089/debugProxy/native/143bfabd-fdca-4704-92a1-b13bd127b05a"
    }
}