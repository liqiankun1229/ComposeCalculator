## 自定义 Plugin
官方文档地址(https://docs.gradle.org/current/userguide/custom_plugins.html)

### 三种方法
#### 1. 直接写在需要使用插件的 build.gradle 文件中

(1): 写在 build.gradle 文件 最前面

(2): (独立文件中 然后在build.gradle文件中使用 apply from:'xxx')  **PS:apply 不能写在 plugins{} 前面**

#### 2. 工程目录中的 buildSrc 特殊 module

buildSrc module 是工程最先执行的模块


#### 3. 自定义插件模块 发布到本地或远程, 然后引用
