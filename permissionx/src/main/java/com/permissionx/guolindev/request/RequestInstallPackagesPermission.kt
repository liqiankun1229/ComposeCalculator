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

/**
 * 请求安装应用的权限 android.permission.REQUEST_INSTALL_PACKAGES
 * 8.0 后新增权限
 */
internal class RequestInstallPackagesPermission internal constructor(permissionBuilder: PermissionBuilder) :
    BaseTask(permissionBuilder) {

    override fun request() {
        if (pb.shouldRequestInstallPackagesPermission()
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            && pb.targetSdkVersion >= Build.VERSION_CODES.O) {
            if (pb.activity.packageManager.canRequestPackageInstalls()) {
                // REQUEST_INSTALL_PACKAGES 权限已经请求了 直接执行任务
                finish()
                return
            }
            if (pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) {
                val requestList = mutableListOf(Manifest.permission.REQUEST_INSTALL_PACKAGES)
                if (pb.explainReasonCallbackWithBeforeParam != null) {
                    // 在 ExplainReasonCallback 之前回调 ExplainReasonCallbackWithBeforeParam
                    pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(getExplainScope(), requestList, true)
                } else {
                    pb.explainReasonCallback!!.onExplainReason(getExplainScope(), requestList)
                }
            } else {
                // 没有执行explainReasonCallback
                // 此时我们不能请求 REQUEST_INSTALL_PACKAGES 权限 因为用户不明白为什么
                finish()
            }
        } else {
            // 此时不应请求 REQUEST_INSTALL_PACKAGES 权限
            // 因此我们调用 finish() 来结束此任务
            finish()
        }
    }

    override fun requestAgain(permissions: List<String>) {
        // 不在乎权限参数是什么 始终请求 REQUEST_INSTALL_PACKAGES 权限
        pb.requestInstallPackagePermissionNow(this)
    }

    companion object {
        /**
         * 定义 const 以与低于 M 的系统兼容
         */
        const val REQUEST_INSTALL_PACKAGES = "android.permission.REQUEST_INSTALL_PACKAGES"
    }
}