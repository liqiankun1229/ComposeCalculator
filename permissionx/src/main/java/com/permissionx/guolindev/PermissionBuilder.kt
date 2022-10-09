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
package com.permissionx.guolindev

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam
import com.permissionx.guolindev.callback.ForwardToSettingsCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.permissionx.guolindev.chain.RequestChain
import com.permissionx.guolindev.dialog.DefaultDialog
import com.permissionx.guolindev.dialog.RationaleDialog
import com.permissionx.guolindev.dialog.RationaleDialogFragment
import com.permissionx.guolindev.fragment.InvisibleFragment
import com.permissionx.guolindev.request.*
import com.permissionx.guolindev.request.RequestBackgroundLocationPermission
import com.permissionx.guolindev.request.RequestBodySensorsBackgroundPermission
import com.permissionx.guolindev.request.RequestInstallPackagesPermission
import com.permissionx.guolindev.request.RequestManageExternalStoragePermission
import com.permissionx.guolindev.request.RequestNormalPermissions
import com.permissionx.guolindev.request.RequestNotificationPermission
import com.permissionx.guolindev.request.RequestSystemAlertWindowPermission
import com.permissionx.guolindev.request.RequestWriteSettingsPermission
import com.permissionx.guolindev.task.ChainTask
import java.util.*

/**
 * 更多 API 供开发人员控制 PermissionX 功能
 */
class PermissionBuilder(
    fragmentActivity: FragmentActivity?,
    fragment: Fragment?,
    normalPermissions: MutableSet<String>,
    specialPermissions: MutableSet<String>
) {

    /**
     * 初始化 Activity 的实例
     */
    lateinit var activity: FragmentActivity

    /**
     * 一切的片段实例作为活动(Activity)的替代选择
     * fragment 实例
     */
    private var fragment: Fragment? = null

    /**
     * 在 Light 主题中的 DefaultDialog 上设置的自定义色调颜色
     */
    private var lightColor = -1

    /**
     * 在深色主题中的 DefaultDialog 上设置的自定义色调颜色
     */
    private var darkColor = -1

    /**
     * 当前 Activity 的源请求方向 我们需要在权限请求完成后恢复它
     */
    private var originRequestOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    /**
     * 如果在 Activity 中 则获取 FragmentManager
     * 如果在 Fragment 中 则获取 ChildFragmentManager
     * @return FragmentManager 操作Fragment
     */
    private val fragmentManager: FragmentManager
        get() {
            return fragment?.childFragmentManager ?: activity.supportFragmentManager
        }

    /**
     * 获取请求权限的活动中的不可见片段
     * 如果没有不可见的片段,则在活动中添加一个
     * 不用担心,这是非常轻量级的
     */
    private val invisibleFragment: InvisibleFragment
        get() {
            val existedFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG)
            return if (existedFragment != null) {
                existedFragment as InvisibleFragment
            } else {
                val invisibleFragment = InvisibleFragment()
                fragmentManager.beginTransaction()
                    .add(invisibleFragment, FRAGMENT_TAG)
                    .commitNowAllowingStateLoss()
                invisibleFragment
            }
        }

    /**
     * 向用户显示的当前对话框的实例
     * 当 InvisibleFragment 被销毁时, 我们需要关闭此对话框
     */
    @JvmField
    var currentDialog: Dialog? = null

    /**
     * 应用想要请求的正常运行时权限
     */
    @JvmField
    var normalPermissions: MutableSet<String>

    /**
     * 我们需要通过特殊情况处理的特殊权限
     * 如 SYSTEM_ALERT_WINDOW / WRITE_SETTINGS / MANAGE_EXTERNAL_STORAGE
     */
    @JvmField
    var specialPermissions: MutableSet<String>

    /**
     * 表示 PermissionX 应在请求之前说明请求原因
     */
    @JvmField
    var explainReasonBeforeRequest = false

    /**
     * 表示
     * [ExplainScope.showRequestReasonDialog]
     * 或
     * [ForwardScope.showForwardToSettingsDialog]
     * 在 [.onExplainRequestReason] 或 [.onForwardToSettings] 回调中调用
     * 如果未调用, 则 PermissionX 会自动调用 requestCallback
     */
    @JvmField
    var showDialogCalled = false

    /**
     * 一些不应该请求的权限将存储在这里
     * 并在请求完成时通知用户
     */
    @JvmField
    var permissionsWontRequest: MutableSet<String> = LinkedHashSet()

    /**
     * 持有已在请求的权限中授予的权限
     */
    @JvmField
    var grantedPermissions: MutableSet<String> = LinkedHashSet()

    /**
     * 持有在请求的权限中被拒绝的权限
     */
    @JvmField
    var deniedPermissions: MutableSet<String> = LinkedHashSet()

    /**
     * 持有在请求的权限中被永久拒绝的权限。（拒绝，不再询问）
     */
    @JvmField
    var permanentDeniedPermissions: MutableSet<String> = LinkedHashSet()

    /**
     * 当我们请求多个权限时, 有些被拒绝，有些被永久拒绝
     * 拒绝的权限将首先回调, 并且永久拒绝的权限将存储在这个 [tempPermanentDeniedPermissions] 中
     * 一旦不再存在被拒绝的权限, 它们将被回调
     */
    @JvmField
    var tempPermanentDeniedPermissions: MutableSet<String> = LinkedHashSet()

    /**
     * 持有应该转发到设置以允许它们的权限
     * 并非所有永久拒绝的权限都应转发到“设置”
     * 只有那些开发人员认为他们是必要的才应该
     */
    @JvmField
    var forwardPermissions: MutableSet<String> = LinkedHashSet()

    /**
     * [request] 方法的回调 不能为空
     */
    @JvmField
    var requestCallback: RequestCallback? = null

    /**
     * [onExplainRequestReason] 方法的回调 也许是空的
     */
    @JvmField
    var explainReasonCallback: ExplainReasonCallback? = null

    /**
     * [onExplainRequestReason] 方法的回调 但带有 beforeRequest 参数 也许是空的
     */
    @JvmField
    var explainReasonCallbackWithBeforeParam: ExplainReasonCallbackWithBeforeParam? = null

    /**
     * [onForwardToSettings] 方法的回调 也许是空的
     */
    @JvmField
    var forwardToSettingsCallback: ForwardToSettingsCallback? = null

    /**
     * 获取当前应用的 targetSdkVersion
     *
     * @return 当前应用的 targetSdkVersion
     */
    val targetSdkVersion: Int
        get() = activity.applicationInfo.targetSdkVersion

    /**
     * 当权限需要解释请求原因时调用
     * 通常, 每次用户拒绝您的请求时都会调用此方法
     * 如果您链接[explainReasonBeforeRequest], 此方法可能会在权限请求之前运行
     * @param callback - 用户拒绝权限的回调
     * @return PermissionBuilder 本身
     */
    fun onExplainRequestReason(callback: ExplainReasonCallback): PermissionBuilder {
        explainReasonCallback = callback
        return this
    }

    /**
     * 当权限需要解释请求原因时调用
     * 通常，每次用户拒绝您的请求时都会调用此方法
     * 如果您链接 [explainReasonBeforeRequest] 此方法可能会在权限请求之前运行
     * [beforeRequest] 参数会告诉您此方法当前是在权限请求之前或之后
     *
     * @param callback - 用户拒绝权限的回调
     * @return PermissionBuilder 本身
     */
    fun onExplainRequestReason(callback: ExplainReasonCallbackWithBeforeParam?): PermissionBuilder {
        explainReasonCallbackWithBeforeParam = callback
        return this
    }

    /**
     * 当权限需要转发到设置以允许时调用
     * 通常，用户拒绝您的请求并选中“不再询问”会调用此方法
     * 记住 [onExplainRequestReason] 总是在这个方法之前
     * 如果调用 [onExplainRequestReason] 则不会在同一请求时间内调用该方法
     *
     * @param callback 权限被拒绝和检查的回调永远不会被用户再次询问
     * @return PermissionBuilder 本身
     */
    fun onForwardToSettings(callback: ForwardToSettingsCallback?): PermissionBuilder {
        forwardToSettingsCallback = callback
        return this
    }

    /**
     * 如果您需要显示请求许可理由 请在您的请求语法中链接此方法
     * [onExplainRequestReason] 将在权限请求之前调用
     *
     * @return PermissionBuilder 本身
     */
    fun explainReasonBeforeRequest(): PermissionBuilder {
        explainReasonBeforeRequest = true
        return this
    }

    /**
     * 将色调颜色设置为默认的基本原理对话框。
     * @param lightColor 用于浅色主题 0xAARRGGBB 形式的颜色值 不要传递资源 ID, 要从资源 ID 中获取颜色值 请调用 getColor
     * @param darkColor  用于深色主题 0xAARRGGBB 形式的颜色值 不要传递资源 ID, 要从资源 ID 中获取颜色值 请调用 getColor
     * @return PermissionBuilder 本身
     */
    fun setDialogTintColor(lightColor: Int, darkColor: Int): PermissionBuilder {
        this.lightColor = lightColor
        this.darkColor = darkColor
        return this
    }

    /**
     * 一次请求权限，并在回调中处理请求结果。
     * @return callback - 带有 3 个参数的回调。 allGranted、grantedList、deniedList。
     */
    fun request(callback: RequestCallback?) {
        requestCallback = callback
        startRequest()
    }

    /**
     * 此方法是内部的，不应由开发人员调用
     * 向用户显示一个对话框并解释为什么需要这些权限
     *
     * @param chainTask              当前任务的实例
     * @param showReasonOrGoSettings 表示应该显示解释原因或转发到设置
     * @param permissions            再次请求的权限
     * @param message                向用户解释为什么需要这些权限的消息
     * @param positiveText           正面按钮上的正面文本以再次请求
     * @param negativeText           否定按钮上的否定文本。如果不应取消此对话框，则可能为 null
     */
    fun showHandlePermissionDialog(
        chainTask: ChainTask,
        showReasonOrGoSettings: Boolean,
        permissions: List<String>,
        message: String,
        positiveText: String,
        negativeText: String?
    ) {
        val defaultDialog = DefaultDialog(
            activity,
            permissions,
            message,
            positiveText,
            negativeText,
            lightColor,
            darkColor
        )
        showHandlePermissionDialog(chainTask, showReasonOrGoSettings, defaultDialog)
    }

    /**
     * 此方法是内部的，不应由开发人员调用
     * 向用户显示一个对话框并解释为什么需要这些权限
     * @param chainTask - 当前任务的实例
     * @param showReasonOrGoSettings - 表示应该显示解释原因或转发到设置
     * @param dialog - 向用户解释为什么需要这些权限的对话框
     */
    fun showHandlePermissionDialog(
        chainTask: ChainTask,
        showReasonOrGoSettings: Boolean,
        dialog: RationaleDialog
    ) {
        showDialogCalled = true
        val permissions = dialog.getPermissionsToRequest()
        if (permissions.isEmpty()) {
            chainTask.finish()
            return
        }
        currentDialog = dialog
        dialog.show()
        if (dialog is DefaultDialog && dialog.isPermissionLayoutEmpty()) {
            // 没有在对话框上显示的有效权限 我们改为调用dismiss
            dialog.dismiss()
            chainTask.finish()
        }
        val positiveButton = dialog.getPositiveButton()
        val negativeButton = dialog.getNegativeButton()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        positiveButton.isClickable = true
        positiveButton.setOnClickListener {
            dialog.dismiss()
            if (showReasonOrGoSettings) {
                chainTask.requestAgain(permissions)
            } else {
                forwardToSettings(permissions)
            }
        }
        if (negativeButton != null) {
            negativeButton.isClickable = true
            negativeButton.setOnClickListener {
                dialog.dismiss()
                chainTask.finish()
            }
        }
        // 销毁
        currentDialog?.setOnDismissListener {
            currentDialog = null
        }
    }

    /**
     * 向用户显示 DialogFragment 并解释为什么需要这些权限
     *
     * @param chainTask              Instance of current task.
     * @param showReasonOrGoSettings Indicates should show explain reason or forward to Settings.
     * @param dialogFragment         DialogFragment to explain to user why these permissions are necessary.
     */
    fun showHandlePermissionDialog(
        chainTask: ChainTask,
        showReasonOrGoSettings: Boolean,
        dialogFragment: RationaleDialogFragment
    ) {
        showDialogCalled = true
        val permissions = dialogFragment.permissionsToRequest
        if (permissions.isEmpty()) {
            chainTask.finish()
            return
        }
        dialogFragment.showNow(fragmentManager, "PermissionXRationaleDialogFragment")
        val positiveButton = dialogFragment.positiveButton
        val negativeButton = dialogFragment.negativeButton
        dialogFragment.isCancelable = false
        positiveButton.isClickable = true
        positiveButton.setOnClickListener {
            dialogFragment.dismiss()
            if (showReasonOrGoSettings) {
                chainTask.requestAgain(permissions)
            } else {
                forwardToSettings(permissions)
            }
        }
        if (negativeButton != null) {
            negativeButton.isClickable = true
            negativeButton.setOnClickListener(View.OnClickListener {
                dialogFragment.dismiss()
                chainTask.finish()
            })
        }
    }

    /**
     * 在片段中一次请求权限
     *
     * @param permissions 您要请求的权限
     * @param chainTask   当前任务的实例
     */
    fun requestNow(permissions: Set<String>, chainTask: ChainTask) {
        invisibleFragment.requestNow(this, permissions, chainTask)
    }

    /**
     * 在 Fragment 中立即请求 ACCESS_BACKGROUND_LOCATION 权限
     *
     * @param chainTask 当前任务的实例
     */
    fun requestAccessBackgroundLocationPermissionNow(chainTask: ChainTask) {
        invisibleFragment.requestAccessBackgroundLocationPermissionNow(this, chainTask)
    }

    /**
     * 在 Fragment 中立即请求 SYSTEM_ALERT_WINDOW 权限
     *
     * @param chainTask 当前任务的实例
     */
    fun requestSystemAlertWindowPermissionNow(chainTask: ChainTask) {
        invisibleFragment.requestSystemAlertWindowPermissionNow(this, chainTask)
    }

    /**
     * 在 Fragment 中立即请求 WRITE_SETTINGS 权限
     *
     * @param chainTask 当前任务的实例
     */
    fun requestWriteSettingsPermissionNow(chainTask: ChainTask) {
        invisibleFragment.requestWriteSettingsPermissionNow(this, chainTask)
    }

    /**
     * 在 Fragment 中立即请求 MANAGE_EXTERNAL_STORAGE 权限
     *
     * @param chainTask 当前任务的实例
     */
    fun requestManageExternalStoragePermissionNow(chainTask: ChainTask) {
        invisibleFragment.requestManageExternalStoragePermissionNow(this, chainTask)
    }

    /**
     * 在片段中立即请求 REQUEST_INSTALL_PACKAGES 权限
     *
     * @param chainTask 当前任务的实例
     */
    fun requestInstallPackagePermissionNow(chainTask: ChainTask) {
        invisibleFragment.requestInstallPackagesPermissionNow(this, chainTask)
    }

    /**
     * 在片段中立即请求通知权限
     *
     * @param chainTask 当前任务的实例
     */
    fun requestNotificationPermissionNow(chainTask: ChainTask) {
        invisibleFragment.requestNotificationPermissionNow(this, chainTask)
    }

    /**
     * 在 Fragment 中立即请求 BODY_SENSORS_BACKGROUND 权限
     *
     * @param chainTask 当前任务的实例
     */
    fun requestBodySensorsBackgroundPermissionNow(chainTask: ChainTask) {
        invisibleFragment.requestBodySensorsBackgroundPermissionNow(this, chainTask)
    }

    /**
     * 我们是否应该请求 ACCESS_BACKGROUND_LOCATION 权限
     *
     * @return 如果 specialPermissions 包含 ACCESS_BACKGROUND_LOCATION 权限则为真 否则为假
     */
    fun shouldRequestBackgroundLocationPermission(): Boolean {
        return specialPermissions.contains(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
    }

    /**
     * 我们是否应该请求 SYSTEM_ALERT_WINDOW 权限
     *
     * @return 如果 specialPermissions 包含 SYSTEM_ALERT_WINDOW 权限 则为真 否则为假
     */
    fun shouldRequestSystemAlertWindowPermission(): Boolean {
        return specialPermissions.contains(Manifest.permission.SYSTEM_ALERT_WINDOW)
    }

    /**
     * 我们是否应该请求 WRITE_SETTINGS 权限
     *
     * @return 如果 specialPermissions 包含 WRITE_SETTINGS 权限 则为真 否则为假
     */
    fun shouldRequestWriteSettingsPermission(): Boolean {
        return specialPermissions.contains(Manifest.permission.WRITE_SETTINGS)
    }

    /**
     * 我们是否应该请求 MANAGE_EXTERNAL_STORAGE 权限
     *
     * @return 如果 specialPermissions 包含 MANAGE_EXTERNAL_STORAGE 权限 则为真 否则为假
     */
    fun shouldRequestManageExternalStoragePermission(): Boolean {
        return specialPermissions.contains(RequestManageExternalStoragePermission.MANAGE_EXTERNAL_STORAGE)
    }

    /**
     * 我们是否应该请求 REQUEST_INSTALL_PACKAGES 权限
     *
     * @return 如果 specialPermissions 包含 REQUEST_INSTALL_PACKAGES 权限 则为真 否则为假
     */
    fun shouldRequestInstallPackagesPermission(): Boolean {
        return specialPermissions.contains(RequestInstallPackagesPermission.REQUEST_INSTALL_PACKAGES)
    }

    /**
     * 我们是否应该请求特定的特别许可
     *
     * @return 如果 specialPermissions 包含 POST_NOTIFICATIONS 权限则为真 否则为假
     */
    fun shouldRequestNotificationPermission(): Boolean {
        return specialPermissions.contains(PermissionX.permission.POST_NOTIFICATIONS)
    }

    /**
     * 我们是否应该请求特定的特别许可
     *
     * @return 如果 specialPermissions 包含 BODY_SENSORS_BACKGROUND 权限 则为真 否则为假
     */
    fun shouldRequestBodySensorsBackgroundPermission(): Boolean {
        return specialPermissions.contains(RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND)
    }

    private fun startRequest() {
        // 请求权限时锁定方向 否则可能由于活动被破坏而错过回调
        lockOrientation()

        // 构建请求链
        // RequestNormalPermissions 首先运行
        // 然后 Request Background Location Permission 运行
        val requestChain = RequestChain()
        requestChain.addTaskToChain(RequestNormalPermissions(this))
        requestChain.addTaskToChain(RequestBackgroundLocationPermission(this))
        requestChain.addTaskToChain(RequestSystemAlertWindowPermission(this))
        requestChain.addTaskToChain(RequestWriteSettingsPermission(this))
        requestChain.addTaskToChain(RequestManageExternalStoragePermission(this))
        requestChain.addTaskToChain(RequestInstallPackagesPermission(this))
        requestChain.addTaskToChain(RequestNotificationPermission(this))
        requestChain.addTaskToChain(RequestBodySensorsBackgroundPermission(this))
        requestChain.runTask()
    }

    /**
     * 从当前 FragmentManager 中移除 InvisibleFragment
     */
    private fun removeInvisibleFragment() {
        val existedFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG)
        if (existedFragment != null) {
            fragmentManager.beginTransaction().remove(existedFragment).commitNowAllowingStateLoss()
        }
    }

    /**
     * 恢复屏幕方向
     * 活动只是像以前一样被锁定
     * Android O 存在只有全屏 Activity 才能请求方向的 bug 所以我们需要排除 Android O
     */
    private fun restoreOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            activity.requestedOrientation = originRequestOrientation
        }
    }

    /**
     * 锁定屏幕方向
     * 活动无法随传感器旋转
     * Android O 存在只有全屏 Activity 才能请求方向的 bug 所以我们需要排除 Android O
     */
    @SuppressLint("SourceLockedOrientationActivity")
    private fun lockOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            originRequestOrientation = activity.requestedOrientation
            val orientation = activity.resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            }
        }
    }

    /**
     * 转到您应用的设置页面 让用户打开必要的权限
     *
     * @param permissions 必要的权限
     */
    private fun forwardToSettings(permissions: List<String>) {
        forwardPermissions.clear()
        forwardPermissions.addAll(permissions)
        invisibleFragment.forwardToSettings()
    }

    internal fun endRequest() {
        // 请求完成后从当前 Activity 中移除 Invisible Fragment
        removeInvisibleFragment()
        // 请求完成后恢复方向 因为它之前被锁定
        restoreOrientation()
    }

    companion object {
        /**
         * 要查找和创建的 InvisibleFragment 的 TAG
         */
        private const val FRAGMENT_TAG = "InvisibleFragment"
    }

    init {
        if (fragmentActivity != null) {
            activity = fragmentActivity
        }
        // 活动和片段不能同时为空
        if (fragmentActivity == null && fragment != null) {
            activity = fragment.requireActivity()
        }
        // 同时为空的时候抛出异常
        if (activity == null && fragment == null) {
            throw IllegalAccessException()
        }
        this.fragment = fragment
        this.normalPermissions = normalPermissions
        this.specialPermissions = specialPermissions
    }
}