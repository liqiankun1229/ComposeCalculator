package com.lqk.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @author LQK
 * @time 2022/9/27 22:15
 *
 */
abstract class BaseComposeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
    }

    protected open fun initActivity() {
        initToolbar()
        setContentView(initLayoutId())
    }

    /**
     * 布局 Id
     */
    abstract fun initLayoutId(): Int

    protected open fun initToolbar() {}


}