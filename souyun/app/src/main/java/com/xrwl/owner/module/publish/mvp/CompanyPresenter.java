package com.xrwl.owner.module.publish.mvp;

import android.content.Context;
import android.text.TextUtils;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.module.publish.bean.CompanyManageBean;
import com.xrwl.owner.retrofit.BaseObserver;
import com.xrwl.owner.retrofit.BaseSimpleObserver;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

public class CompanyPresenter extends CompanyContract.APresenter{

    private CompanyContract.IModel mModel;

    public CompanyPresenter(Context context) {
        super(context);
        mModel = new CompanyModel();
    }

    @Override
    public void getData() {
        Map<String, String> params = new HashMap<>();
        params.put("userid", getAccount().getId());
        mModel.getData(params).subscribe(new BaseObserver<List<CompanyManageBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<CompanyManageBean>> entity) {
                if (entity.isSuccess()) {
                    mView.getDataSuccess(entity);
                } else {
                    mView.getDataError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onRefreshError(e);
            }
        });
    }

    @Override
    public void addData(Map<String, String> params) {
        params.put("userid", getAccount().getId());
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

        mModel.addData(sendParams).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
                if (entity.isSuccess()) {
                    mView.addDataSuccess(entity);
                } else {
                    mView.addDataError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onRefreshError(e);
            }
        });
    }
}
