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
package com.permissionx.guolindev.callback

import com.permissionx.guolindev.request.ForwardScope

/**
 * [PermissionBuilder.onForwardToSettings] 方法的回调
 */
interface ForwardToSettingsCallback {
    /**
     * 当您应该告诉用户在设置中允许这些权限时调用
     * @param scope 显示基本原理对话框的范围
     * @param deniedList 设置中应该允许的权限
     */
    fun onForwardToSettings(scope: ForwardScope, deniedList: List<String>)
}