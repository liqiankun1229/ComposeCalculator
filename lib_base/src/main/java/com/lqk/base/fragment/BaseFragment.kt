package com.lqk.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * @author LQK
 * @time 2022/10/9 11:13
 *
 */
abstract class BaseFragment : Fragment() {

    /**
     * 初始化 Fragment
     */
    open fun initFragment(inflater: LayoutInflater, container: ViewGroup?): View {

        return inflater.inflate(layoutId(), container, false)
    }

    abstract fun layoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return initFragment(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initData()
    }

    open fun initView() {}
    open fun initListener() {}
    open fun initData() {}
}