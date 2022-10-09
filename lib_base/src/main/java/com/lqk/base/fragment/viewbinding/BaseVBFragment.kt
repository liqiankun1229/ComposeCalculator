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

    private val viewBinding: VB by lazy {
        initViewBinding()
    }

    abstract fun initViewBinding(): VB

    override fun initFragment(inflater: LayoutInflater, container: ViewGroup?): View {

        return viewBinding.root
    }

}