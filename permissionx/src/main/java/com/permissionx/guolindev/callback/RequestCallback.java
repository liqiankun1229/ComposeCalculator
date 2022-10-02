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

import java.util.List;

/**
 * 请求权限的回调 用于
 * {@link com.permissionx.guolindev.request.PermissionBuilder#request(RequestCallback)}
 */
public interface RequestCallback {

    /**
     * 请求结果的回调
     * @param allGranted – 指示是否授予了所有权限
     * @param grantedList – 用户授予的所有权限
     * @param deniedList – 用户拒绝的所有权限
     */
    void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList);

}
