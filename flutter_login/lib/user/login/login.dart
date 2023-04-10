import 'package:fcommon/widget/round_check_box.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:fluttertoast/fluttertoast.dart';

/// 验证码登录 / 账号密码登录
///
class Login extends StatefulWidget {
  @override
  State createState() {
    return LoginState();
  }
}

class LoginState extends State<Login> {
  // 登录类型 手机号验证码: 1 / 手机号密码 : 2 ; 默认 1
  int _loginType = 1;
  int loginType = 1;

  changeType() {
    loginType = (loginType == 1 ? 2 : 1);
    setState(() {
      _loginType = loginType;
    });
  }

  String loginTxt() {
    return _loginType == 1 ? "密码登录" : "验证码登录";
  }

  bool _agreement = false;
  bool agreement = false;

  setAgreement() {
    agreement = (agreement == true ? false : true);
    setState(() {
      _agreement = agreement;
      Fluttertoast.showToast(msg: "当前状态$_agreement");
    });
  }

  getAgreement() {
    return _agreement;
  }

  // 导航栏 返回键 + 切换的模式
  toolbar() {
    return Container(
      height: 48,
      width: double.infinity,
      child: Row(
        children: [
          GestureDetector(
            child: Padding(
              padding: EdgeInsets.fromLTRB(12, 0, 12, 0),
              child: SvgPicture.asset(
                "img/svg/ic_back_black.svg",
                width: 24,
                height: 24,
              ),
            ),
            onTap: () {
              Navigator.of(context).pop();
            },
          ),
          Container(
            width: 250,
          ),
          Container(
            width: 100,
            child: GestureDetector(
              child: Text(
                loginTxt(),
                textAlign: TextAlign.end,
                style: TextStyle(
                  fontSize: 17,
                  color: Color(0xFF999999),
                ),
              ),
              onTap: () {
                changeType();
              },
            ),
          ),
        ],
      ),
    );
  }

  late TapGestureRecognizer _gestureRecognizer1;
  late TapGestureRecognizer _gestureRecognizer2;
  late TapGestureRecognizer _gestureRecognizer3;

  @override
  void initState() {
    super.initState();
    _gestureRecognizer1 = TapGestureRecognizer()
      ..onTap = () {
        Fluttertoast.showToast(msg: "用户服务协议");
      };
    _gestureRecognizer2 = TapGestureRecognizer()
      ..onTap = () {
        Fluttertoast.showToast(msg: "市民卡平台个人信保护政策");
      };
    _gestureRecognizer3 = TapGestureRecognizer()
      ..onTap = () {
        Fluttertoast.showToast(msg: "市民账户服务协议");
      };
  }

  @override
  void dispose() {
    _gestureRecognizer1.dispose();
    _gestureRecognizer2.dispose();
    _gestureRecognizer3.dispose();
    super.dispose();
  }

  /// 验证码登录
  uiVerifyCode() {
    return Container(
      padding: EdgeInsets.only(left: 30, right: 30),
      child: Column(
        children: [
          // 手机号
          Stack(
            alignment: Alignment.centerRight,
            children: [
              TextField(
                cursorColor: Color(0xFF5EB867),
                keyboardType: TextInputType.phone,
                maxLength: 11,
                maxLines: 1,
                decoration: InputDecoration(
                  border: InputBorder.none,
                  counterText: "",
                  hintText: "请输入手机号",
                  hintStyle: TextStyle(
                    fontSize: 18,
                    color: Color(0xFFCCCCCC),
                  ),
                ),
              ),
              Container(
                child: GestureDetector(
                  onTap: () {
                    Fluttertoast.showToast(msg: "发送验证码");
                  },
                  child: Text(
                    "发送验证码",
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      fontSize: 18,
                      color: Color(0xffcccccc),
                    ),
                  ),
                ),
              ),
              // Container(
              //   alignment: Alignment.centerRight,
              //   child: ,
              // ),
            ],
          ),
          Container(
            height: 1,
            color: Color(0xFFCCCCCC),
          ),
          Container(
            height: 12,
          ),
          // 验证码
          TextField(
            cursorColor: Color(0xFF5EB867),
            cursorHeight: 24,
            keyboardType: TextInputType.text,
            maxLength: 6,
            textAlign: TextAlign.start,
            maxLines: 1,
            decoration: InputDecoration(
              border: InputBorder.none,
              counterText: "",
              hintText: "请输入验证码",
              hintStyle: TextStyle(
                fontSize: 18,
                color: Color(0xFFCCCCCC),
              ),
            ),
          ),
          Container(
            height: 1,
            color: Color(0xFFCCCCCC),
          ),
          Container(
            padding: EdgeInsets.only(top: 24),
            child: Row(
              children: [
                Stack(
                  alignment: Alignment.topLeft,
                  children: [
                    Container(
                      width: 28,
                      height: 40,
                    ),
                    Container(
                      width: 20,
                      height: 20,
                      padding: EdgeInsets.only(top: 1),
                      child: RoundCheckBox(
                        isChecked: getAgreement(),
                        isRound: true,
                        checkedWidget:
                            SvgPicture.asset("img/svg/ic_cb_selected.svg"),
                        uncheckedWidget:
                            SvgPicture.asset("img/svg/ic_cb_normal.svg"),
                        uncheckedColor: Color(0x00000000),
                        borderColor: Color(0x00000000),
                        checkedColor: Color(0x00000000),
                        onTap: (bool? v) {
                          setAgreement();
                        },
                      ),
                    ),
                  ],
                ),
                Expanded(
                  child: Text.rich(
                    TextSpan(
                      text: "我已阅读并同意",
                      children: <TextSpan>[
                        TextSpan(
                          text: "《用户服务协议》",
                          style: TextStyle(
                            color: Color(0xFF3DB443),
                          ),
                          recognizer: _gestureRecognizer1,
                        ),
                        TextSpan(
                          text: "《市民卡平台个人信保护政策》",
                          style: TextStyle(
                            color: Color(0xFF3DB443),
                          ),
                          recognizer: _gestureRecognizer2,
                        ),
                        TextSpan(
                          text: "《市民账户服务协议》",
                          style: TextStyle(
                            color: Color(0xFF3DB443),
                          ),
                          recognizer: _gestureRecognizer3,
                        ),
                      ],
                    ),
                    style: TextStyle(
                      fontSize: 16,
                      color: Color(0xFF999999),
                    ),
                  ),
                ),
              ],
            ),
          ),
          Container(
            margin: EdgeInsets.only(top: 40),
            height: 44,
            constraints: BoxConstraints(
              minWidth: double.infinity,
            ),
            child: ElevatedButton(
              style: ButtonStyle(
                backgroundColor: MaterialStateProperty.resolveWith(
                    (states) => Color(0xFF6BD463)),
                overlayColor: MaterialStateProperty.resolveWith(
                    (states) => Color(0xFF6BD463)),
                foregroundColor: MaterialStateProperty.resolveWith(
                    (states) => Color(0xFF6BD463)),
                padding: MaterialStateProperty.resolveWith(
                    (states) => EdgeInsets.symmetric(horizontal: 40)),
                shape: MaterialStateProperty.resolveWith(
                  (states) => RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(30.0),
                  ),
                ),
              ),
              onPressed: () {
                Fluttertoast.showToast(
                  msg: "登录",
                  gravity: ToastGravity.CENTER,
                );
              },
              child: Text(
                "登录",
                style: TextStyle(color: Colors.white, fontSize: 24),
              ),
            ),
          ),
        ],
      ),
    );
  }

  // 账号密码登录
  uiPassword() {
    return Container(
      padding: EdgeInsets.only(left: 30, right: 30),
      child: Column(
        children: [
          // 手机号
          TextField(
            cursorColor: Color(0xFF5EB867),
            keyboardType: TextInputType.phone,
            maxLength: 11,
            maxLines: 1,
            decoration: InputDecoration(
              border: InputBorder.none,
              counterText: "",
              hintText: "请输入手机号",
              hintStyle: TextStyle(
                fontSize: 18,
                color: Color(0xFFCCCCCC),
              ),
            ),
          ),
          Container(
            height: 1,
            color: Color(0xFFCCCCCC),
          ),
          Container(
            height: 12,
          ),
          // 密码
          TextField(
            cursorColor: Color(0xFF5EB867),
            keyboardType: TextInputType.text,
            maxLines: 1,
            decoration: InputDecoration(
              border: InputBorder.none,
              counterText: "",
              hintText: "请输入登录密码",
              hintStyle: TextStyle(
                fontSize: 18,
                color: Color(0xFFCCCCCC),
              ),
            ),
          ),
          Container(
            height: 1,
            color: Color(0xFFCCCCCC),
          ),
          // 相关协议
          Container(
            padding: EdgeInsets.only(top: 24),
            child: Row(
              children: [
                Stack(
                  alignment: Alignment.topLeft,
                  children: [
                    Container(
                      width: 28,
                      height: 40,
                    ),
                    Container(
                      width: 20,
                      height: 20,
                      padding: EdgeInsets.only(top: 1),
                      child: RoundCheckBox(
                        isChecked: getAgreement(),
                        isRound: true,
                        checkedWidget:
                            SvgPicture.asset("img/svg/ic_cb_selected.svg"),
                        uncheckedWidget:
                            SvgPicture.asset("img/svg/ic_cb_normal.svg"),
                        uncheckedColor: Color(0x00000000),
                        borderColor: Color(0x00000000),
                        checkedColor: Color(0x00000000),
                        onTap: (bool? v) {
                          setAgreement();
                        },
                      ),
                    ),
                  ],
                ),
                Expanded(
                  child: Text.rich(
                    TextSpan(
                      text: "我已阅读并同意",
                      children: <TextSpan>[
                        TextSpan(
                          text: "《用户服务协议》",
                          style: TextStyle(
                            color: Color(0xFF3DB443),
                          ),
                          recognizer: _gestureRecognizer1,
                        ),
                        TextSpan(
                          text: "《市民卡平台个人信保护政策》",
                          style: TextStyle(
                            color: Color(0xFF3DB443),
                          ),
                          recognizer: _gestureRecognizer2,
                        ),
                        TextSpan(
                          text: "《市民账户服务协议》",
                          style: TextStyle(
                            color: Color(0xFF3DB443),
                          ),
                          recognizer: _gestureRecognizer3,
                        ),
                      ],
                    ),
                    style: TextStyle(
                      fontSize: 16,
                      color: Color(0xFF999999),
                    ),
                  ),
                ),
              ],
            ),
          ),
          // 登录按钮
          Container(
            margin: EdgeInsets.only(top: 40, bottom: 20),
            height: 44,
            constraints: BoxConstraints(
              minWidth: double.infinity,
            ),
            child: ElevatedButton(
              style: ButtonStyle(
                backgroundColor: MaterialStateProperty.resolveWith(
                  (states) => Color(0xFF6BD463),
                ),
                overlayColor: MaterialStateProperty.resolveWith(
                  (states) => Color(0xFF6BD463),
                ),
                foregroundColor: MaterialStateProperty.resolveWith(
                  (states) => Color(0xFF6BD463),
                ),
                padding: MaterialStateProperty.resolveWith(
                  (states) => EdgeInsets.symmetric(horizontal: 30),
                ),
                shape: MaterialStateProperty.resolveWith(
                  (states) => RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(30.0),
                  ),
                ),
              ),
              onPressed: () {
                Fluttertoast.showToast(
                  msg: "登录",
                  gravity: ToastGravity.CENTER,
                );
              },
              child: Text(
                "登录",
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 24,
                ),
              ),
            ),
          ),
          // 忘记密码
          GestureDetector(
            onTap: () {
              Fluttertoast.showToast(msg: "忘记密码 前往重置");
            },
            child: Text(
              "忘记密码?",
              style: TextStyle(
                fontSize: 15,
                color: Color(0xFF999999),
              ),
            ),
          ),
        ],
      ),
    );
  }

  uiLoginType() {
    return _loginType == 1 ? uiVerifyCode() : uiPassword();
  }

  @override
  Widget build(BuildContext context) {
    return Material(
      child: Container(
        color: Color(0xffffffff),
        child: Column(
          children: [
            // 状态栏
            Container(
              height: MediaQuery.of(context).padding.top,
            ),
            // 头部 返回键 + 切换的模式
            toolbar(),
            // 标题
            Container(
              height: 100,
              width: double.infinity,
              child: Padding(
                padding: EdgeInsets.fromLTRB(30, 30, 0, 40),
                child: Text(
                  "手机快捷登录",
                  style: TextStyle(
                    color: Color(0xFF333333),
                    fontSize: 24,
                  ),
                ),
              ),
            ),
            // 中间部分 手机号验证码 / 手机号密码
            uiLoginType(),
            // 底部 三方登录
            Expanded(
              child: Stack(
                alignment: Alignment.bottomCenter,
                children: [
                  Container(
                    padding: EdgeInsets.only(
                      bottom: 50,
                    ),
                    child: GestureDetector(
                      child: SvgPicture.asset(
                        "img/svg/logo_wechat.svg",
                        width: 48,
                        height: 48,
                      ),
                      onTap: () {
                        Fluttertoast.showToast(msg: "微信登录");
                      },
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  } // Could not get package user id: /system/bin/run-as: package not debuggable: com.lqk.ku
}
