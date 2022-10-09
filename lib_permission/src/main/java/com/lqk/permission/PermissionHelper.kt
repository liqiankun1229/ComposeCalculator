package com.lqk.permission

import android.Manifest
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.ForwardToSettingsCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.permissionx.guolindev.request.ExplainScope
import com.permissionx.guolindev.request.ForwardScope

/**
 * @author LQK
 * @time 2022/9/27 23:35
 *
 */
class PermissionHelper {

    // 判断权限是否被授予

    // 设置申请权限需要的 说明用途 弹框

    // 申请权限前 读取当前是否已经锁定屏幕方向 ,没有的话锁定(申请结束后释放)

    // 是否再次申请权限(第一次调用申请后 返回结果(被拒绝) -> 再次申请)

    // 显示再次申请权限所需要的弹框

    fun doPermission(activity: AppCompatActivity) {
        PermissionX.init(activity)
            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .explainReasonBeforeRequest()
            .onExplainRequestReason(object : ExplainReasonCallback {
                override fun onExplainReason(scope: ExplainScope, deniedList: List<String>) {

                }
            })
            .onForwardToSettings(object : ForwardToSettingsCallback {
                override fun onForwardToSettings(scope: ForwardScope, deniedList: List<String>) {

                }
            })
            .request(object :RequestCallback{
                override fun onResult(allGranted: Boolean, grantedList: List<String>, deniedList: List<String>) {

                }
            })
    }
}