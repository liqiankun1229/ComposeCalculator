package com.lqk.base.fragment.databinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.lqk.base.fragment.BaseFragment

/**
 * @author LQK
 * @time 2022/10/12 21:47
 *
 */
abstract class BaseDBFragment<VDB : ViewDataBinding> : BaseFragment() {
    abstract fun initDataBinding(inflater: LayoutInflater): VDB

    protected lateinit var dataBinding: VDB

    override fun initFragment(inflater: LayoutInflater, container: ViewGroup?): View {
        // 初始化
        dataBinding = initDataBinding(inflater)
        return dataBinding.root
    }
}