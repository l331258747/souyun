package com.xrwl.owner.module.account.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.bean.WxCode;
import com.xrwl.owner.retrofit.BaseObserver;
import com.xrwl.owner.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/4/8 下午8:33.
 */
public class LoginPresenter extends AccountContract.ALoginPresenter {

    private AccountModel mModel;

    public LoginPresenter(Context context) {
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
    public void loginwx(final Map<String, String> params) {
        mModel.loginwx(params).subscribe(new BaseObserver<Account>() {
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
        Map<String, String> params = new HashMap<>();
        params.put("appid", Constants.APP_ID);
        params.put("secret", Constants.APP_SECRET);
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        mModel.wxtoken(params).subscribe(new BaseObserver<WxCode>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<WxCode> entity) {
                try {
                    mView.onwxGetCodeSuccess(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.onwxGetCodeError(e);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onwxGetCodeError(e);
            }
        });
    }



    @Override
    public void wxuserinfo(String access_token,String openid) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", access_token);
        params.put("openid", openid);
        mModel.wxuserinfo(params).subscribe(new BaseObserver<WxCode>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<WxCode> entity) {
                try {
                    mView.onwxinfoGetCodeSuccess(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.onwxinfoGetCodeError(e);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onwxinfoGetCodeError(e);
            }
        });
    }


}
