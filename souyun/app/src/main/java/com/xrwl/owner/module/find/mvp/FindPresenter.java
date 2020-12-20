package com.xrwl.owner.module.find.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Order;
import com.xrwl.owner.retrofit.BaseObserver;

import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/5/3 下午10:23.
 */
public class FindPresenter extends FindContract.APresenter {

    private FindContract.IModel mModel;
    private List<Order> mDatas;

    public FindPresenter(Context context) {
        super(context);

        mModel = new FindModel();
    }

    @Override
    public void getData(Map<String, String> params) {
        mView.showLoading();
        mPageIndex = 0;

//        for (String key : params.keySet()) {
//            String value = params.get(key);
//            try {
//                params.put(key, URLEncoder.encode(value, "UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                Log.e(TAG, e.getMessage(), e);
//            }
//        }

        params.put("userid", getAccount().getId());
        params.put("pages", mPageIndex + "");
        mModel.getData(params).subscribe(new BaseObserver<List<Order>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<Order>> entity) {
                if (entity.isSuccess()) {
                    mDatas = entity.getData();
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
    public void getMoreData(Map<String, String> params) {
        mPageIndex++;

//        for (String key : params.keySet()) {
//            String value = params.get(key);
//            try {
//                params.put(key, URLEncoder.encode(value, "UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                Log.e(TAG, e.getMessage(), e);
//            }
//        }

        mModel.getData(params).subscribe(new BaseObserver<List<Order>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<Order>> entity) {
                if (entity.isSuccess()) {
                    mDatas.addAll(entity.getData());
                    entity.setData(mDatas);
                    mView.onRefreshSuccess(entity);
                } else {
                    mPageIndex--;
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mPageIndex--;
                mView.onRefreshError(e);
            }
        });
    }
}
