# permissionX 框架
***

### 请求权限逻辑 ###
-> 判断有无对应权限 </br>
-> 没有权限需要请求的权限(普通权限-特殊权限)(是否需要再次请求) </br>
-> 显示请求权限的目的(自定义弹框) </br>
-> 请求权限(应用内请求(系统弹框) / 跳转设置的请求(自定义弹框)) </br>
-> 请求权限的回调 </br>
-> 请求失败后是否 (开启说明(自定义弹框) 跳转设置再次请求权限) </br>
-> 请求成功执行 需要相应权限的逻辑 </br>


### PermissionX 使用: ###

    PermissionX.init(activity)
        .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .explainReasonBeforeRequest()
        .onExplainRequestReason { scope, deniedList ->
            run {}
        }
        .onForwardToSettings { scope, deniedList ->
            run {}
        }
        .request { allGranted, grantedList, deniedList ->
            run {}
        }


传递请求的页面(记录, 回调) -> 返回 PermissionMediator (工厂, 用于创建 PermissionBuilder)
PermissionBuilder 保存调用时 需要的权限/ 调用的界面/ 回调/ 对话框/ 样式/ 
init(activity)
init(fragment)



构建一个请求权限的 task 链(RequestChain) 