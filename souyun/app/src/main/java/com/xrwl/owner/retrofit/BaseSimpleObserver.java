package com.xrwl.owner.retrofit;

import android.util.Log;

import io.reactivex.Observer;

public abstract class BaseSimpleObserver<T> implements Observer<T> {

    private static final String TAG = "BaseObserver";

    @Override
    public void onNext(T value) {
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


    protected abstract void onHandleSuccess(T entity);

    protected abstract void onHandleError(Throwable e);
}