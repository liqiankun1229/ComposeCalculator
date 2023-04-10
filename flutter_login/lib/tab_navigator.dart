import 'dart:async';

import 'package:fcommon/main/content_page.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class TabNavigator extends StatefulWidget {
  String? title = "";

  TabNavigator({Key? key, this.title}) : super(key: key);

  @override
  State createState() {
    return TabNavigatorState();
  }
}

class TabNavigatorState extends State<TabNavigator> {
  final _defaultColor = Colors.grey;
  final _activeColor = Colors.blue;
  int _currentIndex = 0;

  int _counter = 0;
  String showText = "0";

  //<editor-fold desc="Native -> Flutter">

  // flutter 通道 native -> flutter
  static const callFlutter = const EventChannel("plugin_call_flutter");

  /// 订阅
  StreamSubscription? _subscription;

  _onEvent(Object? object) {
    if (object != null) {
      print("在 Flutter 打印:_onEvent:" + object.toString());
      // 转换成 json 字符串
      // 转换成 hashMap
      // 提取 方法签名(确保唯一) 重复的时候覆盖
      //
      setState(() {
        showDialog(
            context: context,
            builder: (BuildContext context) {
              return AlertDialog(
                title: Text("Flutter"),
                content: Text(object.toString()),
                actions: [],
              );
            });
      });
    } else {
      setState(() {
        showText = "无数据";
      });
    }
  }

  _onError(Object desc, StackTrace stackTrace) {
    /// String code, String msg,
    /// print("在 Flutter 打印:_onError:" + code + ":" + msg + ":" + desc);
    print("在 Flutter 打印:_onError:" + desc.toString());
    setState(() {
      showDialog(
          context: context,
          builder: (BuildContext context) {
            return AlertDialog(
              title: Text("Flutter"),
              content: Text(desc.toString()),
              actions: [],
            );
          });
    });
  }

  //</editor-fold>

  static const callNativeLogin = const MethodChannel("com.lqk.ku/login");

  /// 对象插入树 时 (生命周期方法 attach)
  /// 订阅事件
  @override
  void initState() {
    receiveMessage();
    if (_subscription == null) {
      _subscription = callFlutter
          .receiveBroadcastStream()
          // .listen(_onEvent);
          .listen(_onEvent, onError: _onError);
    }
    super.initState();
  }

  /// 取消订阅
  @override
  void dispose() {
    super.dispose();
    _subscription = null;
  }

  //<editor-fold desc="消息渠道 login_message">

  // 消息
  static const msgPlugin =
      const BasicMessageChannel("login_message", JSONMessageCodec());

  /// 发送消息 flutter 主动给原生 发送 有返回
  Future<Null> sendMessage() async {
    Object? reply = await msgPlugin.send("Flutter Message");
    if (reply != null) {
      print("Flutter:发送消息的返回值:" + reply.toString());
      setState(() {
        showText = reply.toString();
      });
    } else {
      setState(() {
        showText = "发送失败";
      });
    }
  }

  /// 接收消息
  void receiveMessage() {
    print("设置接受信息");
    msgPlugin.setMessageHandler((msg) async {
      if (mContext != null) {
        if (msg != null) {
          print("显示信息");
          setState(() {
            showDialog(
                context: mContext!,
                builder: (BuildContext context) {
                  return AlertDialog(
                    title: Text("原生过来的消息"),
                    content: Text("这是消息内容:" + msg.toString()),
                  );
                });
            showText = msg.toString();
          });
        } else {
          showText = "无数据";
        }
      }
      return msg;
    });
  }

//</editor-fold>

//<editor-fold desc="Flutter -> Native">

// flutter 通道 flutter -> native
  static const callNative = const MethodChannel("plugin_call_native");

  /// Flutter -> Native 发送消息
  Future<Null> _callNativeLogin(String method) async {
    String string = await callNative.invokeMethod(method);
    setState(() {
      showText = string;
    });
  }

  /// Flutter -> Native 方法 无参
  Future<Null> _callNativeMethod(String method) async {
    String string = await callNative.invokeMethod(method);
    setState(() {
      showText = string;
    });
  }

  /// Flutter -> Native 传参 123
  Future<Null> _jumpToNative() async {
    String string = await callNative.invokeMethod('oneAct', "123");
    setState(() {
      showText = string;
      print(string);
    });
  }

  /// Flutter -> Native json
  /// 发送 json 字符串
  Future<Null> _jumpToNativeWithValue() async {
    Map<String, String> map = {
      "data":
          "{\"status\": 200, \"message\": \"Login Succeed\", \"data\": {\"name\": \"LQK\", \"mobile\": \"18106899660\",\"sex\":{\"s\":\"男\"}}}"
    };
    String string = await callNative.invokeMethod("twoAct", map);
    setState(() {
      showText = string;
    });
  }

//</editor-fold>

  /// floatButton 点击事件
  void _incrementCounter() {
    setState(() {
      // This call to setState tells the Flutter framework that something has
      // changed in this State, which causes it to rerun the build method below
      // so that the display can reflect the updated values. If we changed
      // _counter without calling setState(), then the build method would not be
      // called again, and so nothing would appear to happen.
      _counter++;
      showText = _counter.toString();
    });
    if (_counter == 3) {
      _jumpToNative();
    }
    if (_counter == 4) {
      print("发送信息");
      sendMessage();
    }
    if (_counter == 5) {
      _jumpToNativeWithValue();
    }
    if (_counter == 6) {
      _callNativeLogin("jumpLogin");
    }
    if (_counter == 7) {
      _callNativeMethod("jumpMethod");
    }
    if (_counter == 10) {
      _counter = 0;
    }
  }

// pager 控制器 监听 page 的变化
  ContentPagerController contentPagerController = ContentPagerController();

  BuildContext? mContext;

  @override
  Widget build(BuildContext context) {
    mContext = context;
    return Scaffold(
      body: Container(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            colors: [
              Colors.white,
              Colors.white70,
            ],
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
          ),
        ),
        child: ContentPager(
          onPageChanged: (int index) {
            setState(() {
              _currentIndex = index;
            });
          },
          contentPagerController: contentPagerController,
        ),
      ),
      bottomNavigationBar: BottomNavigationBar(
        type: BottomNavigationBarType.fixed,
        currentIndex: _currentIndex,
        onTap: (index) {
          setState(() {
            contentPagerController.jumpToPage(context, index, showText);
            _currentIndex = index;
          });
        },
        items: [
          _bottomBuild("首页", Icons.home, 0),
          _bottomBuild("分类", Icons.category, 1),
          _bottomBuild("生活", Icons.filter_drama, 2),
          _bottomBuild("首页", Icons.people, 3),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: Text(
          _counter.toString(),
          style: TextStyle(
            fontSize: 18,
            color: Colors.white,
          ),
        ),
      ),
    );
  }

  _bottomBuild(String title, IconData icon, int index) {
    return BottomNavigationBarItem(
      icon: Icon(
        icon,
        color: _defaultColor,
      ),
      activeIcon: Icon(
        icon,
        color: _activeColor,
      ),
      label: title,
      // title: Text(
      //   title,
      //   style: TextStyle(
      //       color: _currentIndex != index ? _defaultColor : _activeColor),
      // ),
    );
  }
}
