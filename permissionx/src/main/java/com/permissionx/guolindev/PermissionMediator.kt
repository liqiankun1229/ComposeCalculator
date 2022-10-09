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

package com.permissionx.guolindev

import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.dialog.allSpecialPermissions
import com.permissionx.guolindev.request.RequestBackgroundLocationPermission
import kotlin.collections.LinkedHashSet

/**
 * 为传递权限参数提供特定范围的内部类
 */
class PermissionMediator {

    private var activity: FragmentActivity? = null
    private var fragment: Fragment? = null

    constructor(activity: FragmentActivity) {
        this.activity = activity
    }

    constructor(fragment: Fragment) {
        this.fragment = fragment
    }

    /**
     * 您要请求的所有权限
     *
     * @param permissions 要申请的权限列表
     * @return PermissionBuilder 本身
     */
    fun permissions(permissions: List<String>): PermissionBuilder {
        val normalPermissionSet = LinkedHashSet<String>()
        val specialPermissionSet = LinkedHashSet<String>()
        val osVersion = Build.VERSION.SDK_INT
        val targetSdkVersion = if (activity != null) {
            activity!!.applicationInfo.targetSdkVersion
        } else {
            fragment!!.requireContext().applicationInfo.targetSdkVersion
        }
        for (permission in permissions) {
            if (permission in allSpecialPermissions) {
                specialPermissionSet.add(permission)
            } else {
                normalPermissionSet.add(permission)
            }
        }
        if (RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION in specialPermissionSet) {
            if (osVersion == Build.VERSION_CODES.Q ||
                (osVersion == Build.VERSION_CODES.R && targetSdkVersion < Build.VERSION_CODES.R)) {
                // 如果我们在 Q 或 R 上请求 ACCESS_BACKGROUND_LOCATION 但 targetSdkVersion 在 R 以下
                // 我们不需要特别请求 只需按正常权限请求即可
                specialPermissionSet.remove(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                normalPermissionSet.add(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
            }
        }
        if (PermissionX.permission.POST_NOTIFICATIONS in specialPermissionSet) {
            if (osVersion >= Build.VERSION_CODES.TIRAMISU && targetSdkVersion >= Build.VERSION_CODES.TIRAMISU) {
                // 如果我们在 TIRAMISU 或以上请求 POST_NOTIFICATIONS 并且 targetSdkVersion >= TIRAMISU
                // 我们不需要特别请求 只需按正常权限请求即可
                specialPermissionSet.remove(PermissionX.permission.POST_NOTIFICATIONS)
                normalPermissionSet.add(PermissionX.permission.POST_NOTIFICATIONS)
            }
        }
        return PermissionBuilder(activity, fragment, normalPermissionSet, specialPermissionSet)
    }

    /**
     * 您要请求的所有权限
     *
     * @param permissions 要申请的权限列表 vararg
     * @return PermissionBuilder 本身
     */
    fun permissions(vararg permissions: String): PermissionBuilder {
        return permissions(listOf(*permissions))
    }

}