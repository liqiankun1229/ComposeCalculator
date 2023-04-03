package com.lqk.base.activity.mvi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author LQK
 * @time 2022/8/3 17:07
 *
 */
open class BaseViewModel : ViewModel() {

    // 定义 视图状态
    private val _viewStates: MutableLiveData<BaseViewState> = MutableLiveData()
    val viewStates: LiveData<BaseViewState> = _viewStates

    private val _viewEvents: MutableLiveData<String> = MutableLiveData()
    val viewEvents: LiveData<String> = _viewEvents


    /**
     * 接收到用户界面事件 准备分发处理请求数据(或者其他耗时操作)
     */
    fun emit(state: BaseViewState) {

    }

    /**
     * 分发用户事件返回
     */
    fun dispatch(action: BaseAction) {

    }
}