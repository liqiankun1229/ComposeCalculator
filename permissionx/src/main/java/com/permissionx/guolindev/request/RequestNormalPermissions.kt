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
import java.util.*

/**
 * 请求普通权限的任务
 */
internal class RequestNormalPermissions internal constructor(permissionBuilder: PermissionBuilder) :
    BaseTask(permissionBuilder) {

    override fun request() {
        val requestList = ArrayList<String>()
        for (permission in pb.normalPermissions) {
            if (PermissionX.isGranted(pb.activity, permission)) {
                pb.grantedPermissions.add(permission) // 已经授予
            } else {
                requestList.add(permission) // 仍然需要请求
            }
        }
        if (requestList.isEmpty()) { // 授予所有权限
            finish()
            return
        }
        if (pb.explainReasonBeforeRequest && (pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null)) {
            pb.explainReasonBeforeRequest = false
            pb.deniedPermissions.addAll(requestList)
            if (pb.explainReasonCallbackWithBeforeParam != null) {
                // 在 ExplainReasonCallback 之前回调 ExplainReasonCallbackWithBeforeParam
                pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(getExplainScope(), requestList, true)
            } else {
                pb.explainReasonCallback!!.onExplainReason(getExplainScope(), requestList)
            }
        } else {
            // 立即执行请求
            // 无论是否已授予所有权限 请始终请求所有权限 以防用户在“设置”中将其关闭
            pb.requestNow(pb.normalPermissions, this)
        }
    }

    /**
     * 如果权限被用户拒绝 并调用了[ExplainScope.showRequestReasonDialog]或[ForwardScope.showForwardToSettingsDialog]
     * 当用户单击肯定按钮时 将调用此方法
     * @param permissions   再次请求的权限
     */
    override fun requestAgain(permissions: List<String>) {
        val permissionsToRequestAgain: MutableSet<String> = HashSet(pb.grantedPermissions)
        permissionsToRequestAgain.addAll(permissions)
        if (permissionsToRequestAgain.isNotEmpty()) {
            pb.requestNow(permissionsToRequestAgain, this)
        } else {
            finish()
        }
    }
}