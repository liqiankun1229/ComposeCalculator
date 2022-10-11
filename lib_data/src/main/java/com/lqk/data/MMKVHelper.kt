package com.lqk.data

import android.app.Application
import android.os.Parcel
import android.os.Parcelable
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

    fun saveInt(k: String, v: Int) {
        mmkv?.encode(k, v)
    }

    fun gainInt(k: String, defaultInt: Int): Int {
        return mmkv?.decodeInt(k, defaultInt) ?: defaultInt
    }

    fun saveLong(k: String, v: Long) {
        mmkv?.encode(k, v)
    }

    fun gainLong(k: String, defaultLong: Long): Long {
        return mmkv?.decodeLong(k, defaultLong) ?: defaultLong
    }

    fun saveFloat(k: String, v: Float) {
        mmkv?.encode(k, v)
    }

    fun gainFloat(k: String, defaultFloat: Float): Float {
        return mmkv?.decodeFloat(k, defaultFloat) ?: defaultFloat
    }

    fun saveDouble(k: String, v: Double) {
        mmkv?.encode(k, v)
    }

    fun gainDouble(k: String, defaultDouble: Double): Double {
        return mmkv?.decodeDouble(k, defaultDouble) ?: defaultDouble
    }

    fun saveBoolean(k: String, v: Boolean) {
        mmkv?.encode(k, v)
    }

    fun gainBoolean(k: String, defaultBoolean: Boolean): Boolean {
        return mmkv?.getBoolean(k, defaultBoolean) ?: defaultBoolean
    }

    fun saveByteArray(k: String, v: ByteArray) {
        mmkv?.encode(k, v)
    }

    fun gainByteArray(k: String, defaultByteArray: ByteArray): ByteArray {
        return mmkv?.decodeBytes(k, defaultByteArray) ?: defaultByteArray
    }

    fun <T : Parcelable> saveParcelable(k: String, v: T) {
        mmkv?.encode(k, v)
    }

    fun <T : Parcelable> gainParcelable(k: String, defaultParcelable: Class<T>): T? {
        return mmkv?.decodeParcelable(k, defaultParcelable)
    }

    fun saveSetString(k: String, v: Set<String>) {
        mmkv?.encode(k, v)
    }

    fun gainSetString(k: String, defaultSetString: Set<String>): Set<String> {
        return mmkv?.decodeStringSet(k, defaultSetString) ?: defaultSetString
    }

}