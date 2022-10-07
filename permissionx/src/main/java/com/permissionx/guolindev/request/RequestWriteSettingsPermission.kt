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

import android.Manifest
import android.os.Build
import android.provider.Settings

/**
 * 请求修改系统设置的权限 android.permission.WRITE_SETTINGS.
 *
 */
internal class RequestWriteSettingsPermission internal constructor(permissionBuilder: PermissionBuilder) :
    BaseTask(permissionBuilder) {

    override fun request() {
        if (pb.shouldRequestWriteSettingsPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && pb.targetSdkVersion >= Build.VERSION_CODES.M) {
                if (Settings.System.canWrite(pb.activity)) {
                    // 已授予 WRITE_SETTINGS 权限 我们现在可以完成此任务
                    finish()
                    return
                }
                if (pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) {
                    val requestList = mutableListOf(Manifest.permission.WRITE_SETTINGS)
                    if (pb.explainReasonCallbackWithBeforeParam != null) {
                        // 在 ExplainReasonCallback 之前回调 ExplainReasonCallbackWithBeforeParam
                        pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(getExplainScope(), requestList, true)
                    } else {
                        pb.explainReasonCallback!!.onExplainReason(getExplainScope(), requestList)
                    }
                } else {
                    // 没有执行 explainReasonCallback
                    // 此时我们不能请求 WRITE_SETTINGS 权限 因为用户不明白为什么
                    finish()
                }
            } else {
                // WRITE_SETTINGS 权限在 Android M 以下自动授予
                pb.grantedPermissions.add(Manifest.permission.WRITE_SETTINGS)
                // 此时 不应再对 WRITE_SETTINGS 权限进行特殊处理
                pb.specialPermissions.remove(Manifest.permission.WRITE_SETTINGS)
                finish()
            }
        } else {
            // 此时不应请求 WRITE_SETTINGS 权限
            // 因此我们调用 finish() 来结束此任务
            finish()
        }
    }

    override fun requestAgain(permissions: List<String>) {
        // 不在乎权限参数是什么 始终请求 WRITE_SETTINGS 权限
        pb.requestWriteSettingsPermissionNow(this)
    }
}