package com.xrwl.owner.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ldw.library.mvp.BaseMVP;

import org.greenrobot.eventbus.EventBus;

/**
 * 如果有用到EventBus可以继承此类 但是要记住必须要有@Subscribe注解过的方法
 * Created by www.longdw.com on 2018/3/21 下午3:03.
 */

public abstract class BaseEventActivity<V extends BaseMVP.IBaseView, P extends BaseMVP.BasePresenter<V>> extends BaseActivity<V, P> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}