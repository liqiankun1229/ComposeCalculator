import 'package:flutter/cupertino.dart';
import 'package:flutter_svg/flutter_svg.dart';

/// 实名认证 L2-1 L2-2
///
class RealName extends StatefulWidget {
  @override
  State createState() {
    return RealNameState();
  }
}

class RealNameState extends State<RealName> {
  /// 导航栏
  Widget toolbar() {
    return Row(
      children: [
        SvgPicture.asset(
          "img/svg/ic_back_black.svg",
          width: 48,
          height: 48,
        ),
        Expanded(
          child: Text(
            "身份证识别",
            style: TextStyle(fontSize: 18, color: Color(0xFF333333)),
          ),
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        toolbar(),
      ],
    );
  }
}
