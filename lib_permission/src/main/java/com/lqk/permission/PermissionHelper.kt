package com.lqk.permission

import android.Manifest
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.request.ExplainScope

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
            .onExplainRequestReason { scope, deniedList ->
                run {

                }
            }
            .onForwardToSettings { scope, deniedList ->
                run {

                }
            }
            .request { allGranted, grantedList, deniedList ->
                run {

                }
            }
    }
}