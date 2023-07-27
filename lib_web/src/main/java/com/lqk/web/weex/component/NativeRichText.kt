package com.lqk.web.weex.component

import android.content.Context
import android.text.Html
import android.util.Log
import android.widget.TextView
import com.taobao.weex.WXSDKInstance
import com.taobao.weex.annotation.JSMethod
import com.taobao.weex.ui.action.BasicComponentData
import com.taobao.weex.ui.component.WXComponent
import com.taobao.weex.ui.component.WXVContainer

/**
 *
 * @create LQK 2023/5/30 15:27
 */
class NativeRichText : WXComponent<TextView> {
    
    companion object {
        
        const val TAG = "NativeRichText"
    }
    
    constructor(
        instance: WXSDKInstance?,
        parent: WXVContainer<*>?,
        instanceId: String?,
        isLazy: Boolean,
        basicComponentData: BasicComponentData<*>?
    ) : super(instance, parent, instanceId, isLazy, basicComponentData) {
        this.initCustomView()
    }
    
    constructor(
        instance: WXSDKInstance?,
        parent: WXVContainer<*>?,
        basicComponentData: BasicComponentData<*>?
    ) : super(instance, parent, basicComponentData) {
        this.initCustomView()
    }
    
    private fun initCustomView() {
        Log.d(TAG, "initCustomView: ")
    }
    
    // 创建 view
    override fun initComponentHostView(context: Context): TextView {
        val v = TextView(context)
        v.text = Html.fromHtml(this.basicComponentData.attrs["txt"].toString())
        Log.d(TAG, "initComponentHostView: ${v.text}")
        
        return v
    }
    
    override fun onHostViewInitialized(host: TextView?) {
        super.onHostViewInitialized(host)
    }
    
    /**
     * 将方法暴露给H5
     * this.$refs.view.clickRich(params)
     */
    @JSMethod
    fun clickRich(){}
    
    
}