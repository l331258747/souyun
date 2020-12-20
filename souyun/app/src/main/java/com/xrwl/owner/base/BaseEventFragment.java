package com.xrwl.owner.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ldw.library.mvp.BaseMVP;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by www.longdw.com on 2018/4/27 下午3:32.
 */
public abstract class BaseEventFragment<V extends BaseMVP.IBaseView, P extends BaseMVP.BasePresenter<V>> extends BaseFragment<V, P> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

}
