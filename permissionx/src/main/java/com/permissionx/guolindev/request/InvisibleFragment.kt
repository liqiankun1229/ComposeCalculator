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
import android.annotation.SuppressLint
import android.os.Build
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.permissionx.guolindev.PermissionX
import java.util.ArrayList

/**
 * 封装了请求权限的方法
 * 嵌入到用于处理权限请求的活动中的不可见片段
 * 这是非常轻量级的, 不会影响您的应用程序的效率
 */
class InvisibleFragment : Fragment() {

    private val handler = Handler(Looper.getMainLooper())

    /**
     * PermissionBuilder 实例
     */
    private lateinit var pb: PermissionBuilder

    /**
     * 当前任务(权限请求)的实例
     */
    private lateinit var task: ChainTask

    /**
     * 用于获取请求多个权限的结果
     */
    private val requestNormalPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantResults ->
            postForResult {
                onRequestNormalPermissionsResult(grantResults)
            }
        }

    /**
     * 用于获取后台定位 ACCESS_BACKGROUND_LOCATION 权限的结果
     */
    private val requestBackgroundLocationLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            postForResult {
                onRequestBackgroundLocationPermissionResult(granted)
            }
        }

    /**
     * 用于获取系统弹窗 SYSTEM_ALERT_WINDOW 权限的结果
     */
    private val requestSystemAlertWindowLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            postForResult {
                onRequestSystemAlertWindowPermissionResult()
            }
        }

    /**
     * 用于获取修改设置 WRITE_SETTINGS 权限的结果
     */
    private val requestWriteSettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            postForResult {
                onRequestWriteSettingsPermissionResult()
            }
        }

    /**
     * 用于获取管理外部存储 MANAGE_EXTERNAL_STORAGE 权限的结果
     */
    private val requestManageExternalStorageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            postForResult {
                onRequestManageExternalStoragePermissionResult()
            }
        }

    /**
     * 用于获取安装应用 REQUEST_INSTALL_PACKAGES 权限的结果
     */
    private val requestInstallPackagesLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            postForResult {
                onRequestInstallPackagesPermissionResult()
            }
        }

    /**
     * 用于获取通知权限的结果
     */
    private val requestNotificationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            postForResult {
                onRequestNotificationPermissionResult()
            }
        }

    /**
     * 用于获取身体传感器 BODY_SENSORS_BACKGROUND 权限的结果
     */
    private val requestBodySensorsBackgroundLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            postForResult {
                onRequestBodySensorsBackgroundPermissionResult(granted)
            }
        }

    /**
     * 用于在用户从设置切换回来时获取结果
     */
    private val forwardToSettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (checkForGC()) {
                task.requestAgain(ArrayList(pb.forwardPermissions))
            }
        }

    /**
     * 通过调用Fragment.requestPermissions一次请求权限
     * 并在 ActivityCompat.OnRequestPermissionsResultCallback 处理请求结果
     * @param permissionBuilder - PermissionBuilder 的实例
     * @param permissions - 您要请求的权限
     * @param chainTask - 当前任务的实例
     */
    fun requestNow(
        permissionBuilder: PermissionBuilder,
        permissions: Set<String>,
        chainTask: ChainTask
    ) {
        pb = permissionBuilder
        task = chainTask
        requestNormalPermissionLauncher.launch(permissions.toTypedArray())
    }

    /**
     * 通过调用 [Fragment.requestPermissions] 立即请求 ACCESS_BACKGROUND_LOCATION
     * 并在 ActivityCompat.OnRequestPermissionsResultCallback 中处理请求结果
     *
     * @param permissionBuilder PermissionBuilder 的实例
     * @param chainTask         当前任务的实例
     */
    fun requestAccessBackgroundLocationPermissionNow(
        permissionBuilder: PermissionBuilder,
        chainTask: ChainTask
    ) {
        pb = permissionBuilder
        task = chainTask
        requestBackgroundLocationLauncher.launch(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
    }

    /**
     * 请求系统弹窗(SYSTEM_ALERT_WINDOW) 权限
     * 在 Android M 及更高版本上 它是由带有 Settings.ACTION_MANAGE_OVERLAY_PERMISSION 的 Intent 请求的
     */
    fun requestSystemAlertWindowPermissionNow(
        permissionBuilder: PermissionBuilder,
        chainTask: ChainTask
    ) {
        pb = permissionBuilder
        task = chainTask
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(requireContext())) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:${requireActivity().packageName}")
            requestSystemAlertWindowLauncher.launch(intent)
        } else {
            onRequestSystemAlertWindowPermissionResult()
        }
    }

    /**
     * 请求修改设置(WRITE_SETTINGS) 权限
     * 在 Android M 及更高版本上 它是由带有 Settings.ACTION_MANAGE_WRITE_SETTINGS 的 Intent 请求的
     */
    fun requestWriteSettingsPermissionNow(
        permissionBuilder: PermissionBuilder,
        chainTask: ChainTask
    ) {
        pb = permissionBuilder
        task = chainTask
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(requireContext())) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:${requireActivity().packageName}")
            requestWriteSettingsLauncher.launch(intent)
        } else {
            onRequestWriteSettingsPermissionResult()
        }
    }

    /**
     * 请求管理外部存储(MANAGE_EXTERNAL_STORAGE) 权限
     * 在 Android R 及更高版本上 它是由带有 Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION 的 Intent 请求的
     */
    @SuppressLint("QueryPermissionsNeeded")
    fun requestManageExternalStoragePermissionNow(
        permissionBuilder: PermissionBuilder,
        chainTask: ChainTask
    ) {
        pb = permissionBuilder
        task = chainTask
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            var intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:${requireActivity().packageName}")
            if (intent.resolveActivity(requireActivity().packageManager) == null) {
                intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            }
            requestManageExternalStorageLauncher.launch(intent)
        } else {
            onRequestManageExternalStoragePermissionResult()
        }
    }

    /**
     * 请求安装应用(REQUEST_INSTALL_PACKAGES) 权限
     * 在 Android O 及更高版本上 它是由带有 Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES 的 Intent 请求的
     */
    fun requestInstallPackagesPermissionNow(
        permissionBuilder: PermissionBuilder,
        chainTask: ChainTask
    ) {
        pb = permissionBuilder
        task = chainTask
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            intent.data = Uri.parse("package:${requireActivity().packageName}")
            requestInstallPackagesLauncher.launch(intent)
        } else {
            onRequestInstallPackagesPermissionResult()
        }
    }

    /**
     * 请求通知权限
     * 在 Android O 及更高版本上 它是由带有 Settings.ACTION_APP_NOTIFICATION_SETTINGS 的 Intent 请求的
     */
    fun requestNotificationPermissionNow(
        permissionBuilder: PermissionBuilder,
        chainTask: ChainTask
    ) {
        pb = permissionBuilder
        task = chainTask
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireActivity().packageName)
            requestNotificationLauncher.launch(intent)
        } else {
            onRequestInstallPackagesPermissionResult()
        }
    }

    /**
     * 通过调用 [Fragment.requestPermissions] 立即请求 ACCESS_BACKGROUND_LOCATION
     * 并在 ActivityCompat.OnRequestPermissionsResultCallback 中处理请求结果
     *
     * @param permissionBuilder PermissionBuilder 的实例
     * @param chainTask         当前任务的实例
     */
    fun requestBodySensorsBackgroundPermissionNow(
        permissionBuilder: PermissionBuilder,
        chainTask: ChainTask
    ) {
        pb = permissionBuilder
        task = chainTask
        requestBodySensorsBackgroundLauncher.launch(RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND)
    }

    /**
     * 转到您应用的设置页面
     * 让用户打开必要的权限
     */
    fun forwardToSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        forwardToSettingsLauncher.launch(intent)
    }

    /**
     * 销毁 检查是否 GC 回收
     */
    override fun onDestroy() {
        super.onDestroy()
        if (checkForGC()) {
            // 当 Invisible Fragment 被回收时 关闭显示对话框 以避免窗口内存泄漏问题
            pb.currentDialog?.let {
                if (it.isShowing) {
                    it.dismiss()
                }
            }
        }
    }

    /**
     * 处理正常权限请求的结果
     */
    private fun onRequestNormalPermissionsResult(grantResults: Map<String, Boolean>) {
        if (checkForGC()) {
            // We can never holds granted permissions for safety, because user may turn some permissions off in settings.
            // So every time request, must request the already granted permissions again and refresh the granted permission set.
            pb.grantedPermissions.clear()
            val showReasonList: MutableList<String> =
                ArrayList() // holds denied permissions in the request permissions.
            val forwardList: MutableList<String> =
                ArrayList() // hold permanently denied permissions in the request permissions.
            for ((permission, granted) in grantResults) {
                if (granted) {
                    pb.grantedPermissions.add(permission)
                    // Remove granted permissions from deniedPermissions and permanentDeniedPermissions set in PermissionBuilder.
                    pb.deniedPermissions.remove(permission)
                    pb.permanentDeniedPermissions.remove(permission)
                } else {
                    // Denied permission can turn into permanent denied permissions, but permanent denied permission can not turn into denied permissions.
                    val shouldShowRationale = shouldShowRequestPermissionRationale(permission)
                    if (shouldShowRationale) {
                        showReasonList.add(permission)
                        pb.deniedPermissions.add(permission)
                        // So there's no need to remove the current permission from permanentDeniedPermissions because it won't be there.
                    } else {
                        forwardList.add(permission)
                        pb.permanentDeniedPermissions.add(permission)
                        // We must remove the current permission from deniedPermissions because it is permanent denied permission now.
                        pb.deniedPermissions.remove(permission)
                    }
                }
            }
            val deniedPermissions: MutableList<String> =
                ArrayList() // used to validate the deniedPermissions and permanentDeniedPermissions
            deniedPermissions.addAll(pb.deniedPermissions)
            deniedPermissions.addAll(pb.permanentDeniedPermissions)
            // maybe user can turn some permissions on in settings that we didn't request, so check the denied permissions again for safety.
            for (permission in deniedPermissions) {
                if (PermissionX.isGranted(requireContext(), permission)) {
                    pb.deniedPermissions.remove(permission)
                    pb.grantedPermissions.add(permission)
                }
            }
            val allGranted = pb.grantedPermissions.size == pb.normalPermissions.size
            if (allGranted) { // If all permissions are granted, finish current task directly.
                task.finish()
            } else {
                var shouldFinishTheTask = true // Indicate if we should finish the task
                // If explainReasonCallback is not null and there are denied permissions. Try the ExplainReasonCallback.
                if ((pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) && showReasonList.isNotEmpty()) {
                    shouldFinishTheTask =
                        false // shouldn't because ExplainReasonCallback handles it
                    if (pb.explainReasonCallbackWithBeforeParam != null) {
                        // callback ExplainReasonCallbackWithBeforeParam prior to ExplainReasonCallback
                        pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(
                            task.getExplainScope(), ArrayList(pb.deniedPermissions), false
                        )
                    } else {
                        pb.explainReasonCallback!!.onExplainReason(
                            task.getExplainScope(),
                            ArrayList(pb.deniedPermissions)
                        )
                    }
                    // store these permanently denied permissions or they will be lost when request again.
                    pb.tempPermanentDeniedPermissions.addAll(forwardList)
                } else if (pb.forwardToSettingsCallback != null && (forwardList.isNotEmpty() || pb.tempPermanentDeniedPermissions.isNotEmpty())) {
                    shouldFinishTheTask =
                        false // shouldn't because ForwardToSettingsCallback handles it
                    pb.tempPermanentDeniedPermissions.clear() // no need to store them anymore once onForwardToSettings callback.
                    pb.forwardToSettingsCallback!!.onForwardToSettings(
                        task.getForwardScope(),
                        ArrayList(pb.permanentDeniedPermissions)
                    )
                }
                // If showRequestReasonDialog or showForwardToSettingsDialog is not called. We should finish the task.
                // There's case that ExplainReasonCallback or ForwardToSettingsCallback is called, but developer didn't invoke
                // showRequestReasonDialog or showForwardToSettingsDialog in the callback.
                // At this case and all other cases, task should be finished.
                if (shouldFinishTheTask || !pb.showDialogCalled) {
                    task.finish()
                }
                // Reset this value after each request. If we don't do this, developer invoke showRequestReasonDialog in ExplainReasonCallback
                // but didn't invoke showForwardToSettingsDialog in ForwardToSettingsCallback, the request process will be lost. Because the
                // previous showDialogCalled affect the next request logic.
                pb.showDialogCalled = false
            }
        }
    }

    /**
     * 处理 后台定位(ACCESS_BACKGROUND_LOCATION) 权限请求的结果
     */
    private fun onRequestBackgroundLocationPermissionResult(granted: Boolean) {
        if (checkForGC()) {
            postForResult {
                if (granted) {
                    pb.grantedPermissions.add(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                    // Remove granted permissions from deniedPermissions and permanentDeniedPermissions set in PermissionBuilder.
                    pb.deniedPermissions.remove(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                    pb.permanentDeniedPermissions.remove(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                    task.finish()
                } else {
                    var goesToRequestCallback = true // Indicate if we should finish the task
                    val shouldShowRationale =
                        shouldShowRequestPermissionRationale(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                    // If explainReasonCallback is not null and we should show rationale. Try the ExplainReasonCallback.
                    if ((pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) && shouldShowRationale) {
                        goesToRequestCallback =
                            false // shouldn't because ExplainReasonCallback handles it
                        val permissionsToExplain: MutableList<String> = ArrayList()
                        permissionsToExplain.add(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                        if (pb.explainReasonCallbackWithBeforeParam != null) {
                            // callback ExplainReasonCallbackWithBeforeParam prior to ExplainReasonCallback
                            pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(
                                task.getExplainScope(), permissionsToExplain, false
                            )
                        } else {
                            pb.explainReasonCallback!!.onExplainReason(
                                task.getExplainScope(),
                                permissionsToExplain
                            )
                        }
                    } else if (pb.forwardToSettingsCallback != null && !shouldShowRationale) {
                        goesToRequestCallback =
                            false // shouldn't because ForwardToSettingsCallback handles it
                        val permissionsToForward: MutableList<String> = ArrayList()
                        permissionsToForward.add(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                        pb.forwardToSettingsCallback!!.onForwardToSettings(
                            task.getForwardScope(),
                            permissionsToForward
                        )
                    }
                    // If showRequestReasonDialog or showForwardToSettingsDialog is not called. We should finish the task.
                    // There's case that ExplainReasonCallback or ForwardToSettingsCallback is called, but developer didn't invoke
                    // showRequestReasonDialog or showForwardToSettingsDialog in the callback.
                    // At this case and all other cases, task should be finished.
                    if (goesToRequestCallback || !pb.showDialogCalled) {
                        task.finish()
                    }
                }
            }
        }
    }

    /**
     * 处理 系统弹窗(SYSTEM_ALERT_WINDOW) 权限请求的结果
     */
    private fun onRequestSystemAlertWindowPermissionResult() {
        if (checkForGC()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(requireContext())) {
                    task.finish()
                } else if (pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) {
                    if (pb.explainReasonCallbackWithBeforeParam != null) {
                        // callback ExplainReasonCallbackWithBeforeParam prior to ExplainReasonCallback
                        pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(
                            task.getExplainScope(),
                            listOf(Manifest.permission.SYSTEM_ALERT_WINDOW),
                            false
                        )
                    } else {
                        pb.explainReasonCallback!!.onExplainReason(
                            task.getExplainScope(), listOf(Manifest.permission.SYSTEM_ALERT_WINDOW)
                        )
                    }
                }
            } else {
                task.finish()
            }
        }
    }

    /**
     * 处理 WRITE_SETTINGS 权限请求的结果
     */
    private fun onRequestWriteSettingsPermissionResult() {
        if (checkForGC()) {
            postForResult {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.System.canWrite(requireContext())) {
                        task.finish()
                    } else if (pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) {
                        if (pb.explainReasonCallbackWithBeforeParam != null) {
                            // callback ExplainReasonCallbackWithBeforeParam prior to ExplainReasonCallback
                            pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(
                                task.getExplainScope(), listOf(Manifest.permission.WRITE_SETTINGS), false
                            )
                        } else {
                            pb.explainReasonCallback!!.onExplainReason(
                                task.getExplainScope(), listOf(Manifest.permission.WRITE_SETTINGS)
                            )
                        }
                    }
                } else {
                    task.finish()
                }
            }
        }
    }

    /**
     * Handle result of MANAGE_EXTERNAL_STORAGE permission request.
     */
    private fun onRequestManageExternalStoragePermissionResult() {
        if (checkForGC()) {
            postForResult {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        task.finish()
                    } else if (pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) {
                        if (pb.explainReasonCallbackWithBeforeParam != null) {
                            // callback ExplainReasonCallbackWithBeforeParam prior to ExplainReasonCallback
                            pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(
                                task.getExplainScope(),
                                listOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE),
                                false
                            )
                        } else {
                            pb.explainReasonCallback!!.onExplainReason(
                                task.getExplainScope(),
                                listOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                            )
                        }
                    }
                } else {
                    task.finish()
                }
            }
        }
    }

    /**
     * Handle result of REQUEST_INSTALL_PACKAGES permission request.
     */
    private fun onRequestInstallPackagesPermissionResult() {
        if (checkForGC()) {
            postForResult {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (requireActivity().packageManager.canRequestPackageInstalls()) {
                        task.finish()
                    } else if (pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) {
                        if (pb.explainReasonCallbackWithBeforeParam != null) {
                            // callback ExplainReasonCallbackWithBeforeParam prior to ExplainReasonCallback
                            pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(
                                task.getExplainScope(),
                                listOf(Manifest.permission.REQUEST_INSTALL_PACKAGES),
                                false
                            )
                        } else {
                            pb.explainReasonCallback!!.onExplainReason(
                                task.getExplainScope(),
                                listOf(Manifest.permission.REQUEST_INSTALL_PACKAGES)
                            )
                        }
                    }
                } else {
                    task.finish()
                }
            }
        }
    }

    /**
     * Handle result of notification permission request.
     */
    private fun onRequestNotificationPermissionResult() {
        if (checkForGC()) {
            postForResult {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (PermissionX.areNotificationsEnabled(requireContext())) {
                        task.finish()
                    } else if (pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) {
                        if (pb.explainReasonCallbackWithBeforeParam != null) {
                            // callback ExplainReasonCallbackWithBeforeParam prior to ExplainReasonCallback
                            pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(
                                task.getExplainScope(),
                                listOf(PermissionX.permission.POST_NOTIFICATIONS),
                                false
                            )
                        } else {
                            pb.explainReasonCallback!!.onExplainReason(
                                task.getExplainScope(),
                                listOf(PermissionX.permission.POST_NOTIFICATIONS)
                            )
                        }
                    }
                } else {
                    task.finish()
                }
            }
        }
    }

    /**
     * Handle result of BODY_SENSORS_BACKGROUND permission request.
     */
    private fun onRequestBodySensorsBackgroundPermissionResult(granted: Boolean) {
        if (checkForGC()) {
            postForResult {
                if (granted) {
                    pb.grantedPermissions.add(RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND)
                    // Remove granted permissions from deniedPermissions and permanentDeniedPermissions set in PermissionBuilder.
                    pb.deniedPermissions.remove(RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND)
                    pb.permanentDeniedPermissions.remove(RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND)
                    task.finish()
                } else {
                    var goesToRequestCallback = true // Indicate if we should finish the task
                    val shouldShowRationale =
                        shouldShowRequestPermissionRationale(RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND)
                    // If explainReasonCallback is not null and we should show rationale. Try the ExplainReasonCallback.
                    if ((pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) && shouldShowRationale) {
                        goesToRequestCallback =
                            false // shouldn't because ExplainReasonCallback handles it
                        val permissionsToExplain: MutableList<String> = ArrayList()
                        permissionsToExplain.add(RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND)
                        if (pb.explainReasonCallbackWithBeforeParam != null) {
                            // callback ExplainReasonCallbackWithBeforeParam prior to ExplainReasonCallback
                            pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(
                                task.getExplainScope(), permissionsToExplain, false
                            )
                        } else {
                            pb.explainReasonCallback!!.onExplainReason(
                                task.getExplainScope(),
                                permissionsToExplain
                            )
                        }
                    } else if (pb.forwardToSettingsCallback != null && !shouldShowRationale) {
                        goesToRequestCallback =
                            false // shouldn't because ForwardToSettingsCallback handles it
                        val permissionsToForward: MutableList<String> = ArrayList()
                        permissionsToForward.add(RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND)
                        pb.forwardToSettingsCallback!!.onForwardToSettings(
                            task.getForwardScope(),
                            permissionsToForward
                        )
                    }
                    // If showRequestReasonDialog or showForwardToSettingsDialog is not called. We should finish the task.
                    // There's case that ExplainReasonCallback or ForwardToSettingsCallback is called, but developer didn't invoke
                    // showRequestReasonDialog or showForwardToSettingsDialog in the callback.
                    // At this case and all other cases, task should be finished.
                    if (goesToRequestCallback || !pb.showDialogCalled) {
                        task.finish()
                    }
                }
            }
        }
    }

    /**
     * 在某些手机上，PermissionBuilder 和 ChainTask 在 GC 等不可预知的情况下可能会变为 null
     * 此时它们不应为空，因此在这种情况下我们无能为力
     * @return PermissionBuilder 和 ChainTask 是否仍然存在。如果不是，我们不应该做任何进一步的逻辑。
     */
    private fun checkForGC(): Boolean {
        if (!::pb.isInitialized || !::task.isInitialized) {
            Log.w(
                "PermissionX",
                "PermissionBuilder and ChainTask should not be null at this time, so we can do nothing in this case."
            )
            return false
        }
        return true
    }

    /**
     * Post to continue the further request callback for safe, in case some edge case crashes.
     */
    private fun postForResult(callback: () -> Unit) {
        handler.post {
            callback()
        }
    }
}