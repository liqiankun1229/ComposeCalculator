package com.lqk.chttp

class NativeLib {

    /**
     * A native method that is implemented by the 'chttp' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'chttp' library on application startup.
        init {
            System.loadLibrary("chttp")
        }
    }
}