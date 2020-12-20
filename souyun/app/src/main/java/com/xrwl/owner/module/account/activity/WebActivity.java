package com.xrwl.owner.module.account.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseWebActivity;
import com.xrwl.owner.module.publish.ui.ChongzhiActivity;

import java.text.SimpleDateFormat;

/**
 * Created by www.longdw.com on 2018/3/25 下午9:20.
 */

public class WebActivity extends BaseWebActivity {

    private WebView web_storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_storage);
        String b = "http://xrsc.16souyun.com/mobile/";
        Intent intent = getIntent();
        String s = intent.getStringExtra("url");
//        Log.e("loadurl", s);
        if (s == null) {
            s = b;
        }

        web_storage = (WebView) findViewById(R.id.web_view);



        WebSettings webSettings = web_storage.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web_storage.setWebChromeClient(new WebChromeClient());

        webSettings.setJavaScriptEnabled(true);//是否允许JavaScript脚本运行，默认为false
        webSettings.setDomStorageEnabled(true);//开启本地DOM存储
        webSettings.setJavaScriptEnabled(true);
        //是否使用WebView内置的放大机制，貌似设置了这条以后下面那条不用设置了
        webSettings.setBuiltInZoomControls(true);
        //设置WebView是否支持放大
        webSettings.setSupportZoom(false);
        //以下两条设置可以使页面适应手机屏幕的分辨率，完整的显示在屏幕上
        //设置是否使用WebView推荐使用的窗口
        webSettings.setUseWideViewPort(true);

        //设置WebView加载页面的模式
        webSettings.setLoadWithOverviewMode(true);
        //设置WebView加载页面的模式
        webSettings.setLoadWithOverviewMode(true);





        web_storage.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        web_storage.loadUrl(s);
        web_storage.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //支持javascript
        web_storage.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        web_storage.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        web_storage.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        web_storage.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        web_storage.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web_storage.getSettings().setLoadWithOverviewMode(true);
    }
}

