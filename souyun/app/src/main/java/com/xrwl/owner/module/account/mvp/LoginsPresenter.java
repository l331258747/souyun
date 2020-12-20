package com.xrwl.owner.module.account.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.retrofit.BaseObserver;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/4/8 下午8:33.
 */
public class LoginsPresenter extends AccountContract.ALoginPresenter {

    private AccountModel mModel;

    public LoginsPresenter(Context context) {
        super(context);

        mModel = new AccountModel();
    }

    @Override
    public void login(final Map<String, String> params) {
        mModel.login(params).subscribe(new BaseObserver<Account>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<Account> entity) {
                if (entity.isSuccess()) {
                    String username = params.get("username");
                    String password = params.get("password");
                    entity.getData().setUsername(username);
                    entity.getData().setPassword(password);

                    entity.getData().setLogin(true);
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
    public void loginwx(Map<String, String> params) {

    }

    @Override
    public void getCode(String mobile) {
        Map<String, String> params = new HashMap<>();
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
        params.put("mobile", mobile);
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
