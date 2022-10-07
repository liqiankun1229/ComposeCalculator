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
import com.permissionx.guolindev.PermissionX

/**
 * 请求 后台定位权限{ @link ACCESS_BACKGROUND_LOCATION }
 *
 */
internal class RequestBackgroundLocationPermission internal constructor(permissionBuilder: PermissionBuilder)
    : BaseTask(permissionBuilder) {

    override fun request() {
        if (pb.shouldRequestBackgroundLocationPermission()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                // 如果应用在 Android Q 下运行 则没有 ACCESS_BACKGROUND_LOCATION 权限
                // 我们将其从请求列表中删除，但会将其作为拒绝权限附加到请求回调中。
                pb.specialPermissions.remove(ACCESS_BACKGROUND_LOCATION)
                pb.permissionsWontRequest.add(ACCESS_BACKGROUND_LOCATION)
                finish()
                return
            }
            if (PermissionX.isGranted(pb.activity, ACCESS_BACKGROUND_LOCATION)) {
                // ACCESS_BACKGROUND_LOCATION 已经授权了，我们现在可以完成这个任务了
                finish()
                return
            }
            val accessFindLocationGranted = PermissionX.isGranted(pb.activity, Manifest.permission.ACCESS_FINE_LOCATION)
            val accessCoarseLocationGranted = PermissionX.isGranted(pb.activity, Manifest.permission.ACCESS_COARSE_LOCATION)
            if (accessFindLocationGranted || accessCoarseLocationGranted) {
                if (pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) {
                    val requestList = mutableListOf(ACCESS_BACKGROUND_LOCATION)
                    if (pb.explainReasonCallbackWithBeforeParam != null) {
                        // 在 ExplainReasonCallback 之前回调 ExplainReasonCallbackWithBeforeParam
                        pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(getExplainScope(), requestList, true)
                    } else {
                        pb.explainReasonCallback!!.onExplainReason(getExplainScope(), requestList)
                    }
                } else {
                    // 没有实现explainReasonCallback 所以我们必须请求 ACCESS_BACKGROUND_LOCATION 而不做解释
                    requestAgain(emptyList())
                }
                return
            }
        }
        // 此时不应该请求 ACCESS_BACKGROUND_LOCATION 所以我们调用 finish() 来结束这个任务
        finish()
    }

    override fun requestAgain(permissions: List<String>) {
        // 不要关心权限参数是什么，总是请求 ACCESS_BACKGROUND_LOCATION
        pb.requestAccessBackgroundLocationPermissionNow(this)
    }

    companion object {
        /**
         * 定义 const 以与低于 Q 的系统兼容
         */
        const val ACCESS_BACKGROUND_LOCATION = "android.permission.ACCESS_BACKGROUND_LOCATION"
    }
}