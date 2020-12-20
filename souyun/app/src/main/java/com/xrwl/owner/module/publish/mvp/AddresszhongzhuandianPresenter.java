package com.xrwl.owner.module.publish.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Address2zhongzhuandian;
import com.xrwl.owner.retrofit.BaseObserver;
import com.xrwl.owner.retrofit.BaseSimpleObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/7/11 下午9:25.
 */
public class AddresszhongzhuandianPresenter extends AddresszhongzhuandianContract.APresenter {

    private AddresszhongzhuandianContract.IModel mModel;

    public AddresszhongzhuandianPresenter(Context context) {
        super(context);
        mModel = new AddresszhongzhuandianModel();
    }

    @Override
    public void getData() {
        Map<String, String> params = new HashMap<>();
        params.put("userid", getAccount().getId());
        mModel.getData(params).subscribe(new BaseObserver<List<Address2zhongzhuandian>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<Address2zhongzhuandian>> entity) {
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
        mModel.postData(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
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
}
