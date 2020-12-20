package com.xrwl.owner.module.order.driver.ui;

import android.media.MediaPlayer;

import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;

import butterknife.OnClick;

/**
 * 抢单成功
 * Created by www.longdw.com on 2018/4/3 下午10:39.
 */

public class GrapSuccessActivity extends BaseActivity {
    @Override
    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.grapsuccess_activity;
    }

    @Override
    protected void initViews() {
        MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.gongxininqiangdanchenggong);
        mediaPlayer.start();
    }

    @OnClick(R.id.psOkBtn)
    public void onClick() {
        finish();
    }
}
