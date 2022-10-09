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

package com.permissionx.guolindev.dialog

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.R
import com.permissionx.guolindev.databinding.PermissionxDefaultDialogLayoutBinding
import com.permissionx.guolindev.databinding.PermissionxPermissionItemBinding

/**
 * 默认基本原理对话框显示开发人员是否没有实现他们自己的自定义基本原理对话框
 */
class DefaultDialog(context: Context,
    private val permissions: List<String>,
    private val message: String,
    private val positiveText: String,
    private val negativeText: String?,
    private val lightColor: Int,
    private val darkColor: Int
) : RationaleDialog(context, R.style.PermissionXDefaultDialog) {

    private lateinit var binding: PermissionxDefaultDialogLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PermissionxDefaultDialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupText()
        buildPermissionsLayout()
        setupWindow()
    }

    /**
     * 提供肯定按钮实例以继续请求
     * @return 继续请求的正按钮实例
     */
    override fun getPositiveButton(): View {
        return binding.positiveBtn
    }

    /**
     * 提供否定按钮实例以中止请求
     * 这是另一种选择 如果 negativeText 为null 我们只返回 null，意味着所有这些权限都是必需的
     * @return 用于中止请求的否定按钮实例 如果所有这些权限都是必需的 则为 null
     */
    override fun getNegativeButton(): View? {
        return negativeText?.let {
            return binding.negativeBtn
        }
    }

    /**
     * 提供再次请求的权限
     * @return 再次请求的权限
     */
    override fun getPermissionsToRequest(): List<String> {
        return permissions
    }

    /**
     * 检查权限布局是否为空
     * 如果传入的所有权限都是无效权限 例如名为“hello world”的字符串
     * 则有可能 我们不会将这些添加到权限布局中
     */
    internal fun isPermissionLayoutEmpty(): Boolean {
        return binding.permissionsLayout.childCount == 0
    }

    /**
     * 在对话框上设置文本和文本颜色
     */
    private fun setupText() {
        binding.messageText.text = message
        binding.positiveBtn.text = positiveText
        if (negativeText != null) {
            binding.negativeLayout.visibility = View.VISIBLE
            binding.negativeBtn.text = negativeText
        } else {
            binding.negativeLayout.visibility = View.GONE
        }
        if (isDarkTheme()) {
            if (darkColor != -1) {
                binding.positiveBtn.setTextColor(darkColor)
                binding.negativeBtn.setTextColor(darkColor)
            }
        } else {
            if (lightColor != -1) {
                binding.positiveBtn.setTextColor(lightColor)
                binding.negativeBtn.setTextColor(lightColor)
            }
        }
    }

    /**
     * 将需要解释请求原因的每个权限添加到对话框中
     * 但是我们只需要添加权限组
     * 因此 如果有两个权限属于一个组 则只会将一项添加到对话框中
     */
    private fun buildPermissionsLayout() {
        val tempSet = HashSet<String>()
        val currentVersion = Build.VERSION.SDK_INT
        for (permission in permissions) {
            val permissionGroup = when {
                currentVersion < Build.VERSION_CODES.Q -> {
                    try {
                        val permissionInfo = context.packageManager.getPermissionInfo(permission, 0)
                        permissionInfo.group
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                        null
                    }
                }
                currentVersion == Build.VERSION_CODES.Q -> permissionMapOnQ[permission]
                currentVersion == Build.VERSION_CODES.R -> permissionMapOnR[permission]
                currentVersion == Build.VERSION_CODES.S -> permissionMapOnS[permission]
                currentVersion == Build.VERSION_CODES.TIRAMISU -> permissionMapOnT[permission]
                else -> {
                    // 如果 currentVersion 比我们支持的最新版本更新
                    // 我们只需将最新版本用于 temp 将在下一个版本中升级
                    permissionMapOnT[permission]
                }
            }
            if ((permission in allSpecialPermissions && !tempSet.contains(permission))
                || (permissionGroup != null && !tempSet.contains(permissionGroup))) {
                val itemBinding = PermissionxPermissionItemBinding.inflate(layoutInflater, binding.permissionsLayout, false)
                when {
                    permission == Manifest.permission.ACCESS_BACKGROUND_LOCATION -> {
                        itemBinding.permissionText.text = context.getString(R.string.permissionx_access_background_location)
                        itemBinding.permissionIcon.setImageResource(context.packageManager.getPermissionGroupInfo(permissionGroup!!, 0).icon)
                    }
                    permission == Manifest.permission.SYSTEM_ALERT_WINDOW -> {
                        itemBinding.permissionText.text = context.getString(R.string.permissionx_system_alert_window)
                        itemBinding.permissionIcon.setImageResource(R.drawable.permissionx_ic_alert)
                    }
                    permission == Manifest.permission.WRITE_SETTINGS -> {
                        itemBinding.permissionText.text = context.getString(R.string.permissionx_write_settings)
                        itemBinding.permissionIcon.setImageResource(R.drawable.permissionx_ic_setting)
                    }
                    permission == Manifest.permission.MANAGE_EXTERNAL_STORAGE -> {
                        itemBinding.permissionText.text = context.getString(R.string.permissionx_manage_external_storage)
                        itemBinding.permissionIcon.setImageResource(context.packageManager.getPermissionGroupInfo(permissionGroup!!, 0).icon)
                    }
                    permission == Manifest.permission.REQUEST_INSTALL_PACKAGES -> {
                        itemBinding.permissionText.text = context.getString(R.string.permissionx_request_install_packages)
                        itemBinding.permissionIcon.setImageResource(R.drawable.permissionx_ic_install)
                    }
                    permission == Manifest.permission.POST_NOTIFICATIONS
                            && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU -> {
                        // 当操作系统版本低于 Android 13 时 没有通知图标或 labelRes 供我们获取 所以我们需要以特殊许可的方式处理
                        itemBinding.permissionText.text = context.getString(R.string.permissionx_post_notification)
                        itemBinding.permissionIcon.setImageResource(R.drawable.permissionx_ic_notification)
                    }
                    permission == Manifest.permission.BODY_SENSORS_BACKGROUND -> {
                        itemBinding.permissionText.text = context.getString(R.string.permissionx_body_sensor_background)
                        itemBinding.permissionIcon.setImageResource(context.packageManager.getPermissionGroupInfo(permissionGroup!!, 0).icon)
                    }
                    else -> {
                        itemBinding.permissionText.text = context.getString(context.packageManager.getPermissionGroupInfo(permissionGroup!!, 0).labelRes)
                        itemBinding.permissionIcon.setImageResource(context.packageManager.getPermissionGroupInfo(permissionGroup, 0).icon)
                    }
                }
                if (isDarkTheme()) {
                    if (darkColor != -1) {
                        itemBinding.permissionIcon.setColorFilter(darkColor, PorterDuff.Mode.SRC_ATOP)
                    }
                } else {
                    if (lightColor != -1) {
                        itemBinding.permissionIcon.setColorFilter(lightColor, PorterDuff.Mode.SRC_ATOP)
                    }
                }
                binding.permissionsLayout.addView(itemBinding.root)
                tempSet.add(permissionGroup ?: permission)
            }
        }
    }

    /**
     * 要显示的设置对话框窗口 在纵向和横向模式下控制不同的窗口大小
     */
    private fun setupWindow() {
        val width = context.resources.displayMetrics.widthPixels
        val height = context.resources.displayMetrics.heightPixels
        if (width < height) {
            // 现在我们在竖直屏幕
            window?.let {
                val param = it.attributes
                it.setGravity(Gravity.CENTER)
                param.width = (width * 0.86).toInt()
                it.attributes = param
            }
        } else {
            // 现在我们在横屏
            window?.let {
                val param = it.attributes
                it.setGravity(Gravity.CENTER)
                param.width = (width * 0.6).toInt()
                it.attributes = param
            }
        }
    }

    /**
     * 目前我们是否处于黑暗主题
     */
    private fun isDarkTheme(): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }

}