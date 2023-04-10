import 'dart:ffi';

import 'package:fcommon/main/page/base_card.dart';
import 'package:flutter/material.dart';

class CardFree extends BaseCard {
  @override
  BaseCardState createState() => CardFreeState();
}

const BOOK_LIST = [
  {
    'title': "牧神记",
    'img':
        "http://t9.baidu.com/it/u=4169540006,4220376401&fm=79&app=86&f=JPEG?w=1000&h=1500",
    'price': "20.0"
  },
  {
    'title': "剑来",
    'img':
        "http://t9.baidu.com/it/u=4169540006,4220376401&fm=79&app=86&f=JPEG?w=1000&h=1500",
    'price': "20.0"
  },
  {
    'title': "斗罗大陆",
    'img':
        "http://t9.baidu.com/it/u=4169540006,4220376401&fm=79&app=86&f=JPEG?w=1000&h=1500",
    'price': "20.0"
  },
  {
    'title': "完美世界",
    'img':
        "http://t9.baidu.com/it/u=4169540006,4220376401&fm=79&app=86&f=JPEG?w=1000&h=1500",
    'price': "20.0"
  },
  {
    'title': "万相之王",
    'img':
        "http://t9.baidu.com/it/u=4169540006,4220376401&fm=79&app=86&f=JPEG?w=1000&h=1500",
    'price': "20.0"
  },
  {
    'title': "Android",
    'img':
        "http://t9.baidu.com/it/u=4169540006,4220376401&fm=79&app=86&f=JPEG?w=1000&h=1500",
    'price': "20.0"
  },
  {
    'title': "iOS",
    'img':
        "http://t9.baidu.com/it/u=4169540006,4220376401&fm=79&app=86&f=JPEG?w=1000&h=1500",
    'price': "20.0"
  },
  {
    'title': "Flutter",
    'img':
        "http://t9.baidu.com/it/u=4169540006,4220376401&fm=79&app=86&f=JPEG?w=1000&h=1500",
    'price': "20.0"
  }
];

class CardFreeState extends BaseCardState {
  @override
  topTitle(String topTitle) {
    return super.topTitle("免费听书馆");
  }

  @override
  subTitle(String subTitle) {
    return super.subTitle("第 146 期");
  }

  /// 获取需要展示书籍的行数
  bookLength() {
    int length = 0;
    length = length + BOOK_LIST.length ~/ 3;
    if (BOOK_LIST.length % 3 == 0) {
      // 整除 后不用加 1
    } else {
      length += 1;
    }
    print(length);
    return length.toDouble();
  }

  @override
  bottomContent() {
    return Expanded(
      child: Container(
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Container(
                alignment: Alignment.center,
                padding: EdgeInsets.fromLTRB(8, 8, 8, 8),
                decoration: BoxDecoration(color: Colors.transparent),
                child: Container(
                  height: 100,
                  decoration: BoxDecoration(
                    boxShadow: [
                      BoxShadow(
                        color: Colors.grey,
                        blurRadius: 2,
                        offset: Offset(0, 5),
                      ),
                    ],
                  ),
                  child: Image.network(
                    "http://t9.baidu.com/it/u=583874135,70653437&fm=79&app=86&f=JPEG?w=3607&h=2408",
                    height: 100,
                  ),
                ),
              ),
              Container(
                constraints: BoxConstraints(
                  maxHeight: bookLength() * 155,
                ),
                child: bookList(),
              ),
              Container(
                constraints: BoxConstraints(
                  minWidth: double.infinity,
                  minHeight: 40,
                ),
                padding: EdgeInsets.only(bottom: 5, top: 5),
                child: bottomBtn(),
              ),
            ],
          ),
        ),
      ),
    );
    // return Expanded(
    //   child: ,
    // );
  }

  Widget _item(Map<String, String> item) {
    return SizedBox(
      child: Column(
        verticalDirection: VerticalDirection.down,
        mainAxisAlignment: MainAxisAlignment.start,
        mainAxisSize: MainAxisSize.max,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          // 绝对布局
          GestureDetector(
            onTap: () {
              showDialog(
                  context: context,
                  builder: (BuildContext context) {
                    return AlertDialog(
                      title: Text("免费听书馆"),
                      content: Text("查看书籍:${item["title"]}"),
                      actions: [],
                    );
                  });
            },
            child: Stack(
              alignment: AlignmentDirectional.center,
              children: <Widget>[
                Container(
                  child: Image.network(
                    "${item['img']}",
                    fit: BoxFit.cover,
                  ),
                ),
                Container(
                  width: 30,
                  height: 30,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(15),
                    color: Colors.black26,
                  ),
                  child: Icon(
                    Icons.play_circle_outline,
                    color: Colors.white,
                  ),
                ),
                Positioned(
                  child: Container(
                    padding: EdgeInsets.all(3),
                    decoration: BoxDecoration(color: Colors.black54),
                    child: Text(
                      "原价: ${item['price']}",
                      style: TextStyle(color: Colors.white),
                    ),
                  ),
                  bottom: 0,
                  left: 0,
                  right: 0,
                ),
              ],
            ),
          ),
          Text(
            "${item["title"]}",
            style: TextStyle(fontSize: 12, color: Colors.black54),
            softWrap: true,
          ),
        ],
      ),
    );
  }

  bookList() {
    return GridView.count(
      physics: const NeverScrollableScrollPhysics(),
      // 每行 item 数量
      crossAxisCount: 3,
      // 垂直间隔
      mainAxisSpacing: 0,
      // 水平间隔
      crossAxisSpacing: 5,
      // 宽高比
      childAspectRatio: 5 / 9,
      padding: EdgeInsets.only(left: 20, right: 20),
      children: BOOK_LIST.map((item) {
        return _item(item);
      }).toList(),
    );
  }

  bottomBtn() {
    return Container(
      padding: EdgeInsets.only(left: 20, right: 20),
      child: ElevatedButton(
        style: ButtonStyle(
            backgroundColor:
                MaterialStateProperty.resolveWith((states) => Colors.red),
            overlayColor: MaterialStateProperty.resolveWith(
                (states) => Colors.blueAccent),
            foregroundColor:
                MaterialStateProperty.resolveWith((states) => Colors.red),
            padding: MaterialStateProperty.resolveWith(
                (states) => EdgeInsets.symmetric(horizontal: 40)),
            shape: MaterialStateProperty.resolveWith((states) =>
                RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(30.0)))),
        onPressed: () {
          showDialog(
              context: context,
              builder: (BuildContext context) {
                return AlertDialog(
                  title: Text("Flutter"),
                  actions: [],
                );
              });
        },
        child: Text(
          "免费领取",
          style: TextStyle(color: Colors.white, fontSize: 24),
        ),
      ),
    );
    // return FractionallySizedBox(
    //   widthFactor: 1,
    //   child: ,
    // );
  }
}
