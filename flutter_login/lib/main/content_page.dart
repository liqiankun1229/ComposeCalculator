import 'package:fcommon/main/custom_appbar.dart';
import 'package:fcommon/main/page/card_free.dart';
import 'package:fcommon/main/page/card_recommend.dart';
import 'package:fcommon/main/page/card_share.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'page/card_book.dart';

// ignore: must_be_immutable
class ContentPager extends StatefulWidget {
  final ValueChanged<int>? onPageChanged;
  final ContentPagerController? contentPagerController;

  ContentPager({Key? key, this.onPageChanged, this.contentPagerController})
      : super(key: key);

  @override
  State createState() {
    return ContentPagerState();
  }
}

class ContentPagerState extends State<ContentPager> {
  // 试图比例
  PageController _pageController = PageController(viewportFraction: 0.8);
  String showText = "";
  int _currentIndex = 0;

  static List<Color> _colors = [
    Colors.blue,
    Colors.red,
    Colors.deepOrange,
    Colors.teal
  ];

  @override
  void initState() {
    if (widget.contentPagerController != null) {
      widget.contentPagerController!._pageController = _pageController;
    }
    // _statusBar();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        // appBar
        CustomAppBar(),
        // pager
        Expanded(
          child: PageView(
            onPageChanged: widget.onPageChanged,
            controller: _pageController,
            children: <Widget>[
              _wrapItem(CardRecommend(), 0),
              _wrapItem(CardShare(), 0),
              _wrapItem(CardFree(), 0),
              _wrapItem(CardBook(), 0),
            ],
          ),
        ),
      ],
    );
  }

  /// 构造 page 布局
  ///
  _wrapItem(Widget widget, int index) {
    return Padding(
      padding: EdgeInsets.all(10),
      child: index == 0 ? widget : cardBuild(index),
    );
  }

  cardBuild(int index) {
    return Padding(
      padding: EdgeInsets.all(10),
      child: Container(
        decoration: BoxDecoration(color: _colors[index]),
        child: Text(
          '$showText \n 当前是第 $_currentIndex 个Tab',
          style: TextStyle(
            fontSize: 18,
            color: Colors.white,
          ),
        ),
      ),
    );
  }

  /// 沉浸式状态栏
  _statusBar() {
    // 黑色
    SystemUiOverlayStyle style = SystemUiOverlayStyle(
      systemNavigationBarColor: Color(0xFF000000),
      systemNavigationBarDividerColor: null,
      statusBarColor: Colors.transparent,
      systemNavigationBarIconBrightness: Brightness.light,
      statusBarIconBrightness: Brightness.dark,
      statusBarBrightness: Brightness.light,
    );
    SystemChrome.setSystemUIOverlayStyle(style);
  }
}

class ContentPagerController {
  PageController? _pageController;

  void jumpToPage(BuildContext context, int page, String showText) {
    showDialog(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            title: Text("Flutter"),
            content: SingleChildScrollView(
              child: Text(showText + double.infinity.toString()),
            ),
            actions: [
              TextButton(
                  onPressed: () {
                    Navigator.pop(context);
                  },
                  child: Text("确定")),
              TextButton(
                  onPressed: () {
                    Navigator.pop(context);
                  },
                  child: Text("取消"))
            ],
          );
        });
    _pageController?.jumpToPage(page);
  }
}
