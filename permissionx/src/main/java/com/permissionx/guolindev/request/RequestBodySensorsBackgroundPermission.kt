/*
 * Copyright (C)  guolin, PermissionX Open Source Project
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
import com.permissionx.guolindev.PermissionBuilder
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.task.BaseTask

/**
 * 请求身体传感器权限
 */
internal class RequestBodySensorsBackgroundPermission internal constructor(permissionBuilder: PermissionBuilder)
    : BaseTask(permissionBuilder) {

    override fun request() {
        if (pb.shouldRequestBodySensorsBackgroundPermission()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                // 如果应用在 Android T 下运行 则没有 BODY_SENSORS_BACKGROUND 权限
                // 我们将其从请求列表中删除 但会将其作为拒绝权限附加到请求回调中
                pb.specialPermissions.remove(BODY_SENSORS_BACKGROUND)
                pb.permissionsWontRequest.add(BODY_SENSORS_BACKGROUND)
                finish()
                return
            }
            if (PermissionX.isGranted(pb.activity, BODY_SENSORS_BACKGROUND)) {
                // BODY_SENSORS_BACKGROUND 已经授权了 我们现在可以完成这个任务了
                finish()
                return
            }
            val bodySensorGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                PermissionX.isGranted(pb.activity, Manifest.permission.BODY_SENSORS)
            } else {
                false
            }
            if (bodySensorGranted) {
                if (pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) {
                    val requestList = mutableListOf(BODY_SENSORS_BACKGROUND)
                    if (pb.explainReasonCallbackWithBeforeParam != null) {
                        // callback ExplainReasonCallbackWithBeforeParam prior to ExplainReasonCallback
                        pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(getExplainScope(), requestList, true)
                    } else {
                        pb.explainReasonCallback!!.onExplainReason(getExplainScope(), requestList)
                    }
                } else {
                    // 没有执行explainReasonCallback 所以我们必须请求 BODY_SENSORS_BACKGROUND 而不做解释
                    requestAgain(emptyList())
                }
                return
            }
        }
        // 此时不应该请求 BODY_SENSORS_BACKGROUND 所以我们调用 finish() 来完成这个任务
        finish()
    }

    override fun requestAgain(permissions: List<String>) {
        // 不要关心权限参数是什么 总是请求 BODY_SENSORS_BACKGROUND
        pb.requestBodySensorsBackgroundPermissionNow(this)
    }

    companion object {
        /**
         * 定义 const 以与低于 T 的系统兼容
         */
        const val BODY_SENSORS_BACKGROUND = "android.permission.BODY_SENSORS_BACKGROUND"
    }
}