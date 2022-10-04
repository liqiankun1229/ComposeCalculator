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
                // MANAGE_EXTERNAL_STORAGE permission has already granted, we can finish this task now.
                finish()
                return
            }
            if (pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) {
                val requestList = mutableListOf(MANAGE_EXTERNAL_STORAGE)
                if (pb.explainReasonCallbackWithBeforeParam != null) {
                    // callback ExplainReasonCallbackWithBeforeParam prior to ExplainReasonCallback
                    pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(explainScope, requestList, true)
                } else {
                    pb.explainReasonCallback!!.onExplainReason(explainScope, requestList)
                }
            } else {
                // No implementation of explainReasonCallback, we can't request
                // MANAGE_EXTERNAL_STORAGE permission at this time, because user won't understand why.
                finish()
            }
            return
        }
        // shouldn't request MANAGE_EXTERNAL_STORAGE permission at this time, so we call finish()
        // to finish this task.
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
         * Define the const to compat with system lower than R.
         */
        const val MANAGE_EXTERNAL_STORAGE = "android.permission.MANAGE_EXTERNAL_STORAGE"
    }
}