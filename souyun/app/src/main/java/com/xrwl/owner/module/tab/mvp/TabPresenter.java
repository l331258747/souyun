package com.xrwl.owner.module.tab.mvp;

import android.content.Context;
import android.util.Log;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.Business;
import com.xrwl.owner.retrofit.BaseObserver;
import com.xrwl.owner.retrofit.BaseSimpleObserver;
import com.xrwl.owner.utils.AccountUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/5/4 上午10:52.
 */
public class TabPresenter extends TabContract.APresenter {

    private final Account mAccount;
    private TabContract.IModel mModel;

    public TabPresenter(Context context) {
        super(context);

        mModel = new TabModel();

        mAccount = AccountUtil.getAccount(context);
    }

    @Override
    public void postLonLat(double lon, double lat) {
        Map<String, String> params = new HashMap<>();
        params.put("userid", getAccount().getId());
        params.put("start_lon", lon + "");
        params.put("start_lat", lat + "");
        mModel.postLonLat(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
                if (entity.isSuccess()) {
                    Log.e(TAG, "经纬度传输成功");
                } else {
                    Log.e(TAG, "经纬度传输失败");
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                Log.e(TAG, "经纬度传输失败", e);
            }
        });
    }

    @Override
    public void getBadgeCount() {
        Map<String, String> params = new HashMap<>();
        params.put("userid", mAccount.getId());
        mModel.getBadgeCount(params).subscribe(new BaseObserver<List<Business>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<Business>> entity) {
                if (entity.isSuccess()) {
                    mView.onRefreshSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onRefreshError(e);
            }
        });
    }
}
