package com.xrwl.owner.module.account.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.retrofit.BaseObserver;
import com.xrwl.owner.retrofit.BaseSimpleObserver;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/4/8 下午8:33.
 */
public class ModifyPresenter extends AccountContract.AModifyPresenter {

    private AccountContract.IModel mModel;

    public ModifyPresenter(Context context) {
        super(context);

        mModel = new AccountModel();
    }

    @Override
    public void modify(Map<String, String> params) {
        params.put("userid", getAccount().getId());
        mModel.modify(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
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
    public void retrieve(Map<String, String> params) {
        mModel.retrieve(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
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
    public void getCode(String phone) {
        Map<String, String> params = new HashMap<>();
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
        params.put("mobile", phone);
        mModel.getCode(params).subscribe(new BaseObserver<MsgCode>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<MsgCode> entity) {
                try {
                    mView.onGetCodeSuccess(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.onGetCodeError(e);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onGetCodeError(e);
            }
        });
    }

    @Override
    public void wxtoken(String code) {

    }

    @Override
    public void wxuserinfo(String access_token, String openid) {

    }

}
