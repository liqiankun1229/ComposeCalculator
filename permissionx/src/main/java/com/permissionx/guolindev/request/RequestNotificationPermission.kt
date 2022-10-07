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

import com.permissionx.guolindev.PermissionX

/**
 * 请求通知权限 33 新增动态权限
 * Implementation for request notification permission below Android T.
 */
internal class RequestNotificationPermission internal constructor(permissionBuilder: PermissionBuilder)
    : BaseTask(permissionBuilder) {

    override fun request() {
        if (pb.shouldRequestNotificationPermission()) {
            if (PermissionX.areNotificationsEnabled(pb.activity)) {
                // 通知权限已经授予 我们现在可以完成这个任务了
                finish()
                return
            }
            if (pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) {
                val requestList = mutableListOf(PermissionX.permission.POST_NOTIFICATIONS)
                if (pb.explainReasonCallbackWithBeforeParam != null) {
                    // 在 ExplainReasonCallback 之前回调 ExplainReasonCallbackWithBeforeParam
                    pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(getExplainScope(), requestList, true)
                } else {
                    pb.explainReasonCallback!!.onExplainReason(getExplainScope(), requestList)
                }
                return
            }
        }
        // 此时不应请求通知 因此我们调用 finish() 来结束此任务
        finish()
    }

    override fun requestAgain(permissions: List<String>) {
        // 不要关心权限参数是什么 总是请求通知权限
        pb.requestNotificationPermissionNow(this)
    }
}