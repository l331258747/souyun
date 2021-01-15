package com.xrwl.owner.module.publish.mvp;

import android.content.Context;
import android.text.TextUtils;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Address2;
import com.xrwl.owner.retrofit.BaseObserver;
import com.xrwl.owner.retrofit.BaseSimpleObserver;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/7/11 下午9:25.
 */
public class AddressPresenter extends AddressContract.APresenter {

    private AddressContract.IModel mModel;

    public AddressPresenter(Context context) {
        super(context);
        mModel = new AddressModel();
    }

    @Override
    public void getData() {
        Map<String, String> params = new HashMap<>();
        params.put("userid", getAccount().getId());
        mModel.getData(params).subscribe(new BaseObserver<List<Address2>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<Address2>> entity) {
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
    public void getDatanew() {

    }

    @Override
    public void getDatanewshow(String id) {

    }


    @Override
    public void postData(HashMap<String, String> params) {

        Map<String, String> sendParams = new HashMap<>();

        for (String key : params.keySet()) {
            try {
                String value = params.get(key);
                if (!TextUtils.isEmpty(value)) {
                    String encodedParam = URLEncoder.encode(value, "UTF-8");
                    sendParams.put(key, encodedParam);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        mModel.postData(sendParams).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
                if (entity.isSuccess()) {
                    mView.onPostSuccess(entity);
                } else {
                    mView.onPostError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onPostError(e);
            }
        });
    }

    @Override
    public void CancelAddress(HashMap<String, String> params) {
        params.put("userid", getAccount().getId());
        mModel.CancelAddress(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
                if (entity.isSuccess()) {
                    mView.onCancelAddressSuccess(entity);
                } else {
                    mView.onCancelAddressError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onRefreshError(e);
            }
        });
    }
}
