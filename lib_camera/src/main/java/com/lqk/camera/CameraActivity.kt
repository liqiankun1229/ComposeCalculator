package com.lqk.camera

import android.Manifest
import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.OutputFileResults
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.video.VideoCapture
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.lqk.base.activity.viewbinding.BaseVBActivity
import com.lqk.camera.databinding.ActivityCameraBinding
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author LQK
 * @time 2022/11/26 22:26
 *
 */
// 别名
typealias LumaListener = (luma: Double) -> Unit

class CameraActivity : BaseVBActivity<ActivityCameraBinding>() {

    companion object {
        const val TAG = "CameraActivity"
    }

    override fun layoutId(): Int = R.layout.activity_camera

    override fun initViewBinding(): ActivityCameraBinding = ActivityCameraBinding.inflate(layoutInflater)

    private lateinit var cameraExecutorService: ExecutorService
    var imageCapture: ImageCapture? = null
    var videoCapture: VideoCapture<Recorder>? = null
    var recording: Recording? = null

    override fun initView() {
        super.initView()
        // 获取权限 相机

        // 开始预览
        cameraExecutorService = Executors.newSingleThreadExecutor()
        startCamera()
    }

    override fun initListener() {
        super.initListener()
        viewBinding.btnTakePhoto.setOnClickListener {
            takePhoto()
        }
        viewBinding.btnTakeVideo.setOnClickListener {
            takeVideo()
        }
    }

    /**
     * 开始预览
     */
    private fun startCamera() {
        imageCapture = ImageCapture.Builder().build()
        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
            .build()
        videoCapture = VideoCapture.withOutput(recorder)

        val imageAnalysis = ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(cameraExecutorService, LuminosityAnalyzer { luma ->
                    Log.d(TAG, "startCamera: $luma")
                })
            }

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    // 进行关联
                    it.setSurfaceProvider(viewBinding.camera.surfaceProvider)
                }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                // 简单拍照
//                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                // 结合图片分析
//                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalysis)
                // 视频
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val name = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            // Android 9
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: OutputFileResults) {
                    // 保存
                    Log.d(TAG, "onImageSaved: ${outputFileResults.savedUri}")
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.d(TAG, "onError: ${exception.message}")
                }
            }
        )

    }

    private fun takeVideo() {
        val videoCapture = this.videoCapture ?: return
        // 开始拍摄 按钮功能变化
        viewBinding.btnTakeVideo.isEnabled = true
        val curRecorder = recording
        if (curRecorder != null) {
            curRecorder.stop()
            recording = null
            return
        }
        val name = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
            }
        }
        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()
        recording = videoCapture.output
            .prepareRecording(this, mediaStoreOutputOptions)
            .apply {
                if (PermissionChecker.checkSelfPermission(
                        this@CameraActivity,
                        Manifest.permission.RECORD_AUDIO
                    ) == PermissionChecker.PERMISSION_GRANTED
                ) {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(this)) { videoRecordEvent ->
                when (videoRecordEvent) {
                    is VideoRecordEvent.Start -> {
                        viewBinding.btnTakeVideo.apply {
                            text = "停止"
                            isEnabled = true
                        }
                    }
                    is VideoRecordEvent.Finalize -> {
                        if (!videoRecordEvent.hasError()) {
                            Log.d(TAG, "takeVideo: ${videoRecordEvent.outputResults.outputUri}")
                        } else {
                            recording?.close()
                            recording = null
                        }
                        viewBinding.btnTakeVideo.apply {
                            text = "开始录制"
                            isEnabled = true
                        }
                    }
                }
            }
    }

    private class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()
            val data = ByteArray(remaining())
            get(data)
            return data
        }

        override fun analyze(image: ImageProxy) {
            val buffer = image.planes[0].buffer
            val data = buffer.toByteArray()
            val pixels = data.map {
                it.toInt() and 0xFF
            }
            val luma = pixels.average()
            listener(luma)
            image.close()
        }
    }
}