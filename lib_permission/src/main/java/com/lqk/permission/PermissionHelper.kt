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