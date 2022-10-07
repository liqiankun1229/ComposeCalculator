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
 * 请求系统对话框 android.permission.SYSTEM_ALERT_WINDOW.
 */
internal class RequestSystemAlertWindowPermission internal constructor(permissionBuilder: PermissionBuilder) :
    BaseTask(permissionBuilder) {

    override fun request() {
        if (pb.shouldRequestSystemAlertWindowPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && pb.targetSdkVersion >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(pb.activity)) {
                    // SYSTEM_ALERT_WINDOW 权限已经授予 我们现在可以完成这个任务了
                    finish()
                    return
                }
                if (pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) {
                    val requestList = mutableListOf(Manifest.permission.SYSTEM_ALERT_WINDOW)
                    if (pb.explainReasonCallbackWithBeforeParam != null) {
                        // 在 ExplainReasonCallback 之前回调 ExplainReasonCallbackWithBeforeParam
                        pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(getExplainScope(), requestList, true)
                    } else {
                        pb.explainReasonCallback!!.onExplainReason(getExplainScope(), requestList)
                    }
                } else {
                    // 没有执行 explainReasonCallback
                    // 此时我们不能请求 SYSTEM_ALERT_WINDOW 权限 因为用户不明白为什么
                    finish()
                }
            } else {
                // SYSTEM_ALERT_WINDOW 权限在 Android M 以下自动授予
                pb.grantedPermissions.add(Manifest.permission.SYSTEM_ALERT_WINDOW)
                // 此时 SYSTEM_ALERT_WINDOW 权限不应再进行特殊处理
                pb.specialPermissions.remove(Manifest.permission.SYSTEM_ALERT_WINDOW)
                finish()
            }
        } else {
            // 此时不应该请求 SYSTEM_ALERT_WINDOW 权限
            // 所以我们调用 finish() 来结束这个任务
            finish()
        }
    }

    override fun requestAgain(permissions: List<String>) {
        // 不在乎权限参数是什么 始终请求 SYSTEM_ALERT_WINDOW 权限
        pb.requestSystemAlertWindowPermissionNow(this)
    }
}