import 'dart:async';
import 'dart:ui';

import 'package:fcommon/tab_navigator.dart';
import 'package:fcommon/user/login/login.dart';
import 'package:fcommon/utils/sp_helper.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

void main() {
  // 捕获 invokeMethod 异常信息
  runZonedGuarded(() {
    runApp(getRouter(window.defaultRouteName));
  }, (Object errorObject, StackTrace stackTrace) {
    print("========flutter=========");
    print(errorObject);
    print("=======================");
  });
}

// 启动路由, 可以根据路由名称切换 flutter 程序入口
// 根据路由名称可以配置不同的 flutter 程序
Widget getRouter(String name) {
  // 捕获系统异常
  FlutterError.onError = (FlutterErrorDetails errorDetails) {
    FlutterError.presentError(errorDetails);
    if (kReleaseMode) {
      print("退出");
    }
  };
  switch (name) {
    case 'login':
      print(name);
      return MyApp();
    case 'flutter_login_activity':
      print(name);
      return MyApp();
    default:
      return MyApp();
  }
}

//static const Method

class MyApp extends StatelessWidget {

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    // SPHelper.initSP();
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      // routes: {
      //   "/": (context) => TabNavigator(title: "首页"),
      //   "/login": (context) => Login(),
      // },
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or press Run > Flutter Hot Reload in a Flutter IDE). Notice that the
        // counter didn't reset back to zero; the application is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: TabNavigator(title: 'Flutter Demo Home Page'),
    );
  }
}
