package com.xrwl.owner.module.home.mvp;

import android.content.Context;
import android.text.TextUtils;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.HistoryOrder;
import com.xrwl.owner.bean.Order;
import com.xrwl.owner.retrofit.BaseObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/4/27 下午9:05.
 */
public class HistoryPresenter extends HistoryContract.APresenter {

    private HistoryContract.IModel mModel;

    private Account mAccount;

    private List<Order> mDatas;

    public HistoryPresenter(Context context) {
        super(context);

        mAccount = getAccount();
        mModel = new HistoryModel();
    }

    @Override
    public void getList() {
        mPageIndex = 1;
        Map<String, String> params = new HashMap<>();
        params.put("userid", mAccount.getId());
        params.put("pages", String.valueOf(mPageIndex));
        params.put("type", mAccount.getType());
        mModel.getData(params).subscribe(new BaseObserver<HistoryOrder>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<HistoryOrder> entity) {
                if (entity.isSuccess()) {
                    HistoryOrder ho = entity.getData();
                    mDatas = ho.getLists();
                    String hasnextStr = ho.getHasnext();
                    boolean hasnext = (!TextUtils.isEmpty(hasnextStr) && hasnextStr.equals("1"));
                    mView.onRefreshCallback(ho.getNum(), ho.getPrice(), hasnext, mDatas);
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
    public void getMoreList() {
        mPageIndex++;
        Map<String, String> params = new HashMap<>();
        params.put("userid", getAccount().getId());
        params.put("pages", String.valueOf(mPageIndex));
        params.put("type", mAccount.getType());
        mModel.getData(params).subscribe(new BaseObserver<HistoryOrder>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<HistoryOrder> entity) {
                if (entity.isSuccess()) {
                    HistoryOrder ho = entity.getData();
                    mDatas.addAll(ho.getLists());

                    String hasnextStr = ho.getHasnext();
                    boolean hasnext = (!TextUtils.isEmpty(hasnextStr) && hasnextStr.equals("1"));

                    mView.onLoadCallback(hasnext, mDatas);
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
