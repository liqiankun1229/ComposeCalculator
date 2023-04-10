import 'dart:collection';
import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:fcommon/config/configs.dart';

class DioUtil {
//  static const String base_debug = "https://test.iconntech.com/"; //政务域名外网 政务云测试
  static const String base_debug = ""; //政务域名外网 政务云测试
  static const String base_release = "https://app.hzbanshi.cn/"; //政务域名外网app 正式
  static String _baseUrl = base_debug;
  static const CONTENT_TYPE_JSON = "application/json";
  static const CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

  static Map optionParams = {
    "timeoutMs": 15000,
    "token": null,
    "autohorizationCode": null,
  };

  /// 设置 网络基址
  static setBaseUrl(String baseUrl) {
    _baseUrl = baseUrl;
  }

  static get(url, params) async {
    return await request(
        _baseUrl + url, params, new Map(), new Options(method: "GET"));
  }

  static post(url, params) async {
    return await request(_baseUrl + url, params, new Map(), new Options(method: "POST"));
  }

  static delete(url, params) async {}

  static put(url, params) async {}

  /// 发起网络请求
  static request(url, params, Map<String, String> header, Options option,
      {noTip = false}) async {
    // 没有网络
    // var connectivityResult = await (new Connectivity().checkConnectivity());
    // if (connectivityResult == ConnectivityResult.none) {
    //   return RequestData.netWorkError(Code.NETWORK_ERROR,
    //       Code.errorHandleFunction(Code.NETWORK_ERROR, "", noTip));
    // }
    // 请求头
    Map<String, String> headers = new HashMap();
    if (header != null) {
      headers.addAll(header);
    }
    // 授权码
    // if (optionParams["authorizationCode"] == null) {
    //   var authorizationCode = await getAuthorization();
    //   if (authorizationCode != null) {
    //     optionParams["authorizationCode"] = authorizationCode;
    //   }
    // }
    // headers["Authorization"] = optionParams["authorizationCode"];
    // 设置 baseUrl
    if (option != null) {
      option.headers = headers;
    } else {
      option = new Options(method: "GET");
      option.headers = headers;
    }
    // 超时
    option.receiveTimeout = 15000;
//    option.receiveTimeout = 15000;
//    option.responseType = ResponseType.stream;
    Dio dio = new Dio();
    // 添加拦截器
    if (Config.DEBUG) {
      dio.interceptors.add(InterceptorsWrapper(
        onRequest: (request, requestInterceptorHandler) {},
        onResponse: (response, responseInterceptorHandler) {},
        onError: (error, errorInterceptorHandler) {},
      ));
    }
    // Response response;
    // try {
    //   response = await dio.request(url, data: params, options: option);
    //   utf8.decoder;
    // } on DioError catch (e) {
    //   // 请求错误
    //   Response errorResponse;
    //   if (e.response != null) {
    //     errorResponse = e.response;
    //   } else {
    //     errorResponse = new Response(statusCode: 400, requestOptions: null);
    //   }
    //   if (e.type == DioErrorType.connectTimeout) {
    //     errorResponse.statusCode = Code.NETWORK_TIMEOUT;
    //   }
    //   if (Config.DEBUG) {
    //     print("请求异常: ${e.toString()}");
    //     print("请求异常 Url: $url");
    //   }
    //   return RequestData.netWorkError(errorResponse.statusCode,
    //       Code.errorHandleFunction(errorResponse.statusCode, e.message, noTip));
    // }
    // try {
    //   if (option.contentType != null && option.contentType == "text") {
    //     return new RequestData(
    //         status: Code.SUCCESS, message: "", data: response.data);
    //   }
    // } catch (e) {
    //   print("${e.toString()} $url");
    // }
  }

  /// 清除授权
  static clearAuthorization() {
    optionParams["authorizationCode"] = null;
    // SpUtil.remove(Config.KEY_TOKEN);
  }

  /// 获取授权 token
  static getAuthorization() async {
    // String token = SpUtil.getString(Config.KEY_TOKEN);
    // if (token == null) {
    //   String basic = SpUtil.getString(Config.USER_BASIC_CODE);
    //   if (basic == null) {
    //     // 提示输入 账号密码
    //   } else {
    //     return "Basic $basic";
    //   }
    // } else {
    //   optionParams["authorizationCode"] = token;
    //   return token;
    // }
  }
}
