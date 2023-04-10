import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';

class CustomAppBar extends StatelessWidget {
  String hotBookName = "牧神记";

  @override
  Widget build(BuildContext context) {
    double paddingTop = MediaQuery
        .of(context)
        .padding
        .top;
    return GestureDetector(
      onTap: () {
        showDialog(
            context: context,
            builder: (BuildContext context) {
              return AlertDialog(
                title: Text("Flutter"),
                content: Text("进行搜索了"),
                actions: [],
              );
            });
      },
      child: Container(
        constraints:
        BoxConstraints(maxHeight: MediaQuery
            .of(context)
            .size
            .height),
        margin: EdgeInsets.fromLTRB(20, paddingTop + 10, 20, 5),
        padding: EdgeInsets.fromLTRB(20, 7, 20, 7),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(19),
          color: Colors.brown,
        ),
        child: Row(
          children: <Widget>[
            Icon(
              Icons.search,
              color: Colors.white,
            ),
            Text(
              '$hotBookName',
              style: TextStyle(color: Colors.white),
            ),
            // Expanded(
            //   child: ,
            // ),
            Container(
              width: 1,
              height: 20,
              margin: EdgeInsets.only(right: 13, left: 13),
              decoration: BoxDecoration(color: Colors.white),
            ),
            Text.rich(
              TextSpan(
                text: "搜索书籍",
                style: TextStyle(fontSize: 13, color: Colors.white),
              ),
            ),
            // ElevatedButton(
            //   onPressed: () {
            //
            //   },
            //   child: Text(
            //     '书城',
            //     style: TextStyle(fontSize: 13, color: Colors.white),
            //   ),
            // ),
          ],
        ),
      ),
    );
  }
}
