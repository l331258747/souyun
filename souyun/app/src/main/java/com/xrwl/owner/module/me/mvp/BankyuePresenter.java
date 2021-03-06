package com.xrwl.owner.module.me.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.HistoryOrder;
import com.xrwl.owner.module.me.bean.Bank;
import com.xrwl.owner.module.me.bean.Tixianlist;
import com.xrwl.owner.retrofit.BaseObserver;
import com.xrwl.owner.retrofit.BaseSimpleObserver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/4/21 上午9:45.
 */
public class BankyuePresenter extends BankyueContract.APresenter {

    private BankyueContract.IModel mModel;
    private Account mAccount;

    public BankyuePresenter(Context context) {
        super(context);

        mModel = new BankyueModel();

        mAccount = getAccount();
    }

    @Override
    public void getBankList() {
        Map<String, String> params = new HashMap<>();
        params.put("userid", mAccount.getId());
        mModel.getBankList(params).subscribe(new BaseObserver<List<Bank>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<Bank>> entity) {
                if (entity.isSuccess()) {
                   // mView.onRefreshSuccess(entity);
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
    public void gettixianlist() {
        Map<String, String> params = new HashMap<>();
        params.put("userid", mAccount.getId());
        mModel.gettixianlist(params).subscribe(new BaseObserver<List<Tixianlist>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<Tixianlist>> entity) {
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

    @Override
    public void getTotalPrice() {
        Map<String, String> params = new HashMap<>();
        params.put("userid", mAccount.getId());
        params.put("pages", "1");
        params.put("type", mAccount.getType());
        mModel.getTotalPrice(params).subscribe(new BaseObserver<HistoryOrder>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<HistoryOrder> entity) {
                if (entity.isSuccess()) {
                    HistoryOrder ho = entity.getData();
                    mView.onTotalPriceSuccess(ho.getPrice());
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
    public void getTotalPriceBalance() {
        Map<String, String> params = new HashMap<>();
        params.put("userid", mAccount.getId());
        params.put("pages", "1");
        params.put("type", "0");
        mModel.getTotalPriceBalance(params).subscribe(new BaseObserver<HistoryOrder>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<HistoryOrder> entity) {
                if (entity.isSuccess()) {
                    HistoryOrder ho = entity.getData();
                    mView.onTotalPriceSuccess(ho.getPrice());
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
    public void tixian(String price,String yinhangka)
    {
        Map<String, String> params = new HashMap<>();
        params.put("userid", mAccount.getId());
        params.put("jine", price);
        params.put("yinhangka", yinhangka);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(new Date());
        params.put("addtime", date);
        params.put("type", "0");
        mModel.tixian(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
                if (entity.isSuccess()) {
                    mView.onTixianSuccess();
                } else {
                    mView.onTixianError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onTixianException(e);
            }
        });
    }


    @Override
    public void hongbao(String price,String userid,String orderid)
    {
        Map<String, String> params = new HashMap<>();
        params.put("userid", mAccount.getId());
        params.put("price", price);
        params.put("orderid", orderid);
        mModel.hongbao(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
                if (entity.isSuccess()) {
                    mView.onTixianSuccess();
                } else {
                    mView.onTixianError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onTixianException(e);
            }
        });
    }
}
