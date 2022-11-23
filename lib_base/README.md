## 项目基本框架 MVC MVP MVVM MVI
***
#### BaseActivity
    Activity 活动页面基类 用于 页面展示
#### BaseFragment
    Fragment 碎片布局基类 碎片展示, 依赖与 BaseActivity
    复用展示不同界面 Activity 中重复的局部 UI
    区别单个界面 不同的碎片内容(其他为重复单一布局)
#### BaseDialog
    Dialog 对话框基类, 用于提示/加载动画等 交互UI
#### BaseWindow