package com.xrwl.owner.retrofit;

import android.util.Log;

import com.ldw.library.bean.BaseEntity;

import io.reactivex.Observer;

public abstract class BaseObserver<T> implements Observer<BaseEntity<T>> {

    private static final String TAG = "BaseObserver";

    @Override
    public void onNext(BaseEntity<T> value) {
        onHandleSuccess(value);
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "error:" + e.toString());
        onHandleError(e);
    }

    @Override
    public void onComplete() {
    }

    protected abstract void onHandleSuccess(BaseEntity<T> entity);

    protected abstract void onHandleError(Throwable e);
}