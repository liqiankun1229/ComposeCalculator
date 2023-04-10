import 'package:fcommon/main/page/base_card.dart';
import 'package:flutter/material.dart';

/// 本周推荐
class CardRecommend extends BaseCard {
  @override
  CardRecommendState createState() => CardRecommendState();
}

class CardRecommendState extends BaseCardState {
  @override
  void initState() {
    // 子标题颜色赋值
    subTitleColor = Colors.black12;
    super.initState();
  }

  @override
  topTitle(String subTitle) {
    return super.topTitle("本周推荐");
  }

  @override
  subTitle(String subTitle) {
    return super.subTitle("无限卡, 全场免费读");
  }

  @override
  bottomContent() {
    return Expanded(
      child: Container(
        constraints: BoxConstraints(
            maxHeight: double.infinity, maxWidth: double.infinity),
        margin: EdgeInsets.only(top: 20),
        child: Image.network(
          "http://t8.baidu.com/it/u=1484500186,1503043093&fm=79&app=86&f=JPEG?w=1280&h=853",
          fit: BoxFit.cover,
        ),
      ),
    );
  }
}
