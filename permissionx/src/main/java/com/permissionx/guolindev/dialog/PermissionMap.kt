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
import android.annotation.TargetApi
import android.os.Build
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.request.RequestBackgroundLocationPermission
import com.permissionx.guolindev.request.RequestBodySensorsBackgroundPermission
import com.permissionx.guolindev.request.RequestInstallPackagesPermission
import com.permissionx.guolindev.request.RequestManageExternalStoragePermission

/**
 * 维护我们需要通过特殊情况处理的所有特殊权限
 */
val allSpecialPermissions = setOf(
    RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION,
    Manifest.permission.SYSTEM_ALERT_WINDOW,
    Manifest.permission.WRITE_SETTINGS,
    RequestManageExternalStoragePermission.MANAGE_EXTERNAL_STORAGE,
    RequestInstallPackagesPermission.REQUEST_INSTALL_PACKAGES,
    PermissionX.permission.POST_NOTIFICATIONS,
    RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND,
)

/**
 * 基于此链接 https://developer.android.com/about/versions/10/privacy/changes#permission-groups-removed
 * 从 Android Q 开始 我们无法再通过权限名称获取权限组名称
 * 因此 我们需要跟踪自 Android Q 以来每个 Android 版本的权限和权限组之间的关系
 */
@TargetApi(Build.VERSION_CODES.Q)
val permissionMapOnQ = mapOf(
    Manifest.permission.READ_CALENDAR to Manifest.permission_group.CALENDAR,
    Manifest.permission.WRITE_CALENDAR to Manifest.permission_group.CALENDAR,
    Manifest.permission.READ_CALL_LOG to Manifest.permission_group.CALL_LOG,
    Manifest.permission.WRITE_CALL_LOG to Manifest.permission_group.CALL_LOG,
    "android.permission.PROCESS_OUTGOING_CALLS" to Manifest.permission_group.CALL_LOG,
    Manifest.permission.CAMERA to Manifest.permission_group.CAMERA,
    Manifest.permission.READ_CONTACTS to Manifest.permission_group.CONTACTS,
    Manifest.permission.WRITE_CONTACTS to Manifest.permission_group.CONTACTS,
    Manifest.permission.GET_ACCOUNTS to Manifest.permission_group.CONTACTS,
    Manifest.permission.ACCESS_FINE_LOCATION to Manifest.permission_group.LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION to Manifest.permission_group.LOCATION,
    Manifest.permission.ACCESS_BACKGROUND_LOCATION to Manifest.permission_group.LOCATION,
    Manifest.permission.RECORD_AUDIO to Manifest.permission_group.MICROPHONE,
    Manifest.permission.READ_PHONE_STATE to Manifest.permission_group.PHONE,
    Manifest.permission.READ_PHONE_NUMBERS to Manifest.permission_group.PHONE,
    Manifest.permission.CALL_PHONE to Manifest.permission_group.PHONE,
    Manifest.permission.ANSWER_PHONE_CALLS to Manifest.permission_group.PHONE,
    Manifest.permission.ADD_VOICEMAIL to Manifest.permission_group.PHONE,
    Manifest.permission.USE_SIP to Manifest.permission_group.PHONE,
    Manifest.permission.ACCEPT_HANDOVER to Manifest.permission_group.PHONE,
    Manifest.permission.BODY_SENSORS to Manifest.permission_group.SENSORS,
    Manifest.permission.ACTIVITY_RECOGNITION to Manifest.permission_group.ACTIVITY_RECOGNITION,
    Manifest.permission.SEND_SMS to Manifest.permission_group.SMS,
    Manifest.permission.RECEIVE_SMS to Manifest.permission_group.SMS,
    Manifest.permission.READ_SMS to Manifest.permission_group.SMS,
    Manifest.permission.RECEIVE_WAP_PUSH to Manifest.permission_group.SMS,
    Manifest.permission.RECEIVE_MMS to Manifest.permission_group.SMS,
    Manifest.permission.READ_EXTERNAL_STORAGE to Manifest.permission_group.STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE to Manifest.permission_group.STORAGE,
    Manifest.permission.ACCESS_MEDIA_LOCATION to Manifest.permission_group.STORAGE,
)

/**
 * 值得庆幸的是 Android R 相对 Android Q 没有添加或删除任何权限
 */
@TargetApi(Build.VERSION_CODES.R)
val permissionMapOnR = mapOf(
    Manifest.permission.MANAGE_EXTERNAL_STORAGE to Manifest.permission_group.STORAGE,
).toMutableMap().apply {
    putAll(permissionMapOnQ)
}.toMap()

/**
 * Android S 添加了 3 个蓝牙相关的运行时权限
 */
@TargetApi(Build.VERSION_CODES.S)
val permissionMapOnS = mapOf(
    Manifest.permission.BLUETOOTH_SCAN to Manifest.permission_group.NEARBY_DEVICES,
    Manifest.permission.BLUETOOTH_ADVERTISE to Manifest.permission_group.NEARBY_DEVICES,
    Manifest.permission.BLUETOOTH_CONNECT to Manifest.permission_group.NEARBY_DEVICES,
).toMutableMap().apply {
    putAll(permissionMapOnR)
}.toMap()

/**
 * Android T 添加了 3 个读取存储运行时权限 1个通知运行时权限  1个 wifi 运行时权限 1个 身体传感器运行时权限
 */
@TargetApi(Build.VERSION_CODES.TIRAMISU)
val permissionMapOnT = mapOf(
    Manifest.permission.READ_MEDIA_IMAGES to Manifest.permission_group.READ_MEDIA_VISUAL,
    Manifest.permission.READ_MEDIA_VIDEO to Manifest.permission_group.READ_MEDIA_VISUAL,
    Manifest.permission.READ_MEDIA_AUDIO to Manifest.permission_group.READ_MEDIA_AURAL,
    Manifest.permission.POST_NOTIFICATIONS to Manifest.permission_group.NOTIFICATIONS,
    Manifest.permission.NEARBY_WIFI_DEVICES to Manifest.permission_group.NEARBY_DEVICES,
    Manifest.permission.BODY_SENSORS_BACKGROUND to Manifest.permission_group.SENSORS,
).toMutableMap().apply {
    putAll(permissionMapOnS)
}.toMap()