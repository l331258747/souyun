package com.xrwl.owner.base;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.R;

import butterknife.BindView;

/**
 * Created by www.longdw.com on 2018/4/7 上午8:52.
 */
public abstract class BaseWebActivity extends BaseActivity {

    @BindView(R.id.baseWv)
    public WebView mWebView;

    @BindView(R.id.baseProgressBar)
    public ProgressBar mProgressBar;

    @Override
    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.baseweb_activity;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initViews() {
        //支持javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        mWebView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mWebView.getSettings().setBuiltInZoomControls(true);
        String ua = mWebView.getSettings().getUserAgentString();

        mWebView.getSettings().setUserAgentString(ua+"; Android▍");
        //扩大比例的缩放
        mWebView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (mProgressBar == null) {
                    return;
                }
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                    if (mProgressBar.equals("null")){
                        Toast.makeText(mContext, "下载完成 请注意查看，测试数据", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(mContext, "下载失败，请注意查看，册数数据", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }
}
