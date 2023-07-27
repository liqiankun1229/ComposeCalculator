package com.lqk.web.weex

import android.app.Activity
import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import com.lqk.utils.StatusBarUtil
import com.lqk.web.ui.widget.web.WebActivity
import com.lqk.web.ui.widget.web.local.PackageUtils
import com.lqk.web.ui.widget.web.local.cachePackage
import com.taobao.weex.annotation.JSMethod
import com.taobao.weex.bridge.JSCallback
import com.taobao.weex.common.WXModule

/**
 * @author LQK
 * @time 2023/4/25 15:47
 * 1. 状态栏配置 背景颜色 / icon 颜色
 * 2. 底部导航栏高度 是否显示
 * 3. 打开新页面 Weex
 */
class EventModule : WXModule() {
    
    companion object {
        
        private const val TAG = "EventModule"
        const val MODULE_NAME = "MyModule"
    }
    
    //run ui thread
    @JSMethod(uiThread = true)
    fun toast(msg: String) {
        if (msg == "package") {
            // 启动 package 离线包
            val localPack = PackageUtils.searchLocalPackage(msg)
            if (localPack != null) {
                val path = mWXSDKInstance.context.cachePackage()
                val htmlPath = "$path/${localPack.appId}/${localPack.home}"
                WebActivity.startLocal(mWXSDKInstance.context, "file:$htmlPath")
                return
            }
        }
        Toast.makeText(mWXSDKInstance.context, msg, Toast.LENGTH_SHORT).show()
    }
    
    //run JS thread
    @JSMethod(uiThread = false)
    fun fireEventSyncCall() {
    
    }
    
    @JSMethod
    fun gainStatusBarHeight(type: String, succeed: JSCallback) {
        Log.d(TAG, "setStatusBarIconColor: $type")
        StatusBarUtil.getStatusBarHeight(mWXSDKInstance.context as Activity)
    }
    
    /**
     * 设置状态栏颜色
     */
    @JSMethod
    fun setStatusBarIconColor(type: String) {
        Log.d(TAG, "setStatusBarIconColor: $type")
        if (type == "1") {
            // 黑色主题 -> 白色 icon
            StatusBarUtil.setDarkMode(mWXSDKInstance.context as Activity)
        } else {
            // 白色主题 -> 黑色 icon
            StatusBarUtil.setLightMode(mWXSDKInstance.context as Activity)
        }
    }
    
    /**
     * 获取 底部导航栏高度
     */
    @JSMethod
    fun navigationBarHeight(type: String) {
        val resources: Resources = mWXSDKInstance.context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        Log.d(
            TAG,
            "navigationBarHeight: ${if (resourceId != 0) resources.getDimensionPixelSize(resourceId) else 0}"
        )
    }
    
    @JSMethod
    fun canCallback(jsCallback: JSCallback) {
        jsCallback.invoke(
            hashMapOf(
                "funcName" to "canCallback",
                "funcCode" to 200,
                "data" to "succeed"
            )
        )
    }
    
    /**
     * 打开新页面
     */
    @JSMethod
    fun navOpen(url: String) {
        WeexActivity.start(mWXSDKInstance.context, url)
    }
}