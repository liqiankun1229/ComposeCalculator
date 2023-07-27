package com.lqk.web.ui.widget.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.lqk.web.R
import com.lqk.web.databinding.ActivityOfficeBinding
import com.lqk.web.ui.activity.BaseVBActivity
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsReaderView
import com.tencent.tbs.reader.ITbsReaderCallback
import com.tencent.tbs.reader.TbsFileInterfaceImpl

/**
 * 预览 Office 文件
 */
class OfficeActivity : BaseVBActivity<ActivityOfficeBinding>(), ITbsReaderCallback {
    
    companion object {
        
        const val TAG = "OfficeActivity"
        private const val EXTRA_FILEPATH = "EXTRA_FILEPATH"
        
        /**
         * 打开文件
         */
        fun start(context: Context, filePath: String) {
            val officeIntent = Intent(context, OfficeActivity::class.java)
            officeIntent.putExtra(EXTRA_FILEPATH, filePath)
            context.startActivity(officeIntent)
        }
        
    }
    
    //    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_office)
//    }
    override fun initLayoutId(): Int {
        return R.layout.activity_office
    }
    
    override fun initViewBinding(): ActivityOfficeBinding {
        return ActivityOfficeBinding.inflate(this.layoutInflater)
    }
    
    override fun initView() {
        super.initView()
//        var file = File("file:///android_asset/office/test.docx")
//        var file = File("file://assets/office/test.docx")
        initOfficeTbs()
//        openFile()
    }
    
    /**
     * 判断 存储权限 没有权限需要申请权限
     *
     * 初始化 sdk 设置 licenceKey
     *
     * 判断 文件 浏览引擎是否 加载完成, 没有的话 -> 开始加载; 加载完成 -> 打开文件
     */
    private fun initOfficeTbs() {
//        TbsFileInterfaceImpl.setLicenseKey("+UVZp26Bdbp6q8gxOKsLKjbJCMMyp3DkiBCYSp4APt0=")
//        if (!TbsFileInterfaceImpl.isEngineLoaded()) {
//            TbsFileInterfaceImpl.initEngineAsync(this@OfficeActivity, this)
//        } else {
//            openFile()
//        }
        openFile()
    }   
    
    private var isOpen = false
    
    /**
     * 获取到文件拓展名
     * 返回 "" 表示失败
     */
    private fun String.extName(): String {
        val splitList = this.split(".")
        return if (splitList.size > 1) {
            splitList[splitList.size - 1]
        } else {
            ""
        }
    }
    
    /**
     * 打开文件前的文件 路径校验
     */
    private fun openFile() {
        if (!TbsFileInterfaceImpl.isEngineLoaded()) {
//            TbsFileInterfaceImpl.initEngine(this)
            Log.d(TAG, "openFile: 插件未加载")
        }
        if (isOpen) return
        isOpen = true
        val filePath = intent.getStringExtra(EXTRA_FILEPATH) ?: ""
        Log.d(TAG, "openFile: $filePath")
        if (filePath == "") {
            // 没有文件名 直接关闭
            this.finish()
            return
        }
        openFileUseFileReader(filePath)
    }
    
    /**
     * 使用 SDK TbsFileInterfaceImpl 打开文件
     */
    private fun openFileUseFileReader(filePath: String) {
        // 设置参数
        val extName = filePath.extName()
        // 构建打开参数
        val param = Bundle()
        param.putString("filePath", filePath) //文件路径
        param.putString("fileExt", extName) // 文件后缀名，如文件名为test.pdf，则只需要传入"pdf"
        param.putString("tempPath", getExternalFilesDir("temp")!!.absolutePath)
        //调用 openFileReader 打开文档
        if (TbsFileInterfaceImpl.canOpenFileExt(extName)) { //tbs支持的文档类型
            TbsFileInterfaceImpl.getInstance()
            val ret: Int = TbsFileInterfaceImpl.getInstance()
                .openFileReader(this, param, this, viewBinding.container)
            if (ret != 0) {
                Log.d(TAG, "initView: ")
                Log.i(TAG, "openFileReader失败, ret = $ret")
            }
        } else {
            Log.d(TAG, "openFile: 不支持打开 $extName 文件")
        }
    }
    
    private fun openFileX5(filePath: String) {
        // 添加 X5Reader
        val x5ReaderView = TbsReaderView(this) { p0, p1, p2 ->
            run {
                Log.d(TAG, "onCallBackAction: $p0 $p1 $p2")
            }
        }
        viewBinding.container.removeAllViews()
        val viewParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        viewBinding.container.addView(x5ReaderView, viewParams)
        // 准备文件
        val extName = filePath.extName()
        val bundle = Bundle()
        bundle.putString(TbsReaderView.KEY_FILE_PATH, filePath)
        bundle.putString(TbsReaderView.KEY_TEMP_PATH, getExternalFilesDir("temp")!!.absolutePath)
        val result = x5ReaderView.preOpen(extName, false)
        Log.d(TAG, "openFileX5: $result")
        if (result) {
            x5ReaderView.openFile(bundle)
        }
    }
    
    /**
     * 使用其他软件了
     */
    private fun openFileUseX5(filePath: String) {
        val params = HashMap<String, String>()
        params["style"] = "1"
        params["local"] = "true"
        QbSdk.openFileReader(this, filePath, params) { p0 ->
            run {
                Log.d(TAG, "onReceiveValue: $p0")
            }
        }
    }
    
    override fun onCallBackAction(p0: Int?, p1: Any?, p2: Any?) {
        Log.d(TAG, "onCallBackAction: $p0 $p1 $p2")
        if (7002 == p0) {
            openFile()
        }
    }
    
    override fun onStop() {
        super.onStop()
        TbsFileInterfaceImpl.getInstance().closeFileReader()
    }
}