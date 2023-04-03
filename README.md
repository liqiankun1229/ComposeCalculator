# ComposeCalculator
    Logcat 新版使用使用 PIDs for the local app project: <br>
    package:mine
    Specific values:
    package:<package-ID> mine
    tag:< tag >
    Exclude a specific value by preceding the key with -:
        -tag:<exclude-tag>
    Use regular expressions with a given key by placing a ~ after the key:
    tag~:<regular-expression-tag>
    Combine with the exclude tag: -tag~:<exclude-regular-expression-tag>
    level:[ VERBOSE | INFO | ASSERT |DEBUG | WARN | ERROR ]
### 编译过程:
    aapt - 编译资源生成 R.java 文件
    aidl - 编译 aidl 文件 成 java 文件
    javac - 编译所有Java文件(R.java, aidl) 成 class 文件
    dex - 编译 class 文件 成 dex 文件
    apkbuilder - 编译 dex, values资源, 其他资源文件 生成 apk 文件
    apksigner/jarsigner - 使用 密钥 jks 对 apk 进行签名
    zipalign - 
### 工程信息
Groovy(kts 在拓展上( 两个 xx.gradle.kts之间的引用)有些瑕疵) + Kotlin + Java

### 模块
- app 启动看效果图
- ===============================
- lib_version 版本信息
- lib_base 基本框架 activity fragment dialog popupWindow
- lib_constant 项目常量存放
- lib_router 路由跳转 及 路由信息表
- lib_data 数据持久化 -> MMKV 引入并注释 + (SharedPreferences / DataStore)
- lib_net 网络请求 okhttp + retrofit + rx + 协程
- lib_permission 运行时权限
- lib_utils 工具类
- lib_picture 照片选择 依赖运行时权限框架
- lib_logger Log 的封装 log 样式 的修改
- lib_log_track 日志系统 封装 Log.xxx 格式化日志, 上传到服务器
- lib_camera 相机模块
- lib_zxing 二维码模块(剔除界面那种)
- ================================
- lib_web 网络封装 jsBridge 相关
- lib_flutter 封装 Flutter Channel 相关(类同 Web)
- ================================
- c_native c/c++ module (ndk开发)
- ================================
- local_plugin 本地开发 plugin 存放
- local_maven 本地开发 打出的 aar 包存放
- local_aar 合作方提供的aar文件
-
### MMKV ###
jni 使用动态注册方式, 在 java/kotlin 层 native 不会链接到 c++ 层 (相对安全, 但方法报错, 开发过程不太友好) <br>
mmkv 内存映射 零拷贝技术 ( 用户空间 内核空间) 有 数据丢失风险 用户登录信息还是用 SharedPreferences/ DataStore 保存
