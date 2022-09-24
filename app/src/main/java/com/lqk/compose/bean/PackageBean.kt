package com.lqk.compose.bean

import java.io.Serializable

/**
 * @author LQK
 * @time 2022/9/24 22:01
 *
 */
data class PackageBean(
    var appId: String,
    var category: String,
    var name: String,
    var url: String,
    var versionCode: String,
    var versionName: String,
    var home: String,
    var packageType: Int,
    var onlineUrl: String
) : Serializable
