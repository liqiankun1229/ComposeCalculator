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

package com.permissionx.guolindev;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * 一个开源的 Android 库，可以非常轻松地处理运行时权限。以下代码段显示了简单的用法：
 * PermissionX.init(activity)
 *    .permissions(Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA)
 *    .request { allGranted, grantedList, deniedList ->
 *        // handling the logic
 *    }
 */
public class PermissionX {

    /**
     * 初始化 PermissionX 让一切准备好工作。
     * @param activity– FragmentActivity 的一个实例
     * @return PermissionCollection 实例
     */
    public static PermissionMediator init(@NonNull FragmentActivity activity) {
        return new PermissionMediator(activity);
    }

    /**
     * 初始化 PermissionX 让一切准备好工作。
     * @param fragment Fragment
     * @return PermissionCollection 实例的实例。
     */
    public static PermissionMediator init(@NonNull Fragment fragment) {
        return new PermissionMediator(fragment);
    }

    /**
     *  检查权限的辅助函数是否被授予
     * @param context 不会保留任何上下文
     * @param permission 权限 - 要检查的特定权限名称。例如 [android.Manifest.permission.CAMERA]
     * @return 如果授予此权限，则为 True，否则为 False
     */
    public static boolean isGranted(@NonNull Context context, @NonNull String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 检查是否为当前应用启用了通知的辅助功能。
     * @param context – 不会保留任何上下文。
     * @return 如果 Android 版本低于 N，则返回值将始终为 true。
     */
    public static boolean areNotificationsEnabled(@NonNull Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }

    public static final class permission {
        /**
         * Define the const to compat with system lower than T.
         */
        public static final String POST_NOTIFICATIONS = "android.permission.POST_NOTIFICATIONS";
    }
}
