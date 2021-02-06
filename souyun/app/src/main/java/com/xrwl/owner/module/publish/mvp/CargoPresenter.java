package com.xrwl.owner.module.publish.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.module.publish.bean.CargoBean;
import com.xrwl.owner.retrofit.BaseObserver;
import com.xrwl.owner.utils.AccountUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

public class CargoPresenter extends CargoContract.APresenter {

    private CargoContract.IModel mModel;
    private Account mAccount;
    public CargoPresenter(Context context) {
        super(context);

        mModel = new CargoModel();
        mAccount = AccountUtil.getAccount(context);
    }

    @Override
    public void getList(Map<String, String> params) {

        mModel.getList(params).subscribe(new BaseObserver<List<CargoBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<CargoBean>> entity) {
                if (entity.isSuccess()) {
                    mView.getListSuccessa(entity);
                } else {
                    mView.getListError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.getListException(e);
            }
        });
    }

    @Override
    public void getList2(Map<String, String> params) {
        mModel.getList2(params).subscribe(new BaseObserver<List<CargoBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<CargoBean>> entity) {
                if (entity.isSuccess()) {
                    mView.getListSuccessa2(entity);
                } else {
                    mView.getListError2(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.getListException2(e);
            }
        });
    }
}
