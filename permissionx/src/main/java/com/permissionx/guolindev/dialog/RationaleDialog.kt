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
package com.permissionx.guolindev.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnCancelListener
import android.view.View

/**
 * 默认对话框基类
 * 构造方法
 * 确认按钮
 * 取消按钮
 * 请求的权限列表
 */
abstract class RationaleDialog : Dialog {
    constructor(context: Context) : this(context, -1)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)
    protected constructor(context: Context, cancelable: Boolean, cancelListener: OnCancelListener?)
            : super(context, cancelable, cancelListener)

    /**
     * 获取(确认-允许)按钮
     */
    abstract fun getPositiveButton(): View

    /**
     * 获取(取消-拒绝)按钮 必要权限 取消可为空!
     */
    abstract fun getNegativeButton(): View?

    /**
     * 请求的权限列表
     */
    abstract fun getPermissionsToRequest(): List<String>
}