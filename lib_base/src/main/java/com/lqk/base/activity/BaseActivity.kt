package com.lqk.base.activity

import android.os.Bundle

/**
 * @author LQK
 * @time 2022/8/1 15:33
 *
 */
abstract class BaseActivity : androidx.activity.ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActivity()
    }

    /**
     * 初始化 Activity (窗口/页面/事件/数据)
     */
    open fun initActivity() {
        initWindow()
        setContentView(layoutId())
        initView()
        initListener()
        initEvent()
        initData()
    }

    /**
     * 窗口设置
     */
    open fun initWindow() {}

    /**
     * 布局 Id
     */
    abstract fun layoutId(): Int

    /**
     * 初始样式
     */
    open fun initView() {}

    /**
     * 事件监听
     */
    open fun initListener() {}

    /**
     * 消息事件
     */
    open fun initEvent() {}

    /**
     * 数据请求
     */
    open fun initData() {}


}