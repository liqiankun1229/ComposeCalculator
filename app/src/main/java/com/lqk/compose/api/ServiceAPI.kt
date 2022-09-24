package com.lqk.compose.api

import com.lqk.compose.bean.PackageBean
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * @author LQK
 * @time 2022/9/24 21:56
 *
 */
interface ServiceAPI {
    @POST
    suspend fun initPackageList(@Url url: String = "${BaseUrl.Base_URL}/zip/packages")
            : BaseResponseBean<MutableList<PackageBean>>
}