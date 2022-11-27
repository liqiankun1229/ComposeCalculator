package com.lqk.launcher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lqk.base.activity.viewbinding.BaseVBActivity
import com.lqk.camera.CameraActivity
import com.lqk.launcher.databinding.ActivityLauncherBinding

class LauncherActivity : BaseVBActivity<ActivityLauncherBinding>() {

    override fun layoutId(): Int = R.layout.activity_launcher

    override fun initViewBinding(): ActivityLauncherBinding = ActivityLauncherBinding.inflate(layoutInflater)

    override fun initListener() {
        super.initListener()
        viewBinding.btnCamera.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }
}