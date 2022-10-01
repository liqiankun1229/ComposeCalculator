package com.lqk.base.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

/**
 * @author LQK
 * @time 2022/9/27 22:15
 *
 */
abstract class BaseComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
    }

    protected open fun initActivity() {
        initToolbar()
        setContent{}
    }

    protected open fun initToolbar() {}



}