package com.lqk.web.weex;//package com.lqk.web.weex;
//
///**
// * @create LQK 2023/5/10 12:36
// */
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Environment;
//import android.text.TextUtils;
//import android.util.Log;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.NotificationCompat;
//import androidx.core.content.FileProvider;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONException;
//import com.alibaba.fastjson.JSONObject;
//import com.taobao.weex.annotation.JSMethod;
//import com.taobao.weex.bridge.JSCallback;
//import com.taobao.weex.common.Constants;
//import com.taobao.weex.common.WXModule;
//import com.tencent.smtt.sdk.WebView;
//import java.io.File;
//import java.text.ParsePosition;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///* loaded from: classes.dex */
//public class CommonEvent extends WXModule {
//    private Bitmap bitmap;
//    private String type = "";
//    private String url = "";
//    private String title = "";
//    private String description = "";
//
//    @JSMethod
//    public void openScan(String msg) {
//        IntentIntegrator integrator = new IntentIntegrator(ActivityHelper.INSTANCE.getTopActivity());
//        integrator.setPrompt("请扫描");
//        integrator.setCameraId(0);
//        integrator.setBeepEnabled(false);
//        integrator.setCaptureActivity(ScanActivity.class);
//        integrator.initiateScan();
//    }
//
//    @JSMethod
//    public void getScanResult(String msg, JSCallback callback) {
//        ConstantHolder.scanResultJSCallback = callback;
//        IntentIntegrator integrator = new IntentIntegrator(ActivityHelper.INSTANCE.getTopActivity());
//        integrator.setPrompt("请扫描");
//        integrator.setCameraId(0);
//        integrator.setBeepEnabled(false);
//        integrator.setCaptureActivity(ScanActivity.class);
//        integrator.initiateScan();
//    }
//
//    @JSMethod
//    public void getLoginData(String msg, JSCallback callback) {
//        callback.invoke(SPHelper.getString(ActivityHelper.INSTANCE.getTopActivity(), LoginHelper.SP_LOGIN_INFO));
//    }
//
//    @JSMethod
//    public void getToken(String msg, JSCallback callback) {
//        callback.invoke(LoginHelper.INSTANCE.getLoginToken(ActivityHelper.INSTANCE.getTopActivity()));
//    }
//
//    @JSMethod
//    public void pickDate(String msg, final JSCallback callback) {
//        if (!AppUtils.isEmpty(msg)) {
//            try {
//                String pickDate = JSONObject.parseObject(msg).getString("pickDate");
//                List<Integer> list = new ArrayList<>();
//                list.add(Integer.valueOf(Integer.parseInt(pickDate.substring(0, 4))));
//                list.add(Integer.valueOf(Integer.parseInt(pickDate.substring(4, 6))));
//                list.add(Integer.valueOf(Integer.parseInt(pickDate.substring(6, 8))));
//                DatePickerDialog.Builder builder = new DatePickerDialog.Builder(ActivityHelper.INSTANCE.getTopActivity());
//                builder.setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.1
//                    @Override // com.hzbankwealth.harmony.view.picker.DatePickerDialog.OnDateSelectedListener
//                    public void onDateSelected(int[] dates) {
//                        Object obj;
//                        Object obj2;
//                        StringBuilder sb = new StringBuilder();
//                        sb.append(dates[0]);
//                        sb.append("-");
//                        if (dates[1] > 9) {
//                            obj = Integer.valueOf(dates[1]);
//                        } else {
//                            obj = "0" + dates[1];
//                        }
//                        sb.append(obj);
//                        sb.append("-");
//                        if (dates[2] > 9) {
//                            obj2 = Integer.valueOf(dates[2]);
//                        } else {
//                            obj2 = "0" + dates[2];
//                        }
//                        sb.append(obj2);
//                        callback.invoke(sb.toString());
//                    }
//
//                    @Override // com.hzbankwealth.harmony.view.picker.DatePickerDialog.OnDateSelectedListener
//                    public void onCancel() {
//                    }
//                }).setSelectYear(list.get(0).intValue() - 1).setSelectMonth(list.get(1).intValue() - 1).setSelectDay(list.get(2).intValue() - 1);
//                builder.create().show();
//            } catch (JSONException e) {
//                Logger.m7119ex(e);
//            }
//        }
//    }
//
//    @JSMethod
//    public void pickTime(String msg, final JSCallback callback) {
//        try {
//            if (!AppUtils.isEmpty(msg)) {
//                new TimePickerDialog.Builder(ActivityHelper.INSTANCE.getTopActivity(), JSONObject.parseObject(msg).getString("pickTime")).setOnTimeSelectedListener(new TimePickerDialog.OnTimeSelectedListener() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.2
//                    @Override // com.hzbankwealth.harmony.view.picker.TimePickerDialog.OnTimeSelectedListener
//                    public void onTimeSelected(int[] times) {
//                        Object obj;
//                        Object obj2;
//                        JSCallback jSCallback = callback;
//                        StringBuilder sb = new StringBuilder();
//                        if (times[0] < 10) {
//                            obj = "0" + times[0];
//                        } else {
//                            obj = Integer.valueOf(times[0]);
//                        }
//                        sb.append(obj);
//                        sb.append(Constants.COLON_SEPARATOR);
//                        if (times[1] < 10) {
//                            obj2 = "0" + times[1];
//                        } else {
//                            obj2 = Integer.valueOf(times[1]);
//                        }
//                        sb.append(obj2);
//                        jSCallback.invoke(sb.toString());
//                    }
//                }).create().show();
//            }
//        } catch (Exception e) {
//            Logger.m7119ex(e);
//        }
//    }
//
//    @JSMethod
//    public void pickDateAndTime(String msg, final JSCallback callback) {
//        try {
//            DatePickDialog dialog = new DatePickDialog(ActivityHelper.INSTANCE.getTopActivity());
//            dialog.setYearLimt(5);
//            dialog.setTitle("选择时间");
//            dialog.setType(DateType.TYPE_YMDHM);
//            dialog.setMessageFormat("yyyyMMdd HH:mm");
//            dialog.setOnChangeLisener(null);
//            dialog.setOnSureLisener(new OnSureLisener() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.3
//                @Override // com.hzbankwealth.harmony.view.widget.OnSureLisener
//                public void onSure(Date date) {
//                    callback.invoke(CommonEvent.this.dateToStrLong(date));
//                }
//            });
//            dialog.setStartDate(strToDateLong(JSONObject.parseObject(msg).getString("pickDate")));
//            dialog.show();
//        } catch (Exception e) {
//            Logger.m7119ex(e);
//        }
//    }
//
//    @JSMethod
//    public void datePick(String msg, final JSCallback callback) {
//        int startY = 0;
//        try {
//            int curY = Calendar.getInstance().get(1);
//            if (!TextUtils.isEmpty(msg)) {
//                startY = Integer.parseInt(JSONObject.parseObject(msg).getString(Constants.Name.f9146Y));
//            }
//            DatePickDialog dialog = new DatePickDialog(ActivityHelper.INSTANCE.getTopActivity());
//            dialog.setYearLimt(curY - startY);
//            dialog.setTitle("选择时间");
//            dialog.setType(DateType.TYPE_YM);
//            dialog.setOnChangeLisener(null);
//            dialog.setOnSureLisener(new OnSureLisener() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.4
//                @Override // com.hzbankwealth.harmony.view.widget.OnSureLisener
//                public void onSure(Date date) {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("pickDate", (Object) CommonEvent.this.dateMonthToStr(date));
//                    callback.invoke(jsonObject);
//                }
//            });
//            dialog.setStartDate(new Date());
//            dialog.show();
//        } catch (Exception e) {
//            Logger.m7119ex(e);
//        }
//    }
//
//    /* JADX INFO: Access modifiers changed from: private */
//    public String dateToStrLong(Date dateDate) {
//        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(dateDate);
//    }
//
//    /* JADX INFO: Access modifiers changed from: private */
//    public String dateMonthToStr(Date dateDate) {
//        return new SimpleDateFormat("yyyyMM").format(dateDate);
//    }
//
//    private Date strToDateLong(String strDate) {
//        return new SimpleDateFormat("yyyyMMdd HH:mm").parse(strDate, new ParsePosition(0));
//    }
//
//    @JSMethod
//    public void getReadContact(String msg, JSCallback callback) {
//        try {
//            callback.invoke(ContactHelper.INSTANCE.queryReadContact());
//        } catch (Exception e) {
//            Logger.m7120e(e.getMessage());
//        }
//    }
//
//    @JSMethod
//    public void filePreview(String msg, JSCallback callback) {
//        String url;
//        String type = null;
//        Activity top = ActivityHelper.INSTANCE.getTopActivity();
//        String urlPrefix = PropertiesHelper.INSTANCE.getConfigString(top, ConstantHolder.KEY_CONTACT_URL) + "/Harmony/downloaddetail/gongwendownload3?";
//        try {
//            JSONObject jsonObject = JSONObject.parseObject(msg);
//            url = urlPrefix + "ids=" + jsonObject.getString("ids") + "&attachtype=" + jsonObject.getString("attachtype") + "&mobileuserid=" + jsonObject.getString("mobileuserid");
//            type = jsonObject.getString("attachtype");
//        } catch (Exception e) {
//            url = urlPrefix + msg;
//        }
//        if (!AppUtils.isEmpty(url)) {
//            new FilePreviewHelper().showFile(top, url, type);
//        }
//    }
//
//    @JSMethod
//    public void documentFilePreview(String msg, JSCallback callback) {
//        String url;
//        Logger.m7122d("documentFilePreview msg: " + msg);
//        String type = null;
//        Activity top = ActivityHelper.INSTANCE.getTopActivity();
//        String urlPrefix = PropertiesHelper.INSTANCE.getConfigString(top, ConstantHolder.KEY_CONTACT_URL) + "/Harmony/downloaddetail/gongwendownload?";
//        try {
//            JSONObject jsonObject = JSONObject.parseObject(msg);
//            url = urlPrefix + "logos=" + jsonObject.getString("logos") + "&mobileuserid=" + jsonObject.getString("mobileuserid") + "&docid=" + jsonObject.getString("docid") + "&textorcontent=" + jsonObject.getString("textorcontent") + "&seqid=" + jsonObject.getString("seqid") + "&act=" + jsonObject.getString("act");
//            type = jsonObject.getString("type");
//        } catch (Exception e) {
//            url = urlPrefix + msg;
//        }
//        if (!AppUtils.isEmpty(url)) {
//            new FilePreviewHelper().showFile(top, url, type);
//        }
//    }
//
//    @JSMethod
//    public void getOrganize(final String msg, final JSCallback callback) {
//        new Thread(new Runnable() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.5
//            @Override // java.lang.Runnable
//            public void run() {
//                try {
//                    long l0 = System.currentTimeMillis();
//                    String contact = ContactHelper.INSTANCE.queryOrganize(msg);
//                    long l1 = System.currentTimeMillis();
//                    Logger.m7122d("getOrganize time: " + (l1 - l0));
//                    callback.invoke(contact);
//                } catch (Exception e) {
//                    Logger.m7120e(e.getMessage());
//                }
//            }
//        }).start();
//    }
//
//    @JSMethod
//    public void getContactById(String msg, JSCallback callback) {
//        try {
//            callback.invoke(ContactHelper.INSTANCE.queryContactById(msg));
//        } catch (Exception e) {
//            Logger.m7120e(e.getMessage());
//        }
//    }
//
//    @JSMethod
//    public void contactSearch(String msg, JSCallback callback) {
//        try {
//            callback.invoke(ContactHelper.INSTANCE.contactSearch(msg));
//        } catch (Exception e) {
//            Logger.m7120e(e.getMessage());
//        }
//    }
//
//    @JSMethod
//    public void contactSearch2(String msg, JSCallback callback) {
//        try {
//            callback.invoke(ContactHelper.INSTANCE.contactSearch2(msg));
//        } catch (Exception e) {
//            Logger.m7120e(e.getMessage());
//        }
//    }
//
//    @JSMethod
//    public void searchMore(String msg, JSCallback callback) {
//        try {
//            callback.invoke(ContactHelper.INSTANCE.searchMore(msg));
//        } catch (Exception e) {
//            Logger.m7120e(e.getMessage());
//        }
//    }
//
//    @JSMethod
//    public void searchMore2(String msg, JSCallback callback) {
//        try {
//            callback.invoke(ContactHelper.INSTANCE.searchMore2(msg));
//        } catch (Exception e) {
//            Logger.m7120e(e.getMessage());
//        }
//    }
//
//    @JSMethod
//    public void takeLoginOut(String msg, JSCallback callback) {
//        LoginHelper.INSTANCE.doLogout(ActivityHelper.INSTANCE.getTopActivity());
//    }
//
//    @JSMethod
//    public void idVerification(String msg, JSCallback callback) {
//        Activity activity = ActivityHelper.INSTANCE.getTopActivity();
//        if (activity != null) {
//            Intent intent = new Intent();
//            intent.setClassName(activity, "com.hzbankwealth.harmony.activity.login.SafetyActivity");
//            activity.startActivity(intent);
//        }
//    }
//
//    @JSMethod
//    @SuppressLint({"CheckResult"})
//    public void getDepartment(String msg, final JSCallback callback) {
//        Observable<String> observable = doMtopRequest(msg);
//        if (observable != null) {
//            AutoErrorHandler.subscribe(observable, new Consumer<String>() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.6
//                public void accept(String result) throws Exception {
//                    try {
//                        List<JSONObject> resultData = ((WorkDynamic) JSONObject.parseObject(result, WorkDynamic.class)).getReturnObject().getResultData();
//                        String[] items = new String[resultData.size()];
//                        JSONObject[] data = new JSONObject[resultData.size()];
//                        for (int i = 0; i < resultData.size(); i++) {
//                            items[i] = resultData.get(i).getString("org_name");
//                            data[i] = resultData.get(i);
//                        }
//                        CommonEvent.this.showListDialog(items, data, callback);
//                    } catch (Exception e) {
//                        Logger.m7120e(e.getMessage());
//                    }
//                }
//            });
//        }
//    }
//
//    @JSMethod
//    public void showListDialog(String msg, JSCallback callback) {
//        try {
//            JSONArray jsonArray = JSONArray.parseArray(msg);
//            String[] items = new String[jsonArray.size()];
//            JSONObject[] data = new JSONObject[jsonArray.size()];
//            for (int i = 0; i < jsonArray.size(); i++) {
//                items[i] = jsonArray.getJSONObject(i).getString("title");
//                data[i] = jsonArray.getJSONObject(i).getJSONObject("data");
//            }
//            showListDialog(items, data, callback);
//        } catch (Exception e) {
//            Logger.m7119ex(e);
//        }
//    }
//
//    /* JADX INFO: Access modifiers changed from: private */
//    public void showListDialog(String[] items, final JSONObject[] data, final JSCallback callback) {
//        new AlertDialog.Builder(ActivityHelper.INSTANCE.getTopActivity()).setTitle(C2180R.string.select_please).setItems(items, new DialogInterface.OnClickListener() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.9
//            @Override // android.content.DialogInterface.OnClickListener
//            public void onClick(DialogInterface dialog, int which) {
//                callback.invoke(JSONObject.toJSONString(data[which]));
//            }
//        }).setNegativeButton(C2180R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.8
//            @Override // android.content.DialogInterface.OnClickListener
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        }).setPositiveButton(C2180R.string.confirm, new DialogInterface.OnClickListener() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.7
//            @Override // android.content.DialogInterface.OnClickListener
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        }).create().show();
//    }
//
//    @JSMethod
//    @SuppressLint({"CheckResult"})
//    public void getRequestResult(String msg, final JSCallback callback) {
//        Observable<String> observable = doMtopRequest(msg);
//        if (observable != null) {
//            observable.subscribe(new Consumer<String>() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.10
//                public void accept(String result) throws Exception {
//                    String toastMsg = "";
//                    if (!TextUtils.isEmpty(result)) {
//                        try {
//                            JSONObject rltJson = JSONObject.parseObject(result);
//                            if (!rltJson.containsKey(ConstantHolder.KEY_SUCCESSED) || rltJson.getBoolean(ConstantHolder.KEY_SUCCESSED).booleanValue()) {
//                                JSONObject returnJson = new JSONObject();
//                                returnJson.put("result", (Object) "1");
//                                returnJson.put("callbackData", (Object) JSONObject.parseObject(result));
//                                Logger.m7122d("returnJson: " + returnJson.toString());
//                                callback.invoke(returnJson.toString());
//                                return;
//                            }
//                            if (rltJson.containsKey(ConstantHolder.KEY_EXCEPTION_MSG)) {
//                                toastMsg = rltJson.getString(ConstantHolder.KEY_EXCEPTION_MSG);
//                            } else if (rltJson.containsKey(ConstantHolder.KEY_ERROR_MESSAGE)) {
//                                toastMsg = rltJson.getString(ConstantHolder.KEY_ERROR_MESSAGE);
//                            }
//                            Activity topActivity = ActivityHelper.INSTANCE.getTopActivity();
//                            AppUtils.showToast(topActivity, Operators.SPACE_STR + toastMsg);
//                        } catch (Exception e) {
//                            Logger.m7120e(e.getMessage());
//                        }
//                    } else {
//                        CommonEvent.this.sendErrorCallbak(callback);
//                    }
//                }
//            }, new Consumer<Throwable>() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.11
//                public void accept(Throwable throwable) throws Exception {
//                    CommonEvent.this.sendErrorCallbak(callback);
//                }
//            });
//        }
//    }
//
//    /* JADX INFO: Access modifiers changed from: private */
//    public void sendErrorCallbak(JSCallback callback) {
//        try {
//            JSONObject callbackJson = new JSONObject();
//            callbackJson.put("result", (Object) "0");
//            callbackJson.put("callbackData", (Object) "");
//            callback.invoke(callbackJson.toString());
//        } catch (Exception e) {
//            Logger.m7120e(e.getMessage());
//        }
//    }
//
//    private Observable<String> doMtopRequest(String msg) {
//        if (AppUtils.isEmpty(msg)) {
//            return null;
//        }
//        try {
//            JSONObject jsonObject = JSONObject.parseObject(msg);
//            JSONObject bodyJson = jsonObject.getJSONObject("bodyJson");
//            boolean showLoading = false;
//            if (jsonObject.containsKey("showLoading")) {
//                showLoading = jsonObject.getBoolean("showLoading").booleanValue();
//            }
//            if (jsonObject.containsKey("crmLoading")) {
//                showLoading = jsonObject.getBoolean("crmLoading").booleanValue();
//            }
//            if (bodyJson != null && !AppUtils.isEmpty(bodyJson.toString())) {
//                return new MtopHelper2.Builder(ActivityHelper.INSTANCE.getTopActivity()).url(jsonObject.getString("url")).version(jsonObject.getString("version")).setMethod(jsonObject.getString("type")).dataList(jsonObject.getJSONObject("dataList")).bodyJson(bodyJson).showLoading(showLoading).build().request();
//            }
//            return new MtopHelper.Builder(ActivityHelper.INSTANCE.getTopActivity()).url(jsonObject.getString("url")).version(jsonObject.getString("version")).dataList(jsonObject.getJSONObject("dataList")).setMethod(jsonObject.getString("type")).showLoading(showLoading).build().request();
//        } catch (Exception e) {
//            Logger.m7120e(e.getMessage());
//            return null;
//        }
//    }
//
//    @JSMethod
//    @SuppressLint({"CheckResult"})
//    public void setHomeDetails(String msg, JSCallback callback) {
//        Observable<String> observable = doMtopRequest(msg);
//        if (observable != null) {
//            AutoErrorHandler.subscribe(observable, new Consumer<String>() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.12
//                public void accept(String result) throws Exception {
//                    if (!TextUtils.isEmpty(result)) {
//                        ConstantHolder.HTML_DATA = result;
//                        Intent intent = new Intent(ActivityHelper.INSTANCE.getTopActivity(), DetailsActivity.class);
//                        intent.putExtra("type", ConstantHolder.TYPE_HOME_DETAILS);
//                        ActivityHelper.INSTANCE.getTopActivity().startActivity(intent);
//                    }
//                }
//            });
//        }
//    }
//
//    @JSMethod
//    public void sanhuaDetails(String msg, JSCallback callback) {
//        final Activity activity = ActivityHelper.INSTANCE.getTopActivity();
//        if (activity != null && !TextUtils.isEmpty(msg)) {
//            JSONObject dataJson = JSONObject.parseObject(JSONObject.parseObject(msg).getString("dataList"));
//            final String taskID = dataJson.getString("taskID");
//            final String finishType = dataJson.getString(ConstantHolder.EXTRA_FINISH_TYPE);
//            final String state = dataJson.getString("state");
//            final String userTaskNo = dataJson.getString("userTaskNo");
//            final String dealChannel = dataJson.getString(ConstantHolder.EXTRA_DEAL_CHANNEL);
//            final String preTask = dataJson.getString(ConstantHolder.EXTRA_PRETASK);
//            Observable<String> observable = doMtopRequest(msg);
//            if (observable != null) {
//                AutoErrorHandler.subscribe(observable, new Consumer<String>() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.13
//                    public void accept(String result) throws Exception {
//                        try {
//                            if (!TextUtils.isEmpty(result)) {
//                                ConstantHolder.HTML_DATA = result;
//                                Intent intent = new Intent(activity, DetailsActivity.class);
//                                intent.putExtra("type", ConstantHolder.TYPE_SH_DETAILS);
//                                intent.putExtra("taskID", taskID);
//                                intent.putExtra(ConstantHolder.EXTRA_FINISH_TYPE, finishType);
//                                intent.putExtra("state", state);
//                                intent.putExtra("userTaskNo", userTaskNo);
//                                intent.putExtra(ConstantHolder.EXTRA_DEAL_CHANNEL, dealChannel);
//                                intent.putExtra(ConstantHolder.EXTRA_PRETASK, preTask);
//                                activity.startActivity(intent);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        }
//    }
//
//    @JSMethod
//    public void getEmailDetail(String msg, JSCallback callback) {
//        Activity activity = ActivityHelper.INSTANCE.getTopActivity();
//        if (activity != null) {
//            Intent intent = new Intent(activity, EmailActivity.class);
//            intent.putExtra(ConstantHolder.EXTRA_EMAIL_URL, msg);
//            activity.startActivity(intent);
//        }
//    }
//
//    @JSMethod
//    public void getDeviceList(String msg, JSCallback callback) {
//        Activity activity = ActivityHelper.INSTANCE.getTopActivity();
//        if (activity != null) {
//            activity.startActivity(new Intent(activity, DeviceActivity.class));
//        }
//    }
//
//    @JSMethod
//    public void statuteSign(String msg, JSCallback callback) {
//        AppLaunchHelper.INSTANCE.launchLegal(ActivityHelper.INSTANCE.getTopActivity());
//    }
//
//    @JSMethod
//    public void doCRM(String msg, JSCallback callback) {
//        AppLaunchHelper.INSTANCE.launchCRM(ActivityHelper.INSTANCE.getTopActivity());
//    }
//
//    @JSMethod
//    @SuppressLint({"CheckResult"})
//    public void turnToLiuZhuan(final String msg, JSCallback callback) {
//        ConstantHolder.liuzJSCallback = callback;
//        Observable<String> observable = doMtopRequest(msg);
//        if (observable != null) {
//            AutoErrorHandler.subscribe(observable, new Consumer<String>() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.14
//                public void accept(String result) throws Exception {
//                    Intent intent = new Intent(ActivityHelper.INSTANCE.getTopActivity(), WanderActivity.class);
//                    intent.putExtra("weex_data", msg);
//                    intent.putExtra("list_data", result);
//                    ActivityHelper.INSTANCE.getTopActivity().startActivityForResult(intent, 10012);
//                }
//            });
//        }
//    }
//
//    @JSMethod
//    public void getVersion(String msg, JSCallback callback) {
//        try {
//            callback.invoke(ActivityHelper.INSTANCE.getTopActivity().getApplicationContext().getPackageManager().getPackageInfo(ActivityHelper.INSTANCE.getTopActivity().getPackageName(), 0).versionName);
//        } catch (PackageManager.NameNotFoundException e) {
//            Logger.m7120e(e.getMessage());
//        }
//    }
//
//    @JSMethod
//    public void getMyOrganize(final String msg, final JSCallback callback) {
//        if (!AppUtils.isEmpty(msg)) {
//            new Thread(new Runnable() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.15
//                @Override // java.lang.Runnable
//                public void run() {
//                    try {
//                        System.currentTimeMillis();
//                        String myOrganize = ContactHelper.INSTANCE.getMyOrganize(msg);
//                        System.currentTimeMillis();
//                        callback.invoke(myOrganize);
//                    } catch (Exception e) {
//                        Logger.m7120e(e.getMessage());
//                    }
//                }
//            }).start();
//        }
//    }
//
//    @JSMethod
//    @SuppressLint({"MissingPermission"})
//    public void callPhone(String msg, JSCallback callback) {
//        ConstantHolder.callPhoneJSCallback = callback;
//        Activity activity = ActivityHelper.INSTANCE.getTopActivity();
//        if (activity != null) {
//            if (!AppUtils.isEmpty(msg)) {
//                String[] permissons = {"android.permission.CALL_PHONE", "android.permission.READ_CALL_LOG"};
//                if (!EasyPermissions.hasPermissions(activity, permissons)) {
//                    EasyPermissions.requestPermissions(activity, activity.getString(C2180R.string.grant_tip), 10000, permissons);
//                    return;
//                }
//                Intent intent = new Intent("android.intent.action.CALL");
//                intent.setData(Uri.parse(WebView.SCHEME_TEL + msg));
//                activity.startActivity(intent);
//                activity.getContentResolver().registerContentObserver(Uri.parse("content://call_log/calls"), false, new CallContentObserver(null, activity, msg));
//                return;
//            }
//            AppUtils.showToast(activity, activity.getString(C2180R.string.empty_number));
//        }
//    }
//
//    @JSMethod
//    public void clearCache(String msg, JSCallback callback) {
//        callback.invoke(Long.valueOf(FilePreviewHelper.clearFile(ActivityHelper.INSTANCE.getTopActivity())));
//    }
//
//    @JSMethod
//    public void getPhoneModel(String msg, JSCallback callback) {
//        callback.invoke(Build.MODEL);
//    }
//
//    @JSMethod
//    @SuppressLint({"CheckResult"})
//    public void getSelectDate(String msg, final JSCallback callback) {
//        if (AppUtils.isEmpty(ConstantHolder.mSelectDate)) {
//            MtopHelper.Builder version = new MtopHelper.Builder(ActivityHelper.INSTANCE.getTopActivity()).url(ConstantHolder.URL_GET_BY_PAGE).version(ConstantHolder.VERSION11);
//            MtopHelper.Builder putParam = version.putParam("startDate", AppUtils.getCurrentTime("yyyyMMdd") + "000000");
//            AutoErrorHandler.subscribe(putParam.putParam("endDate", AppUtils.getCurrentTime("yyyyMMdd") + "235959").build().request(), new Consumer<String>() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.16
//                public void accept(String result) throws Exception {
//                    try {
//                        JSONObject returnJson = new JSONObject();
//                        returnJson.put("date_time", (Object) AppUtils.getCurrentTime("yyyyMMdd"));
//                        returnJson.put("data", (Object) result);
//                        callback.invoke(returnJson);
//                    } catch (Exception e) {
//                        Logger.m7120e(e.getMessage());
//                    }
//                }
//            });
//            return;
//        }
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("date_time", (Object) ConstantHolder.mSelectDate);
//        jsonObject.put("data", (Object) ConstantHolder.mSelectData);
//        callback.invoke(jsonObject);
//    }
//
//    @JSMethod
//    public void unAccountManage(String msg, JSCallback callback) {
//        if (!TextUtils.isEmpty(msg)) {
//            Intent intent = new Intent(ActivityHelper.INSTANCE.getTopActivity(), WebViewActivity.class);
//            intent.putExtra(ConstantHolder.EXTRA_WEB_VIEW_URL, msg);
//            intent.putExtra(ConstantHolder.EXTRA_URL_TYPE, ConstantHolder.URL_TYPE_GUANKUAI);
//            ActivityHelper.INSTANCE.getTopActivity().startActivity(intent);
//        }
//    }
//
//    @JSMethod
//    public void Procurement(String msg, JSCallback callback) {
//        Intent intent = new Intent(ActivityHelper.INSTANCE.getTopActivity(), PurchaseWebViewActivity.class);
//        intent.putExtra(ConstantHolder.EXTRA_WEB_VIEW_URL, PropertiesHelper.INSTANCE.getConfigString(ActivityHelper.INSTANCE.getTopActivity(), "cai_gou") + "?username=" + LoginHelper.INSTANCE.getUsername(ActivityHelper.INSTANCE.getTopActivity()) + "&token=" + LoginHelper.INSTANCE.getLoginToken(ActivityHelper.INSTANCE.getTopActivity()) + "&channelno=M&appno=HAO");
//        ActivityHelper.INSTANCE.getTopActivity().startActivity(intent);
//    }
//
//    @JSMethod
//    public void Finance(String msg, JSCallback callback) {
//        Intent intent = new Intent(ActivityHelper.INSTANCE.getTopActivity(), WebViewActivity.class);
//        intent.putExtra(ConstantHolder.EXTRA_WEB_VIEW_URL, PropertiesHelper.INSTANCE.getConfigString(ActivityHelper.INSTANCE.getTopActivity(), "cai_wu") + "?loginId=" + LoginHelper.INSTANCE.getUsername(ActivityHelper.INSTANCE.getTopActivity()) + "&channelno=M&appno=HAO");
//        intent.putExtra(ConstantHolder.EXTRA_URL_TYPE, ConstantHolder.URL_TYPE_CAIWU);
//        ActivityHelper.INSTANCE.getTopActivity().startActivity(intent);
//    }
//
//    @JSMethod
//    public void messageShare(String msg, JSCallback callback) {
//        Intent mIntent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:"));
//        mIntent.putExtra("sms_body", msg);
//        ActivityHelper.INSTANCE.getTopActivity().startActivity(mIntent);
//    }
//
//    @JSMethod
//    public void weChatShare(String msg, JSCallback callback) {
//        Bitmap bitmap = BitmapFactory.decodeResource(ActivityHelper.INSTANCE.getTopActivity().getResources(), C2180R.C2182drawable.wechat_share_dev);
//        WXImageObject imageObject = new WXImageObject(bitmap);
//        WXMediaMessage message = new WXMediaMessage();
//        message.mediaObject = imageObject;
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
//        bitmap.recycle();
//        message.thumbData = ImageUtil.bmpToByteArray(scaledBitmap, true);
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = WXBasicComponentType.IMG + System.currentTimeMillis();
//        req.message = message;
//        req.scene = 0;
//        WechatHelper.INSTANCE.api.sendReq(req);
//    }
//
//    @JSMethod
//    public void getIndexPage(String msg, JSCallback callback) {
//        ActivityHelper.INSTANCE.finishWeexActivity();
//    }
//
//    @JSMethod
//    public void webPreview(String msg, JSCallback callback) {
//        if (!AppUtils.isEmpty(msg)) {
//            new FilePreviewHelper().showFile(ActivityHelper.INSTANCE.getTopActivity(), msg, null);
//        }
//    }
//
//    @JSMethod
//    public void webpreviewAndShare(String msg, JSCallback callback) {
//        if (!AppUtils.isEmpty(msg)) {
//            FilePreviewHelper previewHelper = new FilePreviewHelper();
//            previewHelper.setShowShare(true);
//            previewHelper.showFile(ActivityHelper.INSTANCE.getTopActivity(), msg, null);
//        }
//    }
//
//    @JSMethod
//    public void getOfficialNumber(String msg, JSCallback callback) {
//        try {
//            JSONObject jsonObject = JSONObject.parseObject(msg);
//            Intent intent = new Intent(ConstantHolder.SHOW_RED_POINT_ACTION);
//            intent.putExtra(ConstantHolder.EXTRA_HOME, jsonObject.getIntValue(ConstantHolder.EXTRA_HOME));
//            intent.putExtra(ConstantHolder.EXTRA_WORK, jsonObject.getIntValue("GZlist"));
//            intent.putExtra(ConstantHolder.EXTRA_MINE, jsonObject.getIntValue(ConstantHolder.EXTRA_MINE));
//            ActivityHelper.INSTANCE.getTopActivity().sendBroadcast(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @JSMethod
//    public void getContactByPersonID(String msg, JSCallback callback) {
//        try {
//            callback.invoke(ContactHelper.INSTANCE.queryContactByPersonID(msg));
//        } catch (Exception e) {
//            Logger.m7120e(e.getMessage());
//        }
//    }
//
//    @JSMethod
//    public void getCommonNet(String msg, final JSCallback callback) {
//        if (!TextUtils.isEmpty(msg)) {
//            try {
//                JSONObject jsonObject = JSONObject.parseObject(msg);
//                String baseUrl = PropertiesHelper.INSTANCE.getConfigString(ActivityHelper.INSTANCE.getTopActivity(), ConstantHolder.KEY_CONTACT_URL);
//                StringBuilder builder = new StringBuilder();
//                if (jsonObject.containsKey("url")) {
//                    String url = jsonObject.getString("url");
//                    builder.append(baseUrl);
//                    builder.append(url);
//                    builder.append(Operators.CONDITION_IF_STRING);
//                }
//                if (jsonObject.containsKey("dataList")) {
//                    for (Map.Entry<String, String> entry : ((Map) JSON.parseObject(jsonObject.getString("dataList"), Map.class)).entrySet()) {
//                        if (!(entry.getKey() == null || entry.getValue() == null)) {
//                            builder.append((Object) entry.getKey());
//                            builder.append("=");
//                            builder.append((Object) entry.getValue());
//                            builder.append("&");
//                        }
//                        return;
//                    }
//                }
//                String params = builder.toString();
//                HttpHelper.getInstance().get(ActivityHelper.INSTANCE.getTopActivity(), params.substring(0, params.length() - 1), new HttpHelper.HttpCallback() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.17
//                    @Override // com.hzbankwealth.harmony.helper.HttpHelper.HttpCallback
//                    public void onSuccess(String result) {
//                        callback.invoke(JSONObject.parseObject(result));
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @JSMethod
//    public void selectFileAndPic(String msg, JSCallback callback) {
//        ImageSelector.builder().useCamera(true).setSingle(false).setMaxSelectCount(9).setViewImage(true).start(ActivityHelper.INSTANCE.getTopActivity(), 10004);
//    }
//
//    @JSMethod
//    public void selectFileAndPic2(String msg, JSCallback callback) {
//        if (!TextUtils.isEmpty(msg)) {
//            ConstantHolder.selectFileAndPic2JSCallback = callback;
//            JSONObject jsonObject = JSONObject.parseObject(msg);
//            int maxPic = 9;
//            if (jsonObject.containsKey("maxPic")) {
//                maxPic = jsonObject.getIntValue("maxPic");
//            }
//            UpLoadHelper.msg = msg;
//            ImageSelector.builder().useCamera(true).setSingle(false).setMaxSelectCount(maxPic).setViewImage(true).start(ActivityHelper.INSTANCE.getTopActivity(), 10006);
//        }
//    }
//
//    @JSMethod
//    public void shareWeChatMiniProgram(String msg, JSCallback callback) {
//        Context context = ActivityHelper.INSTANCE.getTopActivity();
//        WeexActivity weexActivity = (WeexActivity) ActivityHelper.INSTANCE.getTopActivity();
//        if (weexActivity != null) {
//            Bitmap bitmap = ImageUtil.getMyCardBitmap(weexActivity, ImageUtil.getAllChildViews(weexActivity.getmWeexContainer()).get(7));
//            callback.invoke(true);
//            if (bitmap == null) {
//                AppUtils.showToast(context, "名片获取失败");
//                return;
//            }
//            LoginInfo loginInfo = LoginHelper.INSTANCE.getLoginInfo(context);
//            String name = LoginHelper.INSTANCE.getNickname(context);
//            String appShareId = loginInfo.getUserId().replace("-", "");
//            String title = name + "的名片";
//            if (!TextUtils.isEmpty(msg)) {
//                try {
//                    JSONObject jsonObject = JSONObject.parseObject(msg);
//                    if (jsonObject.containsKey("name")) {
//                        title = jsonObject.getString("name");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            WXMiniProgramObject miniProgram = new WXMiniProgramObject();
//            miniProgram.webpageUrl = Operators.SPACE_STR;
//            miniProgram.miniprogramType = 0;
//            miniProgram.userName = "gh_012fd4bda1c2";
//            miniProgram.path = "pages/linkCard/index?appShareId=" + appShareId;
//            miniProgram.withShareTicket = true;
//            WXMediaMessage mediaMessage = new WXMediaMessage(miniProgram);
//            mediaMessage.title = title;
//            mediaMessage.thumbData = ImageUtil.compressBitmapToData(bitmap, 128.0f);
//            bitmap.recycle();
//            SendMessageToWX.Req req = new SendMessageToWX.Req();
//            req.scene = 0;
//            req.message = mediaMessage;
//            WechatHelper.INSTANCE.api.sendReq(req);
//        }
//    }
//
//    @JSMethod
//    public void saveWXImage(String msg, JSCallback callback) {
//        Bitmap bitmap;
//        if (!TextUtils.isEmpty(msg) && (bitmap = ImageUtil.stringToBitmap(msg)) != null) {
//            Activity topActivity = ActivityHelper.INSTANCE.getTopActivity();
//            ImageUtil.saveBmp2Gallery(topActivity, bitmap, System.currentTimeMillis() + "");
//        }
//    }
//
//    @JSMethod
//    public void getRunData(String msg, JSCallback callback) {
//        if (callback != null) {
//            callback.invoke(Integer.valueOf(StepCounterHelper.getInstance().getStepCounts()));
//        }
//    }
//
//    @JSMethod
//    public void getVideoList(String msg, final JSCallback callback) {
//        if (!TextUtils.isEmpty(msg)) {
//            JSONObject jsonObject = JSON.parseObject(msg);
//            if (jsonObject.containsKey("url") && jsonObject.containsKey("dataList")) {
//                HttpHelper.getInstance().postBiao(ActivityHelper.INSTANCE.getTopActivity(), jsonObject.getString("url"), jsonObject.getJSONObject("dataList").toJSONString(), new HttpHelper.HttpCallback() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.18
//                    @Override // com.hzbankwealth.harmony.helper.HttpHelper.HttpCallback
//                    public void onSuccess(String result) {
//                        try {
//                            JSONObject rltJson = JSONObject.parseObject(result);
//                            Log.d("andy", "getVideoList onSuccess: " + rltJson.toJSONString());
//                            if (!rltJson.containsKey(ConstantHolder.KEY_SUCCESSED) || !rltJson.getBoolean(ConstantHolder.KEY_SUCCESSED).booleanValue()) {
//                                JSONObject returnJson = new JSONObject();
//                                returnJson.put("result", (Object) "0");
//                                returnJson.put("callbackData", (Object) JSONObject.parseObject(result));
//                                callback.invoke(returnJson.toString());
//                                return;
//                            }
//                            JSONObject returnJson2 = new JSONObject();
//                            returnJson2.put("result", (Object) "1");
//                            returnJson2.put("callbackData", (Object) JSONObject.parseObject(result));
//                            callback.invoke(returnJson2.toString());
//                        } catch (Exception e) {
//                            while (true) {
//                                e.printStackTrace();
//                                return;
//                            }
//                        }
//                    }
//                });
//            }
//        }
//    }
//
//    @JSMethod
//    public void playVideo(String msg, JSCallback callback) {
//        Activity activity = ActivityHelper.INSTANCE.getTopActivity();
//        if (activity != null) {
//            Intent intent = new Intent(activity, PlayVideoActivity2.class);
//            intent.putExtra(NotificationCompat.CATEGORY_MESSAGE, msg);
//            activity.startActivity(intent);
//        }
//    }
//
//    @JSMethod
//    public void getScreenShot(String msg, JSCallback callback) {
//        Activity activity = ActivityHelper.INSTANCE.getTopActivity();
//        if (activity != null) {
//            callback.invoke(ImageUtil.getScreenBase64(activity));
//        }
//    }
//
//    @JSMethod
//    public void getHealthCode(String msg, JSCallback callback) {
//        ConstantHolder.recHealthyJSCallback = callback;
//        ImageSelector.builder().useCamera(false).setSingle(true).setMaxSelectCount(1).setViewImage(true).start(ActivityHelper.INSTANCE.getTopActivity(), 10007);
//    }
//
//    @JSMethod
//    public void getFaceRecognition(String data, JSCallback callback) {
//    }
//
//    @JSMethod
//    public void getCrmPictures(String msg, JSCallback callback) {
//        Activity top = ActivityHelper.INSTANCE.getTopActivity();
//        if (top != null) {
//            if (TextUtils.isEmpty(ConstantHolder.CRM_ADDRESS)) {
//                BaiduMapUtils.getInstance().initBaiduMap(top, null);
//            }
//            int maxCount = 9;
//            if (!TextUtils.isEmpty(msg)) {
//                JSONObject jsonObject = JSON.parseObject(msg);
//                ConstantHolder.CRM_PIC_NEED_DELETE = jsonObject.getBoolean("needDelete").booleanValue();
//                maxCount = jsonObject.getInteger("picNum").intValue();
//            }
//            ConstantHolder.getCrmPicturesJSCallback = callback;
//            ImageSelector.builder().useCamera(true).setSingle(false).setMaxSelectCount(maxCount).setViewImage(true).start(top, 10008);
//        }
//    }
//
//    @JSMethod
//    public void getLocationInfo(JSCallback callback) {
//        BaiduMapUtils.getInstance().initBaiduMap(ActivityHelper.INSTANCE.getTopActivity(), callback);
//    }
//
//    @JSMethod
//    public void takeIDCardPhoto(String data, JSCallback callback) {
//        Activity top = ActivityHelper.INSTANCE.getTopActivity();
//        if (top != null) {
//            if (TextUtils.isEmpty(ConstantHolder.CRM_ADDRESS)) {
//                BaiduMapUtils.getInstance().initBaiduMap(top, null);
//            }
//            try {
//                if (!TextUtils.isEmpty(data)) {
//                    ConstantHolder.CRM_PIC_NEED_DELETE = JSON.parseObject(data).getBoolean("needDelete").booleanValue();
//                }
//                ConstantHolder.ocrJSCallback = callback;
//                Intent intents = new Intent("android.media.action.IMAGE_CAPTURE");
//                File externalFilesDir = top.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
//                File file = new File(externalFilesDir, System.currentTimeMillis() + ".png");
//                if (!file.exists()) {
//                    file.createNewFile();
//                }
//                if (Build.VERSION.SDK_INT >= 24) {
//                    intents.putExtra("output", FileProvider.getUriForFile(top, top.getPackageName() + ".fileProvider", file));
//                } else {
//                    intents.putExtra("output", Uri.fromFile(file));
//                }
//                ConstantHolder.OCR_FILE_PATH = file.getAbsolutePath();
//                top.startActivityForResult(intents, 10010);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @JSMethod
//    public void pop(String url, JSCallback callback) {
//        ActivityHelper.INSTANCE.pop(url);
//        callback.invoke("");
//    }
//
//    @JSMethod
//    public void pushToNew(String url, JSCallback callback) {
//        Activity activity;
//        if (url != null && (activity = ActivityHelper.INSTANCE.getTopActivity()) != null && !TextUtils.isEmpty(url)) {
//            Uri uri = Uri.parse(url);
//            Intent activityIntent = new Intent(activity, WeexActivity.class);
//            activityIntent.setData(uri);
//            activityIntent.setAction(ConstantHolder.ACTION_WEEX);
//            ActivityHelper.INSTANCE.popAll();
//            activity.startActivity(activityIntent);
//            callback.invoke("");
//        }
//    }
//
//    @JSMethod
//    public void postRequest(String msg, final JSCallback callback) {
//        if (!TextUtils.isEmpty(msg)) {
//            Map<String, String> map = new HashMap<>();
//            try {
//                JSONObject jsonObject = JSONObject.parseObject(msg);
//                String url = jsonObject.getString("url");
//                if (jsonObject.containsKey("dataList")) {
//                    map = (Map) JSON.parseObject(jsonObject.getString("dataList"), Map.class);
//                }
//                HttpHelper.getInstance().post(ActivityHelper.INSTANCE.getTopActivity(), url, map, new HttpHelper.HttpCallback() { // from class: com.hzbankwealth.harmony.emas.CommonEvent.19
//                    @Override // com.hzbankwealth.harmony.helper.HttpHelper.HttpCallback
//                    public void onSuccess(String result) {
//                        callback.invoke(result);
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @JSMethod
//    public void crmFilePreview(String msg, JSCallback callback) {
//        if (!TextUtils.isEmpty(msg)) {
//            String type = msg.substring(msg.lastIndexOf(Operators.DOT_STR));
//            if (!TextUtils.isEmpty(type)) {
//                new FilePreviewHelper().showFile(ActivityHelper.INSTANCE.getTopActivity(), msg, type);
//            }
//        }
//    }
//
//    @JSMethod
//    public void screenShotToWeChat(String msg, JSCallback callback) {
//        Activity top;
//        this.bitmap = null;
//        if (!TextUtils.isEmpty(msg) && (top = ActivityHelper.INSTANCE.getTopActivity()) != null) {
//            JSONObject jsonObject = JSON.parseObject(msg);
//            JSONObject paramJson = new JSONObject();
//            if (jsonObject.containsKey("type")) {
//                this.type = jsonObject.getString("type");
//            }
//            if (jsonObject.containsKey("params")) {
//                paramJson = JSON.parseObject(jsonObject.getString("params"));
//            }
//            String str = this.type;
//            char c = 65535;
//            switch (str.hashCode()) {
//                case 49:
//                    if (str.equals("1")) {
//                        c = 0;
//                        break;
//                    }
//                    break;
//                case 50:
//                    if (str.equals("2")) {
//                        c = 1;
//                        break;
//                    }
//                    break;
//                case 51:
//                    if (str.equals("3")) {
//                        c = 2;
//                        break;
//                    }
//                    break;
//            }
//            if (c == 0) {
//                this.bitmap = ImageUtil.getScreenShot(top);
//                if (this.bitmap != null) {
//                    callback.invoke("showShare");
//                }
//            } else if (c != 1) {
//                if (c == 2) {
//                    if (paramJson.containsKey("url")) {
//                        this.url = paramJson.getString("url");
//                    }
//                    if (paramJson.containsKey("title")) {
//                        this.title = paramJson.getString("title");
//                    }
//                    if (paramJson.containsKey(Message.DESCRIPTION)) {
//                        this.description = paramJson.getString(Message.DESCRIPTION);
//                    }
//                    if (!TextUtils.isEmpty(this.url) && !TextUtils.isEmpty(this.title) && !TextUtils.isEmpty(this.description)) {
//                        callback.invoke("showShare");
//                    }
//                }
//            } else if (paramJson.containsKey("image")) {
//                this.bitmap = ImageUtil.stringToBitmap(paramJson.getString("image"));
//                if (this.bitmap != null) {
//                    callback.invoke("showShare");
//                }
//            }
//        }
//    }
//
//    @JSMethod
//    public void crmShareWeChat(String msg, JSCallback callback) {
//        Activity top;
//        Bitmap bitmap;
//        if (!TextUtils.isEmpty(msg) && (top = ActivityHelper.INSTANCE.getTopActivity()) != null) {
//            JSONObject jsonObject = JSON.parseObject(msg);
//            if (jsonObject.containsKey("save") && jsonObject.containsKey("scene")) {
//                boolean save = jsonObject.getString("save").equals("yes");
//                boolean isSession = jsonObject.getString("scene").equals("session");
//                if (!save || (bitmap = this.bitmap) == null) {
//                    if (WechatHelper.INSTANCE.api == null) {
//                        WechatHelper.INSTANCE.init(top);
//                    }
//                    if (!WechatHelper.INSTANCE.api.isWXAppInstalled()) {
//                        AppUtils.showToast(top, top.getString(C2180R.string.wechat_not_install));
//                    } else if (this.type.equals("3")) {
//                        ImageUtil.wxShareUrl(isSession, this.url, this.title, this.description);
//                    } else {
//                        ImageUtil.wxSharePic(WXBasicComponentType.IMG, isSession, this.bitmap, ImageUtil.saveBitmapToLocal(this.bitmap));
//                    }
//                } else {
//                    ImageUtil.saveBmp2Gallery(top, bitmap, System.currentTimeMillis() + "");
//                }
//            }
//        }
//    }
//
//    @JSMethod
//    public void picToWeChat() {
//        ImageSelector.builder().useCamera(true).setSingle(true).setViewImage(true).start(ActivityHelper.INSTANCE.getTopActivity(), ConstantHolder.PIC_WECHAT_SHARE);
//    }
//
//    @JSMethod
//    public void savePersistentData(String data, JSCallback jsCallback) {
//        JsSaveData jsSaveData = (JsSaveData) JSONObject.parseObject(data, JsSaveData.class);
//        if (HZFConfig.getInstance().putString(jsSaveData.getKey(), jsSaveData.getValue()).commit()) {
//            jsCallback.invoke(jsSaveData.getValue());
//        }
//    }
//
//    @JSMethod
//    public void getPersistentData(String data, JSCallback callback) {
//        String value = HZFConfig.getInstance().getString(((JsSaveData) JSONObject.parseObject(data, JsSaveData.class)).getKey(), "");
//        if (TextUtils.isEmpty(value)) {
//            callback.invoke("");
//        } else if (value.charAt(0) == '{') {
//            callback.invoke(JSONObject.parseObject(value));
//        } else if (value.charAt(0) == '[') {
//            callback.invoke(JSONObject.parseArray(value));
//        } else {
//            callback.invoke(value);
//        }
//    }
//
//    @JSMethod
//    public void pushAndRemoveController(String msg) {
//        if (!TextUtils.isEmpty(msg)) {
//            JSONObject jsonObject = JSON.parseObject(msg);
//            if (jsonObject.containsKey("pushUrl") && jsonObject.containsKey("popUrl")) {
//                String pushUrl = jsonObject.getString("pushUrl");
//                String popUrl = jsonObject.getString("popUrl");
//                if (!TextUtils.isEmpty(pushUrl) && !TextUtils.isEmpty(popUrl)) {
//                    ActivityHelper.INSTANCE.push(pushUrl);
//                    ActivityHelper.INSTANCE.pop(popUrl);
//                }
//            }
//        }
//    }
//
//    @JSMethod
//    public void getH5Data(JSCallback callback) {
//        if (callback != null) {
//            callback.invoke(ConstantHolder.HTML_DATA);
//            ConstantHolder.HTML_DATA = "";
//        }
//    }
//
//    @JSMethod
//    public void getClientSize(JSCallback callback) {
//        Activity topActivity = ActivityHelper.INSTANCE.getTopActivity();
//        if (topActivity != null && callback != null) {
//            int width = topActivity.getResources().getDisplayMetrics().widthPixels;
//            int height = topActivity.getResources().getDisplayMetrics().heightPixels;
//            JSONObject object = new JSONObject();
//            object.put("windowWidth", (Object) Integer.valueOf(width));
//            object.put("windowHeight", (Object) Integer.valueOf(height));
//            object.put("statusBarHeight", (Object) Integer.valueOf(AppUtils.getStatusBarHeight(topActivity)));
//            object.put("navigationBarHeight", (Object) Integer.valueOf(AppUtils.getNavigationBarHeight(topActivity)));
//            callback.invoke(object);
//        }
//    }
//
//    @JSMethod
//    public void track(String eventName, String eventData) {
//    }
//
//    @JSMethod
//    public void playStudyVideo(String msg, JSCallback callback) {
//        Activity activity = ActivityHelper.INSTANCE.getTopActivity();
//        if (activity != null) {
//            Intent intent = new Intent(activity, PlayVideoActivity2.class);
//            intent.putExtra(NotificationCompat.CATEGORY_MESSAGE, msg);
//            activity.startActivity(intent);
//        }
//    }
//
//    @JSMethod
//    public void loginZoom(String msg, JSCallback jsCallback) {
//    }
//
//    @JSMethod
//    public void beginTeaching(String msg, JSCallback callback) {
//        if (!TextUtils.isEmpty(msg)) {
//            BaseActivity baseActivity = (BaseActivity) ActivityHelper.INSTANCE.getTopActivity();
//            if (TextUtils.isEmpty(ConstantHolder.zoomToken)) {
//                AutoErrorHandler.subscribe(new MtopHelper.Builder(baseActivity).url(ConstantHolder.URL_ZOOM_LOGIN).dataList(new JSONObject()).build().request(), new Consumer(msg, baseActivity) { // from class: com.hzbankwealth.harmony.emas.-$$Lambda$CommonEvent$loM5coG5PkAsrLu2KAvO1LiV8UQ
//                    private final /* synthetic */ String f$1;
//                    private final /* synthetic */ BaseActivity f$2;
//
//                    {
//                        this.f$1 = r2;
//                        this.f$2 = r3;
//                    }
//
//                    @Override // p325io.reactivex.functions.Consumer
//                    public final void accept(Object obj) {
//                        CommonEvent.this.lambda$beginTeaching$0$CommonEvent(this.f$1, this.f$2, (String) obj);
//                    }
//                });
//            } else {
//                loginZoomService(msg, baseActivity);
//            }
//        }
//    }
//
//    public /* synthetic */ void lambda$beginTeaching$0$CommonEvent(String msg, BaseActivity baseActivity, String result) throws Exception {
//        JSONObject jsonObject = JSON.parseObject(result);
//        if (jsonObject.getBoolean(ConstantHolder.KEY_SUCCESSED).booleanValue()) {
//            ConstantHolder.zoomToken = jsonObject.getJSONObject(ConstantHolder.KEY_RETURN_OBJECT).getString("loginToken");
//            Log.d("andy", "getZoomToken: " + ConstantHolder.zoomToken);
//            loginZoomService(msg, baseActivity);
//        }
//    }
//
//    @JSMethod
//    public void beginLiving(String msg, JSCallback callback) {
//        if (!TextUtils.isEmpty(msg)) {
//            ConstantHolder.IS_BEGIN_LIVING = true;
//            ConstantHolder.courseId = JSON.parseObject(msg).getString(TtmlNode.ATTR_ID);
//            BaseActivity baseActivity = (BaseActivity) ActivityHelper.INSTANCE.getTopActivity();
//            if (TextUtils.isEmpty(ConstantHolder.zoomToken)) {
//                AutoErrorHandler.subscribe(new MtopHelper.Builder(baseActivity).url(ConstantHolder.URL_ZOOM_LOGIN).dataList(new JSONObject()).build().request(), new Consumer(msg, baseActivity) { // from class: com.hzbankwealth.harmony.emas.-$$Lambda$CommonEvent$LkZktnoqDWyMl9jG0HFkC5B3Jr4
//                    private final /* synthetic */ String f$1;
//                    private final /* synthetic */ BaseActivity f$2;
//
//                    {
//                        this.f$1 = r2;
//                        this.f$2 = r3;
//                    }
//
//                    @Override // p325io.reactivex.functions.Consumer
//                    public final void accept(Object obj) {
//                        CommonEvent.this.lambda$beginLiving$1$CommonEvent(this.f$1, this.f$2, (String) obj);
//                    }
//                });
//            } else {
//                loginZoomService(msg, baseActivity);
//            }
//        }
//    }
//
//    public /* synthetic */ void lambda$beginLiving$1$CommonEvent(String msg, BaseActivity baseActivity, String result) throws Exception {
//        JSONObject jsonObject1 = JSON.parseObject(result);
//        if (jsonObject1.getBoolean(ConstantHolder.KEY_SUCCESSED).booleanValue()) {
//            ConstantHolder.zoomToken = jsonObject1.getJSONObject(ConstantHolder.KEY_RETURN_OBJECT).getString("loginToken");
//            Log.d("andy", "getZoomToken: " + ConstantHolder.zoomToken);
//            loginZoomService(msg, baseActivity);
//        }
//    }
//
//    private void loginZoomService(String msg, BaseActivity baseActivity) {
//    }
//
//    @JSMethod
//    public void joinCourse(String msg, JSCallback callback) {
//    }
//
//    @JSMethod
//    public void openSystemBrowser(String url) {
//        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
//            Intent intent = new Intent("android.intent.action.VIEW");
//            intent.addCategory("android.intent.category.BROWSABLE");
//            intent.setData(Uri.parse(url));
//            ActivityHelper.INSTANCE.getTopActivity().startActivity(intent);
//        }
//    }
//
//    @JSMethod
//    public void selectCropPic(String msg, JSCallback callback) {
//        if (!TextUtils.isEmpty(msg)) {
//            ConstantHolder.selectFileAndPic2JSCallback = callback;
//            UpLoadHelper.msg = msg;
//            ImageSelector.builder().useCamera(false).setCrop(true).setCropRatio(0.5625f).start(ActivityHelper.INSTANCE.getTopActivity(), ConstantHolder.CROP_PICTURE);
//        }
//    }
//
//    @JSMethod
//    public void openFile(String msg, JSCallback callback) {
//        if (!TextUtils.isEmpty(msg)) {
//            JSONObject jsonObject = JSON.parseObject(msg);
//            String url = jsonObject.getString(ConstantHolder.EXTRA_FILE_URL);
//            String type = jsonObject.getString("fileType");
//            if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(type)) {
//                new FilePreviewHelper().showFile(ActivityHelper.INSTANCE.getTopActivity(), url, type);
//            }
//        }
//    }
//
//    @JSMethod
//    public void openVoiceRecord() {
//        BaseWeexActivity topActivity = (BaseWeexActivity) ActivityHelper.INSTANCE.getTopActivity();
//        if (topActivity != null) {
//            topActivity.openVoiceRecord(Operators.SPACE_STR, null);
//        }
//    }
//
//    @JSMethod
//    public void cancelVoiceRecord(JSCallback callback) {
//        ConstantHolder.recorderJSCallback = callback;
//        BaseWeexActivity topActivity = (BaseWeexActivity) ActivityHelper.INSTANCE.getTopActivity();
//        if (topActivity != null) {
//            topActivity.cancelVoiceRecord();
//        }
//    }
//
//    @JSMethod
//    public void fileSelect(String msg, JSCallback callback) {
//        if (!TextUtils.isEmpty(msg) && callback != null) {
//            Activity activity = ActivityHelper.INSTANCE.getTopActivity();
//            ConstantHolder.selectFileAndPic2JSCallback = callback;
//            UpLoadHelper.msg = msg;
//            String[] permissons = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
//            if (activity == null || EasyPermissions.hasPermissions(activity, permissons)) {
//                JSONObject jsonObject = JSON.parseObject(msg);
//                int maxCount = jsonObject.getIntValue("maxCount");
//                String fileType = jsonObject.getString("fileType");
//                if (!TextUtils.isEmpty(fileType)) {
//                    FileSelector.from((AppCompatActivity) ActivityHelper.INSTANCE.getTopActivity()).setTilteBg(C2180R.color.oa_color_blue).setMaxCount(maxCount).setFileTypes(fileType.contains(",") ? fileType.split(",") : new String[]{fileType}).setSortType(0).requestCode(ConstantHolder.FILE_SELECT).start();
//                    return;
//                }
//                return;
//            }
//            EasyPermissions.requestPermissions(activity, activity.getString(C2180R.string.grant_tip), 10000, permissons);
//        }
//    }
//
//    @JSMethod
//    public void getAppName(String msg, JSCallback callback) {
//        Activity activity = ActivityHelper.INSTANCE.getTopActivity();
//        if (callback != null && activity != null) {
//            callback.invoke(activity.getResources().getString(C2180R.string.app_name));
//        }
//    }
//}