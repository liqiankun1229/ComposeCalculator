package com.lqk.data

import android.app.Application
import android.util.Log
import com.tencent.mmkv.MMKV

/**
 * @author LQK
 * @time 2022/9/25 18:45
 *
 */
object MMKVHelper {
    private var mmkv: MMKV? = null
    private const val TAG = "MMKV"

    fun instance(): MMKV {
        if (mmkv == null) {
            mmkv = MMKV.defaultMMKV()
            // throw NullPointerException("mmkv is not initialize!!!")
        }
        return mmkv!!
    }

    fun initMMKV(application: Application) {
        // context.getFilesDir().getAbsolutePath() + "/mmkv"
        val rootPath = MMKV.initialize(application)
        instance()
        Log.d(TAG, "initMMKV: $rootPath")
    }

    fun saveString(k: String, v: String) {
        mmkv?.encode(k, v)
    }

    /**
     * 默认返回 空字符串
     */
    fun gainString(k: String, defaultStr: String = ""): String {
        return mmkv?.decodeString(k) ?: defaultStr
    }

}