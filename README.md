# ComposeCalculator

Logcat 新版使用使用
PIDs for the local app project: <br>
package:mine <br>
Specific values: <br>
package:<package-ID> mine <br>
tag:< tag > <br>
Exclude a specific value by preceding the key with -: <br>
-tag:<exclude-tag>  <br>
Use regular expressions with a given key by placing a ~ after the key: <br>
tag~:<regular-expression-tag>  <br>
Combine with the exclude tag: -tag~:<exclude-regular-expression-tag>  <br>
level:[VERBOSE | INFO | ASSERT |DEBUG | WARN | ERROR ]



### 模块
- app - 启动看效果图
- lib_data 数据持久化 -> MMKV 引入并注释
- lib_net 网络请求


### MMKV ###
jni 使用动态注册方式 ,在 java/kotlin 层 native 不会链接到 c++ 层 (报错) <br>
mmkv 内存映射 零拷贝技术 ( 用户空间 内核空间)
