package com.lqk.flutter

/**
 * @author LQK
 * @time 2022/1/19 23:51
 * @remark
 */
interface OnActivityMsgListener {
    fun <T> send(a: T)
}