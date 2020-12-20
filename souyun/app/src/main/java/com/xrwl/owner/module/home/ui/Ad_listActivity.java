package com.xrwl.owner.module.home.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.Auth;
import com.xrwl.owner.module.home.mvp.DriverAuthContract;
import com.xrwl.owner.module.home.mvp.DriverAuthPresenter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 广告图片显示
 * Created by www.longdw.com on 2018/4/4 下午10:29.
 */
public class Ad_listActivity extends BaseActivity<DriverAuthContract.IView, DriverAuthPresenter> implements DriverAuthContract
        .IView {
    @Override
    protected DriverAuthPresenter initPresenter() {
        return new DriverAuthPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ad_listactivity;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void getData() {
    }


    @Override
    public void onPostSuccess(BaseEntity entity) {

    }

    @Override
    public void onPostError(BaseEntity entity) {

    }

    @Override
    public void onPostError(Throwable e) {

    }

    @Override
    public void onRefreshSuccess(BaseEntity<Auth> entity) {

    }

    @Override
    public void onRefreshError(Throwable e) {

    }

    @Override
    public void onError(BaseEntity entity) {

    }
}
