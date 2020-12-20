package com.xrwl.owner.module.account.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.PermissionUtils;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseWebActivity;
import com.xrwl.owner.module.publish.ui.ChongzhiActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebActivityputong extends BaseWebActivity {
    private WebView web_storage;
    protected String ghtoken;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 2;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption = null;
    public String dccity = null;
    private int GPS_REQUEST_CODE = 1;
    public String gonghuigengxin = null;


    public Double x = null;
    public Double y = null;

    public Double xxx = null;
    public Double yyy = null;

    public Double jiexistartx = null;
    public Double jiexistarty = null;

    public String jstel = null;
    public String result;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_storageputong);
        //        Intent intentaaa = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////        startActivity(intentaaa);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else {
            startLocaion();//开始定位
            Toast.makeText(this, "已开启定位权限", Toast.LENGTH_LONG).show();
        }

        //startActivity(new Intent(mContext, ChongzhiActivity.class));


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        getUserInfo();


        String b = "http://www.sxxrsc.cn/";
        Intent intent = getIntent();
        String s = intent.getStringExtra("url");
        String jiekou = intent.getStringExtra("jiekou");
        if (s == null) {
            s = b;
        }

        web_storage = (WebView) findViewById(R.id.web_view);
        web_storage.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });


        //web_storage.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        web_storage.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        web_storage.setVerticalScrollbarOverlay(true);
        web_storage.getSettings().setUseWideViewPort(true);
        web_storage.getSettings().setLoadWithOverviewMode(true);
        web_storage.getSettings().setJavaScriptEnabled(true);//允许执行js
        web_storage.getSettings().setUseWideViewPort(true);
        web_storage.getSettings().setLoadWithOverviewMode(true);
        web_storage.getSettings().setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        web_storage.getSettings().setGeolocationDatabasePath(dir);
        web_storage.getSettings().setDomStorageEnabled(true);
        web_storage.getSettings().setAppCacheEnabled(true);
        web_storage.getSettings().setDomStorageEnabled(true);//加上这一句就好了


        web_storage.getSettings().setGeolocationEnabled(true);
        web_storage.getSettings().setAllowFileAccess(true);
        web_storage.getSettings().setAllowFileAccessFromFileURLs(true);
        web_storage.getSettings().setAllowUniversalAccessFromFileURLs(true);
        web_storage.getSettings().setNeedInitialFocus(true);
        web_storage.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web_storage.getSettings().setLoadsImagesAutomatically(true); //支持自动加载图片

        web_storage.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, true);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //允许混合（http，https）
            //websettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            web_storage.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        web_storage.setWebViewClient(new WebViewClient() {

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();//接受所有网站的证书
            }
        });
        web_storage.setWebChromeClient(new WebChromeClient() {
            protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }


            // For Lollipop 5.0+ Devices
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    Toast.makeText(getBaseContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

        });

        String tocao = null;
        String path = "http://phpygh.lfxrsc.com/api/user/public/get_token?username=" + mAccount.getPhone() + "&device_type=android";
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            // 3.设置一些请求方式
            conn.setRequestMethod("GET");// 注意GET单词字幕一定要大写
            conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            int code = conn.getResponseCode(); // 服务器的响应码 200 OK //404 页面找不到
            if (code == 200) {
                InputStream is = conn.getInputStream();
                // 把is的内容转换为字符串
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                String result = new String(bos.toByteArray());
                try {
                    // 整个最大的JSON对象
                    JSONObject jsonObjectALL = new JSONObject(result);
                    /**
                     * 为什么要使用jsonObject.optString， 不使用jsonObject.getString
                     * 因为jsonObject.optString获取null不会报错
                     */
                    String data = jsonObjectALL.optString("data", null);
                    if (!TextUtils.isEmpty(data)) {
                        JSONObject jsonObject = new JSONObject(data);
                        String token = jsonObject.optString("token", null);
                        ghtoken = token;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                is.close();
            } else {
                // showToast("请求失败，该用户已经注册");
                //Toast.makeText(this, "请求失败，失败原因: " + code, ).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        web_storage.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return false;
            }
        });
        web_storage.setWebChromeClient(new MyWbChromeClient());
        // web_storage.loadUrl("http://www.16souyun.com/");


        //在js中调用本地java方法
        web_storage.addJavascriptInterface(new JsInterface(this), "AndroidWebView");

        web_storage.addJavascriptInterface(new JsInterfaces(this), "AndroidWebViews");

        //还得调用打电话的方法和js交互
        web_storage.addJavascriptInterface(new JsInterfacetel(this), "AndroidWebViewtel");

        //添加客户端支持
        web_storage.setWebChromeClient(new WebChromeClient());

        web_storage.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web_storage.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setTitle(String.valueOf(view.getTitle()));
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (web_storage.canGoBack()) {
            try {
                result = URLEncoder.encode(dccity, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage(), e);
            }

            web_storage.loadUrl("https://gh.jishanhengrui.com/index.html#/?token=" + ghtoken + "&nihao=" + result.toUpperCase() + "");
            web_storage.goBack();
            //showToast(result.toUpperCase());
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == GPS_REQUEST_CODE) {
            openGPSSEtting();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != this.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            Toast.makeText(getBaseContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }


    public void startLocaion() {

        mlocationClient = new AMapLocationClient(getApplicationContext());
        mlocationClient.setLocationListener(mLocationListener);

        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);

        //给定位客户端对象设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        //启动定位

        mlocationClient.startLocation();
    }


    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    Log.i(TAG, "当前定位结果来源-----" + amapLocation.getLocationType());//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    Log.i(TAG, "纬度 ----------------" + amapLocation.getLatitude());//获取纬度
                    Log.i(TAG, "经度-----------------" + amapLocation.getLongitude());//获取经度
                    Log.i(TAG, "精度信息-------------" + amapLocation.getAccuracy());//获取精度信息
                    Log.i(TAG, "地址-----------------" + amapLocation.getAddress());//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    Log.i(TAG, "国家信息-------------" + amapLocation.getCountry());//国家信息
                    Log.i(TAG, "省信息---------------" + amapLocation.getProvince());//省信息
                    Log.i(TAG, "城市信息-------------" + amapLocation.getCity());//城市信息
                    dccity = amapLocation.getCity();

                    try {
                        result = URLEncoder.encode(dccity, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }


                    //          web_storage.loadUrl("https://gh.jishanhengrui.com/index.html#/?token=" + ghtoken + "&nihao=" + result.toUpperCase() + "");
                    x = amapLocation.getLatitude();
                    y = amapLocation.getLongitude();
                    Log.i(TAG, "城区信息-------------" + amapLocation.getDistrict());//城区信息
                    Log.i(TAG, "街道信息-------------" + amapLocation.getStreet());//街道信息
                    Log.i(TAG, "街道门牌号信息-------" + amapLocation.getStreetNum());//街道门牌号信息
                    Log.i(TAG, "城市编码-------------" + amapLocation.getCityCode());//城市编码
                    Log.i(TAG, "地区编码-------------" + amapLocation.getAdCode());//地区编码
                    Log.i(TAG, "当前定位点的信息-----" + amapLocation.getAoiName());//获取当前定位点的AOI信息
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
        // 停止定位
        if (null != mlocationClient) {
            mlocationClient.stopLocation();
        }
    }

    /**
     * 销毁定位
     */
    private void destroyLocation() {
        if (null != mlocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mlocationClient.onDestroy();
            mlocationClient = null;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200://刚才的识别码
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//用户同意权限,执行我们的操作
                    startLocaion();//开始定位
                } else {//用户拒绝之后,当然我们也可以弹出一个窗口,直接跳转到系统设置页面
                    Toast.makeText(this, "未开启定位权限,请手动到设置去开启权限", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }


    //配置权限
    private class MyWbChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //web_storage.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }


    //获取是否更新缓存
    private void getUserInfo() {
        String patha = "http://39.104.49.122:810/XingRongAppServer/Order/Gonghui?tel=" + mAccount.getPhone();
        try {
            URL url = new URL(patha);
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            // 3.设置一些请求方式
            conn.setRequestMethod("GET");// 注意GET单词字幕一定要大写
            conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            int code = conn.getResponseCode(); // 服务器的响应码 200 OK //404 页面找不到
            if (code == 200) {
                InputStream is = conn.getInputStream();
                // 把is的内容转换为字符串
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                String result = new String(bos.toByteArray());
                try {
                    // 整个最大的JSON对象
                    JSONObject jsonObjectALL = new JSONObject(result);
                    /**
                     * 为什么要使用jsonObject.optString， 不使用jsonObject.getString
                     * 因为jsonObject.optString获取null不会报错
                     */
                    String data = jsonObjectALL.optString("data", null);
                    if (!TextUtils.isEmpty(data)) {
                        JSONObject jsonObject = new JSONObject(data);
                        String gengxin = jsonObject.optString("gengxin");
                        gonghuigengxin = gengxin;
                        if ("1".equals(gengxin)) {

                        } else {
                            web_storage.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //清楚缓存
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                is.close();
            } else {
                // showToast("请求失败，该用户已经注册");
                //Toast.makeText(this, "请求失败，失败原因: " + code, ).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //更新工会的信息，传过来具体地址解析成坐标
    private void getAddress(String canshu) {
        String patha = "http://distance.lfxrsc.com/admin/map/compre?pstart=3&pend=4&token=acd890f765efd007cbb5701fd1ac7ae0&startadd=" + canshu + "&endadd=太原";
        try {
            URL url = new URL(patha);
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            // 3.设置一些请求方式
            conn.setRequestMethod("GET");// 注意GET单词字幕一定要大写
            conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            int code = conn.getResponseCode(); // 服务器的响应码 200 OK //404 页面找不到
            if (code == 200) {
                InputStream is = conn.getInputStream();
                // 把is的内容转换为字符串
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                String result = new String(bos.toByteArray());
                try {
                    // 整个最大的JSON对象
                    JSONObject jsonObjectALL = new JSONObject(result);
                    /**
                     * 为什么要使用jsonObject.optString， 不使用jsonObject.getString
                     * 因为jsonObject.optString获取null不会报错
                     */
                    String data = jsonObjectALL.optString("data", null);
                    if (!TextUtils.isEmpty(data)) {
                        JSONObject jsonObject = new JSONObject(data);
                        String startx = jsonObject.optString("startx");
                        String starty = jsonObject.optString("starty");
                        jiexistartx = Double.parseDouble(startx);
                        jiexistarty = Double.parseDouble(starty);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                is.close();
            } else {
                // showToast("请求失败，该用户已经注册");
                //Toast.makeText(this, "请求失败，失败原因: " + code, ).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class JsInterface {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void showInfoFromJs(String name) {

//       Toast.makeText(mContext, "app获得参数111"+name, Toast.LENGTH_SHORT).show();

            openGPSSEtting();
            getAddress(name);
//            Intent intent = new Intent(mContext, GPSNaviActivity.class);
//            intent.putExtra("x",String.valueOf(jiexistarty));
//            intent.putExtra("y",String.valueOf(jiexistartx));  //不行的话就把顺序换一下
//            intent.putExtra("xxx",(x).toString());
//            intent.putExtra("yyy", (y).toString());
//            startActivity(intent);
            goToBMap(mContext, String.valueOf(jiexistarty), String.valueOf(jiexistartx));


        }
    }

    /**
     * 跳转到高德地图
     *
     * @param context   使用Application
     * @param latitude
     * @param longitude
     */
    public void goToBMap(Context context, String latitude, String longitude) {
        //默认驾车
        String uri = "amapuri://route/plan/"
                + "?dlat=" + latitude + "&dlon=" + longitude
                + "&sname=我的位置"
                + "&dname=终点"
                + "&dev=1"
                + "&t=0";
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uri));
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
    }

    //在java中调用js代码
    public void sendInfoToJs(View view) {
        String msg = "";
        //调用js中的函数：showInfoFromJava(msg)
        web_storage.loadUrl("javascript:showInfoFromJava('" + msg + "')");
    }


    private class JsInterfaces {
        private Context mContext;

        public JsInterfaces(Context context) {
            this.mContext = context;
        }

        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void showInfoFromJss(String name) {
            //Toast.makeText(mContext, "app获得参数"+name, Toast.LENGTH_SHORT).show();

            String sourceStr = name;
            String[] sourceStrArray = sourceStr.split(",");
            for (int i = 0; i < sourceStrArray.length; i++) {
                System.out.println(sourceStrArray[0]);
                System.out.println(sourceStrArray[1]);
            }

            Intent intent = new Intent(mContext, ChongzhiActivity.class);
            SimpleDateFormat s_format = new SimpleDateFormat("yyyyMMddhhmmss");
            intent.putExtra("goods_id", sourceStrArray[1]);
            intent.putExtra("present_price", sourceStrArray[2]);
            intent.putExtra("num", sourceStrArray[3]);
            intent.putExtra("price", String.valueOf(Double.parseDouble(sourceStrArray[4]) * 100));
            intent.putExtra("order_number", s_format.format(new Date()));
            intent.putExtra("token", ghtoken);
            startActivity(intent);
            openGPSSEtting();

        }
    }

    //在java中调用js代码
    public void sendInfoToJss(View view) {
        String msg = "";
        //调用js中的函数：showInfoFromJava(msg)
        web_storage.loadUrl("javascript:showInfoFromJava('" + msg + "')");
    }


    private class JsInterfacetel {
        private Context mContext;

        public JsInterfacetel(Context context) {
            this.mContext = context;
        }

        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void showInfoFromJstel(String name) {

            //Toast.makeText(mContext, "app获得参数tel"+name, Toast.LENGTH_SHORT).show();
            jstel = name;
            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PermissionUtils.isGranted(Manifest.permission.CALL_PHONE))
                    || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                new AlertDialog.Builder(mContext).setMessage("是否拨打电话")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent2 = new Intent();
                                intent2.setData(Uri.parse("tel:" + jstel));
                                intent2.setAction(Intent.ACTION_CALL);
                                startActivity(intent2);
                            }
                        }).show();
            } else {
                new AlertDialog.Builder(mContext).setMessage("请先打开电话权限")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionUtils.openAppSettings();
                            }
                        }).show();
            }


        }
    }

    //在java中调用js代码
    public void sendInfoToJstel(View view) {
        String msg = "";
        //调用js中的函数：showInfoFromJava(msg)
        web_storage.loadUrl("javascript:showInfoFromJava('" + msg + "')");
    }


    private boolean checkGpsIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    private void openGPSSEtting() {
        if (checkGpsIsOpen()) {
            //Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(this).setTitle("请打开定位权限")
                    .setMessage("打开位置信息")
                    //  取消选项
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(WebActivityputong.this, "close", Toast.LENGTH_SHORT).show();
                            // 关闭dialog
                            dialogInterface.dismiss();
                        }
                    })
                    //  确认选项
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //跳转到手机原生设置页面
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, GPS_REQUEST_CODE);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

}
