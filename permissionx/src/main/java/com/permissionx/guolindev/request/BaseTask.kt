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
import android.os.Environment
import android.provider.Settings
import com.permissionx.guolindev.PermissionX
import java.util.*

/**
 * 定义一个 BaseTask 来实现重复的逻辑代码
 * 无需在每项任务中实施它们
 */
internal abstract class BaseTask(@JvmField var pb: PermissionBuilder) : ChainTask {
    /**
     * 指向下一个任务 此任务完成后将运行下一个任务
     * 如果没有下一个任务 则请求过程结束
     */
    @JvmField
    var next: ChainTask? = null

    /**
     * 为要调用的特定函数提供 explainReasonCallback 的特定范围
     */
    private var explainReasonScope = ExplainScope(pb, this)

    /**
     * 为要调用的特定函数提供 forwardToSettingsCallback 的特定范围
     */
    private var forwardToSettingsScope = ForwardScope(pb, this)

    override fun getExplainScope() = explainReasonScope

    override fun getForwardScope() = forwardToSettingsScope

    /**
     * 结束任务
     */
    override fun finish() {
        // 如果有下一个任务 则运行它
        next?.request() ?: run {
            // 如果没有下一个任务 结束请求过程并通知结果
            val deniedList: MutableList<String> = ArrayList()
            deniedList.addAll(pb.deniedPermissions)
            deniedList.addAll(pb.permanentDeniedPermissions)
            deniedList.addAll(pb.permissionsWontRequest)
            if (pb.shouldRequestBackgroundLocationPermission()) {
                if (PermissionX.isGranted(pb.activity, RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)) {
                    pb.grantedPermissions.add(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                } else {
                    deniedList.add(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                }
            }
            if (pb.shouldRequestSystemAlertWindowPermission()
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && pb.targetSdkVersion >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(pb.activity)) {
                    pb.grantedPermissions.add(Manifest.permission.SYSTEM_ALERT_WINDOW)
                } else {
                    deniedList.add(Manifest.permission.SYSTEM_ALERT_WINDOW)
                }
            }
            if (pb.shouldRequestWriteSettingsPermission()
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && pb.targetSdkVersion >= Build.VERSION_CODES.M) {
                if (Settings.System.canWrite(pb.activity)) {
                    pb.grantedPermissions.add(Manifest.permission.WRITE_SETTINGS)
                } else {
                    deniedList.add(Manifest.permission.WRITE_SETTINGS)
                }
            }
            if (pb.shouldRequestManageExternalStoragePermission()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                    Environment.isExternalStorageManager()) {
                    pb.grantedPermissions.add(RequestManageExternalStoragePermission.MANAGE_EXTERNAL_STORAGE)
                } else {
                    deniedList.add(RequestManageExternalStoragePermission.MANAGE_EXTERNAL_STORAGE)
                }
            }
            if (pb.shouldRequestInstallPackagesPermission()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && pb.targetSdkVersion >= Build.VERSION_CODES.O) {
                    if (pb.activity.packageManager.canRequestPackageInstalls()) {
                        pb.grantedPermissions.add(RequestInstallPackagesPermission.REQUEST_INSTALL_PACKAGES)
                    } else {
                        deniedList.add(RequestInstallPackagesPermission.REQUEST_INSTALL_PACKAGES)
                    }
                } else {
                    deniedList.add(RequestInstallPackagesPermission.REQUEST_INSTALL_PACKAGES)
                }
            }
            if (pb.shouldRequestNotificationPermission()) {
                if (PermissionX.areNotificationsEnabled(pb.activity)) {
                    pb.grantedPermissions.add(PermissionX.permission.POST_NOTIFICATIONS)
                } else {
                    deniedList.add(PermissionX.permission.POST_NOTIFICATIONS)
                }
            }
            if (pb.shouldRequestBodySensorsBackgroundPermission()) {
                if (PermissionX.isGranted(pb.activity, RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND)) {
                    pb.grantedPermissions.add(RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND)
                } else {
                    deniedList.add(RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND)
                }
            }
            if (pb.requestCallback != null) {
                pb.requestCallback!!.onResult(deniedList.isEmpty(), ArrayList(pb.grantedPermissions), deniedList)
            }

            pb.endRequest()
        }
    }

    init {
        explainReasonScope = ExplainScope(pb, this)
        forwardToSettingsScope = ForwardScope(pb, this)
    }
}