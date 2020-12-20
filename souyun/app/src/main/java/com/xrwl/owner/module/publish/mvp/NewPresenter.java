package com.xrwl.owner.module.publish.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.New;
import com.xrwl.owner.retrofit.BaseObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/7/11 下午9:25.
 */
public class NewPresenter extends NewContract.APresenter {

    private AddressContract.IModel mModel;

    public NewPresenter(Context context) {
        super(context);
        mModel = new AddressModel();
    }

    @Override
    public void getData() {

    }


    @Override
    public void getDatanew() {
        Map<String, String> params = new HashMap<>();
        params.put("userid", getAccount().getId());
        mModel.getDatanew(params).subscribe(new BaseObserver<List<New>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<New>> entity) {
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
    public void getDatanewshow(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        mModel.getDatanewshow(params).subscribe(new BaseObserver<List<New>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<New>> entity) {
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
    public void postData(HashMap<String, String> params) {

    }


}
