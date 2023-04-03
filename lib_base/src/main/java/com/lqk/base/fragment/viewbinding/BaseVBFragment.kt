package com.lqk.base.fragment.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lqk.base.activity.BaseActivity
import com.lqk.base.fragment.BaseFragment

/**
 * @author LQK
 * @time 2022/8/1 15:43
 * Fragment videBinding 有内存泄露的风险
 */
abstract class BaseVBFragment<VB : ViewBinding> : BaseFragment() {

    abstract fun initViewBinding(inflater: LayoutInflater): VB

    protected var viewBinding: VB? = null

    override fun initFragment(inflater: LayoutInflater, container: ViewGroup?): View {
        viewBinding = initViewBinding(inflater)
        return viewBinding!!.root
    }

    override fun onDestroy() {
        // 释放
        viewBinding = null
        super.onDestroy()
    }
}