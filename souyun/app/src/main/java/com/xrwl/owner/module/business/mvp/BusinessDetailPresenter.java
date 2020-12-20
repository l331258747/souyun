package com.xrwl.owner.module.business.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.retrofit.BaseSimpleObserver;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/8/16 下午8:35.
 */
public class BusinessDetailPresenter extends BusinessDetailContract.APresenter {

    private BusinessDetailContract.IModel mModel;

    public BusinessDetailPresenter(Context context) {
        super(context);
        mModel = new BusinessDetailModel();
    }

    @Override
    public void requestData(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        mModel.requestData(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
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
}
