package com.lqk.launcher

import android.view.LayoutInflater
import com.lqk.base.fragment.viewbinding.BaseVBFragment
import com.lqk.launcher.databinding.FragmentLoginBinding

/**
 * @author LQK
 * @time 2022/10/12 22:16
 *
 */
class LoginFragment :BaseVBFragment<FragmentLoginBinding>() {

    override fun layoutId(): Int {
        return R.layout.fragment_login
    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater)
    }

}