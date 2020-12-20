package com.xrwl.owner.module.me.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Aliyunbank;
import com.xrwl.owner.bean.OrderDetail;
import com.xrwl.owner.retrofit.BaseObserver;
import com.xrwl.owner.retrofit.BaseSimpleObserver;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/4/21 上午9:30.
 */
public class AddBankPresenter extends BankContract.AAddPresenter {

    private BankContract.IModel mModel;

    public AddBankPresenter(Context context) {
        super(context);

        mModel = new BankModel();
    }

    @Override
    public void addBank(Map<String, String> params) {
        params.put("userid", getAccount().getId());
        mModel.addBank(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
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
    public void getbanksanyuansu(String accountNo, String idCard, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("accountNo", accountNo);
        params.put("idCard", idCard);
        params.put("name", name);
        mModel.getbanksanyuansu(params).subscribe(new BaseObserver<Aliyunbank>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
            @Override
            protected void onHandleSuccess(BaseEntity<Aliyunbank> entity) {
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
