/*
 * Copyright (C) guolin, PermissionX Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.permissionx.guolindev.request

import android.os.Build
import android.os.Environment

/**
 * 请求管理外部存储权限 android.permission.MANAGE_EXTERNAL_STORAGE
 */
internal class RequestManageExternalStoragePermission internal constructor(permissionBuilder: PermissionBuilder) :
    BaseTask(permissionBuilder) {

    override fun request() {
        if (pb.shouldRequestManageExternalStoragePermission() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                // MANAGE_EXTERNAL_STORAGE 权限已经授予 我们现在可以完成这个任务了
                finish()
                return
            }
            if (pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) {
                val requestList = mutableListOf(MANAGE_EXTERNAL_STORAGE)
                if (pb.explainReasonCallbackWithBeforeParam != null) {
                    // 在 ExplainReasonCallback 之前回调 ExplainReasonCallbackWithBeforeParam
                    pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(getExplainScope(), requestList, true)
                } else {
                    pb.explainReasonCallback!!.onExplainReason(getExplainScope(), requestList)
                }
            } else {
                // 没有执行 explainReasonCallback
                // 此时我们不能请求 MANAGE_EXTERNAL_STORAGE 权限，因为用户不明白为什么
                finish()
            }
            return
        }
        // 此时不应请求 MANAGE_EXTERNAL_STORAGE 权限
        // 因此我们调用 finish() 来结束此任务
        finish()
    }

    /**
     * 封装的是 管理外部存储权限
     */
    override fun requestAgain(permissions: List<String>) {
        // 不在乎权限参数是什么, 总是请求 WRITE_SETTINGS 权限
        pb.requestManageExternalStoragePermissionNow(this)
    }

    companion object {
        /**
         * 定义 const 以与低于 R 的系统兼容
         */
        const val MANAGE_EXTERNAL_STORAGE = "android.permission.MANAGE_EXTERNAL_STORAGE"
    }
}