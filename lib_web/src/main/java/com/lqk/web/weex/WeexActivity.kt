package com.lqk.web.weex

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lqk.network.NetWorkOperation
import com.lqk.network.OkHttpUtil
import com.lqk.utils.StatusBarUtil
import com.lqk.web.R
import com.lqk.web.conf.Config
import com.lqk.web.ui.widget.web.local.cacheWeexPath
import com.taobao.weex.IWXRenderListener
import com.taobao.weex.WXSDKInstance
import com.taobao.weex.common.RenderTypes
import com.taobao.weex.common.WXRenderStrategy.APPEND_ASYNC
import java.io.File

class WeexActivity : AppCompatActivity(), IWXRenderListener {
    
    companion object {
        
        const val TAG = "WeexActivity"
        private const val EXTRA_URL = "EXTRA_URL"
        
        /**
         * 启动 Weex 页面
         * @param context 启动页的 上下文
         * @param url 文件路径 ( 网络 / 本地 )
         */
        fun start(context: Context, url: String) {
            Log.d(TAG, "start: url: $url")
            val weexIntent = Intent(context, WeexActivity::class.java)
            weexIntent.putExtra(EXTRA_URL, url)
            context.startActivity(weexIntent)
        }
    }
    
    private var mWXSDKInstance: WXSDKInstance? = null
    
    override fun onViewCreated(p0: WXSDKInstance?, p1: View?) {
        setContentView(p1)
    }
    
    override fun onRenderSuccess(p0: WXSDKInstance?, p1: Int, p2: Int) {
        Log.d(TAG, "onRenderSuccess: ")
    }
    
    override fun onRefreshSuccess(p0: WXSDKInstance?, p1: Int, p2: Int) {
        Log.d(TAG, "onRefreshSuccess: ")
    }
    
    override fun onException(p0: WXSDKInstance?, p1: String?, p2: String?) {
        Log.d(TAG, "onException: ")
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // icon : 主题
        StatusBarUtil.setDarkMode(this)
        StatusBarUtil.setTransparent(this)
        StatusBarUtil.getStatusBarHeight(this)
        Log.d(TAG, "onCreate: ${StatusBarUtil.getStatusBarHeight(this)}")
        val displayM = DisplayMetrics()
        this.windowManager.defaultDisplay.getRealMetrics(displayM)
        val width = displayM.widthPixels
        val height = displayM.heightPixels
        Log.d(TAG, "onCreate1: ${width}:${height}")
        val displayMetrics = resources.displayMetrics
        Log.d(TAG, "onCreate2: ${displayMetrics.widthPixels}:${displayMetrics.heightPixels}")
        setContentView(R.layout.activity_weex)
        
        mWXSDKInstance = WXSDKInstance(this)
        mWXSDKInstance?.registerRenderListener(this)
        /**
         * bundleUrl source http://dotwe.org/vue/38e202c16bdfefbdb88a8754f975454c
         */
//        val url = intent.getStringExtra(EXTRA_URL) ?: "file:///android_asset/dist/index.js"
        val url = intent.getStringExtra(EXTRA_URL) ?: "file://android_asset/dist/main.js"
//        val url = intent.getStringExtra(EXTRA_URL) ?: "file://assets/dist/main.js"
        Log.d(TAG, "onCreate: url: $url")
        // 加载服务器 weex
        openWeex(url)
    }
    
    /**
     * 打开 weex 文件: webpack 打包生成的 js 文件
     */
    private fun openWeex(url: String) {
        if (url.startsWith("http")) {
            openWeexFromUrl(url)
        } else {
            openWeexFromFile(url)
        }
    }
    
    private fun openWeexFromString(url: String, data: String) {
        val pageName = "WXSample"
        url.contains(RenderTypes.RENDER_TYPE_HERON_URL_PARAM)
        Log.d(TAG, "openWeexFromFile: $url")
        if (url.startsWith("http")) {
        }
        mWXSDKInstance?.render(
            pageName,
            data,
            null,
            null,
            APPEND_ASYNC
        )
    }
    
    private fun String.url2path():String = this.replace(Config.BASE_WEEX_URL, cacheWeexPath())
    private fun String.path2url():String = this.replace(cacheWeexPath(), Config.BASE_WEEX_URL)
    
    /**
     * 打开本地文件
     * 判断本地文件是否存在, 不存在 更换本地 缓存路径, 替换为 域名地址
     *
     * @param url 本地绝对路劲
     */
    private fun openWeexFromFile(url: String) {
        Log.d(TAG, "openWeexFromFile: $url")
        val localFile = File(url.replace("file:",""))
        if (localFile.exists() || url.startsWith("file://android_asset")){
            //
            val pageName = "WXSample"
            url.contains(RenderTypes.RENDER_TYPE_HERON_URL_PARAM)
            if (url.startsWith("http")) {
            }
            // 拓展 JSEnv
            val options = hashMapOf<String, Any>()
            options["native"] = "Android-App"
            mWXSDKInstance?.renderByUrl(
                pageName,
                url,
                options,
                null,
                APPEND_ASYNC
            )
        } else {
            openWeexFromUrl(url.path2url())
        }
    }
    
    /**
     * 替换域名为本地路径
     */
    private fun weexReplace(weexUrl: String): String {
        var url = weexUrl
        // 删除 http  https
        Log.d(TAG, "weexReplace: $url")
        url = url.replace("http://", "")
        Log.d(TAG, "weexReplace: $url")
        url = url.replace("https://", "")
        Log.d(TAG, "weexReplace: $url")
        
        val pathSplit = url.split("/")
        var newUrl = cacheWeexPath()
        for (i in 1 until pathSplit.size) {
            newUrl += "/${pathSplit[i]}"
        }
        return newUrl
    }
    
    private fun String.gainPath(): String {
        val pathList = this.split("/")
        var path = ""
        for (i in 0 until pathList.size - 1) {
            path += "/${pathList[i]}"
        }
        return path
    }
    
    /**
     * 先下载, 下载完成后 返回本地文件地址
     *
     * 去除域名 替换成 本地文件路径
     * 本地地址
     * ${cacheDir.absolutePath}/weexCache ->
     * file:/data/user/0/package/weexCache/weex/
     * 直接创建本地路径 // 多级创建 mkdirs
     * 下载 js 文件
     * 打开本地文件地址
     * @param url http js 资源文件
     */
    private fun openWeexFromUrl(url: String) {
        // 下载
        val filePath = weexReplace(url)
        val fileDir = filePath.gainPath()
        val dir = File(fileDir)
        // 直接创建目录 [多级]
        dir.mkdirs()
        //
//        val filePath = "$fileDir/main.js"
        Log.d(TAG, "openWeexFromUrl: $url")
        Log.d(TAG, "openWeexFromUrl: $fileDir")
        Log.d(TAG, "openWeexFromUrl: $filePath")
        OkHttpUtil.instance.download(
            filePath,
            url,
            hashMapOf(),
            File::class.java,
            object : NetWorkOperation.OnDownLoadListener {
                override fun onSucceed(data: File) {
                    Log.d(TAG, "onSucceed: ${data.path}")
                    Toast.makeText(
                        this@WeexActivity,
                        "weex: ${filePath}下载成功",
                        Toast.LENGTH_SHORT
                    ).show()
                    openWeexFromFile("file:" + data.absolutePath)
                }
                
                override fun onFailed(msg: String) {
                    Log.d(TAG, "onFailed: $msg")
                }
                
                override fun downloadProcess(process: Int) {
                    Log.d(TAG, "downloadProcess: $process")
                }
            })
        // open
    }
    
    /**
     * @param url 本地文件路径
     */
    fun weexRender(url: String) {
    
    }
    
    override fun onResume() {
        super.onResume()
        mWXSDKInstance?.onActivityResume()
    }
    
    override fun onPause() {
        super.onPause()
        mWXSDKInstance?.onActivityPause()
    }
    
    override fun onStop() {
        super.onStop()
        mWXSDKInstance?.onActivityStop()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        mWXSDKInstance?.onActivityDestroy()
        mWXSDKInstance = null
    }
}