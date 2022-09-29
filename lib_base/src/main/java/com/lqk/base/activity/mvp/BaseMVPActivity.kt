package com.lqk.base.activity.mvp

import androidx.viewbinding.ViewBinding
import com.lqk.base.activity.viewbinding.BaseVBActivity
import com.lqk.base.activity.mvp.IBaseContacts.IPresenter

/**
 * @author LQK
 * @time 2022/8/2 11:10
 *
 */
abstract class BaseMVPActivity<VB : ViewBinding, P : IPresenter>
    : BaseVBActivity<VB>() {
}