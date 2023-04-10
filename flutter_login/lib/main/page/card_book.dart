import 'package:fcommon/main/page/base_card.dart';
import 'package:fcommon/user/login/login.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';

class CardBook extends BaseCard {
  @override
  BaseCardState createState() {
    return CardBookState();
  }
}

class CardBookState extends BaseCardState {
  @override
  topContent() {
    return ListView(
      scrollDirection: Axis.vertical,
      shrinkWrap: true,
      children: <Widget>[
        Container(
          padding: EdgeInsets.fromLTRB(66, 10, 66, 30),
          child: InkWell(
            child: SvgPicture.asset(
              "img/svg/logo_wechat.svg",
            ),
            onTap: () {
              Navigator.of(context)
                  .push(MaterialPageRoute(builder: (context) => Login()));
            },
          ),
        ),
        Container(
          padding: EdgeInsets.only(left: 20, top: 15, bottom: 15, right: 20),
          decoration: BoxDecoration(color: Colors.black12),
          child: Row(
            children: <Widget>[
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text.rich(
                    TextSpan(
                      children: const <TextSpan>[
                        TextSpan(
                          text: "书名: ",
                          style: TextStyle(fontSize: 24, color: Colors.black54),
                        ),
                        TextSpan(
                          text: "牧神记",
                          style: TextStyle(fontSize: 18, color: Colors.black26),
                        ),
                      ],
                    ),
                  ),
                  Text.rich(
                    TextSpan(
                      children: const <TextSpan>[
                        TextSpan(
                          text: "作者: ",
                          style: TextStyle(fontSize: 18, color: Colors.black54),
                        ),
                        TextSpan(
                          text: "牧神记",
                          style: TextStyle(fontSize: 14, color: Colors.black26),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              Container(
                margin: EdgeInsets.only(left: 20),
                padding:
                    EdgeInsets.only(left: 10, right: 10, top: 5, bottom: 5),
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(20),
                  gradient:
                      LinearGradient(colors: [Colors.blue, Colors.lightBlue]),
                ),
                child: Text(
                  "分享领取书籍",
                  style: TextStyle(
                    color: Colors.white,
                  ),
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }

  @override
  bottomContent() {
    return Expanded(
      child: Container(
        child: SingleChildScrollView(
          child: Column(
            children: <Widget>[
              Container(
                // constraints: BoxConstraints(
                //     maxHeight: MediaQuery.of(context).size.height - 100),
                padding: EdgeInsets.only(left: 20),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    Text(
                      "书籍描述",
                      overflow: TextOverflow.ellipsis,
                    ),
                    Text(
                      "1: ;\n"
                      "2: ;\n"
                      "3: 调用原生 plugin_call_native -> oneAct 方法;\n"
                      "4: 发送消息 login_message -> Flutter Send;\n"
                      "5: 发送个 json 字符串到原生;\n"
                      "6: 发送无参数事件 jumpLogin;\n"
                      "7: 发送无参数事件 jumpMethod;\n"
                      "8: ;\n"
                      "9: ;\n"
                      "10: ;\n"
                      "11: ;\n"
                      "12: ;\n"
                      "13: ;\n"
                      "14: ;\n"
                      "15: ;\n"
                      "16: ;\n"
                      "17: ;\n"
                      "18: ;\n"
                      "19: ;\n"
                      "20: ;\n"
                      "21: ;\n"
                      "22: ;\n"
                      "23: ;\n"
                      "24: ;\n"
                      "25: 归 0 重新开始循环;\n",
                      overflow: TextOverflow.ellipsis,
                    ),
                  ],
                ),
              ),
              footerTitle('更多好书免费领 >'),
            ],
          ),
        ),
      ),
    );
  }
}
