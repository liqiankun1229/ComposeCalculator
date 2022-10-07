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

package com.permissionx.guolindev.callback;

import androidx.annotation.NonNull;

import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.PermissionBuilder;

import java.util.List;

/**
 * 回调 {@link PermissionBuilder#onExplainRequestReason(ExplainReasonCallbackWithBeforeParam)} 方法
 */
public interface ExplainReasonCallbackWithBeforeParam {

    /**
     * 当您应该解释为什么需要这些权限时调用
     * @param scope 显示基本原理对话框的范围
     * @param deniedList 您应该解释的权限
     * @param beforeRequest 表示它是在权限请求之前或之后 使用 {@link PermissionBuilder#explainReasonBeforeRequest()}
     */
    void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList, boolean beforeRequest);

}
