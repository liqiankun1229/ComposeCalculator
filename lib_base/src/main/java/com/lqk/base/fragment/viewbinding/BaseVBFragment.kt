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
 *
 */
abstract class BaseVBFragment<VB : ViewBinding> : BaseFragment() {

    abstract fun initViewBinding(inflater: LayoutInflater): VB

    protected lateinit var viewBinding: VB

    override fun initFragment(inflater: LayoutInflater, container: ViewGroup?): View {
        viewBinding = initViewBinding(inflater)
        return viewBinding.root
    }

}