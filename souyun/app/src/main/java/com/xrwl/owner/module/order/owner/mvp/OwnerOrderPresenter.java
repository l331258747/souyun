package com.xrwl.owner.module.order.owner.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Order;
import com.xrwl.owner.retrofit.BaseObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/4/21 下午1:21.
 */
public class OwnerOrderPresenter extends OwnerOrderContract.APresenter {

    private OwnerOrderContract.IModel mModel;
    private List<Order> mDatas;

    public OwnerOrderPresenter(Context context) {
        super(context);
        mModel = new OwnerOrderModel();
    }

    @Override
    public void getOrderList(String type) {
        mPageIndex = 1;
        Map<String, String> params = new HashMap<>();
        params.put("userid", getAccount().getId());
        params.put("type", type);
        params.put("pages", String.valueOf(mPageIndex));
        mModel.getOrderList(params).subscribe(new BaseObserver<List<Order>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<Order>> entity) {
                if (entity.isSuccess()) {
                    mDatas = entity.getData();
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

    @Override
    public void getMoreOrderList(String type) {
        mPageIndex++;
        Map<String, String> params = new HashMap<>();
        params.put("userid", getAccount().getId());
        params.put("type", type);
        params.put("pages", String.valueOf(mPageIndex));
        mModel.getOrderList(params).subscribe(new BaseObserver<List<Order>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<Order>> entity) {
                if (entity.isSuccess()) {
                    mDatas.addAll(entity.getData());
                    entity.setData(mDatas);
                    mView.onLoadSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onLoadError(e);
            }
        });
    }
}
