// import 'package:shared_preferences/shared_preferences.dart';

/// 数据保存
///
// class SPHelper {
//   static SharedPreferences? _sharedPreferences;
//
//   static initSP() async {
//     if (_sharedPreferences == null) {
//       await SharedPreferences.getInstance();
//     }
//   }
//
//   static saveString(String key, String v) async {
//     await _sharedPreferences?.setString(key, v);
//   }
//
//   static String getString(String key, String defaultValue) {
//     String? retString = _sharedPreferences != null
//         ? _sharedPreferences!.getString(key)
//         : defaultValue;
//     return retString != null ? retString : defaultValue;
//   }
// }
