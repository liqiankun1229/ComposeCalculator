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

import kotlin.jvm.JvmOverloads
import com.permissionx.guolindev.dialog.RationaleDialog
import com.permissionx.guolindev.dialog.RationaleDialogFragment

/**
 * 为
 * [com.permissionx.guolindev.callback.ExplainReasonCallback]
 * 和
 * [com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam]
 * 提供特定的范围 以赋予其特定的调用函数
 */
class ExplainScope internal constructor(
    private val pb: PermissionBuilder,
    private val chainTask: ChainTask
) {
    /**
     * 显示一个基本原理对话框，向用户解释您为什么需要这些权限
     * @param permissions   请求的权限
     * @param message       显示给用户的消息
     * @param positiveText  正面按钮上的文本 当用户单击时 PermissionX 将再次请求权限
     * @param negativeText  否定按钮上的文本 当用户点击时 PermissionX 将完成请求
     */
    @JvmOverloads
    fun showRequestReasonDialog(permissions: List<String>, message: String, positiveText: String, negativeText: String? = null) {
        pb.showHandlePermissionDialog(chainTask, true, permissions, message, positiveText, negativeText)
    }

    /**
     * 显示一个基本原理对话框，向用户解释您为什么需要这些权限
     * @param dialog RationaleDialog 向用户解释为什么需要这些权限的对话框
     */
    fun showRequestReasonDialog(dialog: RationaleDialog) {
        pb.showHandlePermissionDialog(chainTask, true, dialog)
    }

    /**
     * 显示一个基本原理对话框，向用户解释您为什么需要这些权限。
     * @param dialogFragment DialogFragment 向用户解释为什么需要这些权限
     */
    fun showRequestReasonDialog(dialogFragment: RationaleDialogFragment) {
        pb.showHandlePermissionDialog(chainTask, true, dialogFragment)
    }
}