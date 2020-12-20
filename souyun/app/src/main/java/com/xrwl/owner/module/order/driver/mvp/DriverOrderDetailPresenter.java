package com.xrwl.owner.module.order.driver.mvp;

import android.content.Context;
import android.text.TextUtils;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.OrderDetail;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.module.publish.bean.Photo;
import com.xrwl.owner.retrofit.BaseObserver;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/28 上午10:42.
 */
public class DriverOrderDetailPresenter extends DriverOrderContract.ADetailPresenter {

    private DriverOrderContract.IDetailModel mModel;

    public DriverOrderDetailPresenter(Context context) {
        super(context);

        mModel = new DriverOrderDetailModel();
    }

    @Override
    public void getOrderDetail(String id) {

        mView.showLoading();

        Map<String, String> params = new HashMap<>();
        params.put("userid", getAccount().getId());
        params.put("id", id);
        mModel.getOrderDetail(params).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
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
    public void nav(String id) {
        mModel.nav(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onNavSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onNavError(e);
            }
        });
    }

    private Map<String, String> getParams(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("userid", getAccount().getId());
        params.put("id", id);
        return params;
    }

    @Override
    public void cancelOrder(String id) {
        mModel.cancelOrder(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onCancelOrderSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onCancelOrderError(e);
            }
        });
    }

    @Override
    public void confirmOrder(String id) {
        mModel.confirmOrder(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onConfirmOrderSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onConfirmOrderError(e);
            }
        });
    }

    @Override
    public void trans(String id) {
        mModel.trans(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onTransSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onTransError(e);
            }
        });
    }

    @Override
    public void grapOrder(String id) {
        mModel.grapOrder(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onGrapOrderSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onGrapOrderError(e);
            }
        });
    }

    @Override
    public void uploadImages(String id, List<Photo> images) {
        Map<String, RequestBody> params = new HashMap<>();

        Map<String, String> strParams = new HashMap<>();
        strParams.put("id", id);
        strParams.put("userid", getAccount().getId());

        for (String key : strParams.keySet()) {
            try {
                String value = strParams.get(key);
                if (!TextUtils.isEmpty(value)) {
                    String encodedParam = URLEncoder.encode(value, "UTF-8");
                    params.put(key, RequestBody.create(MediaType.parse("text/plain"), encodedParam));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        for (Photo photo : images) {
            if (photo.isCanDelete()) {
                File file = new File(photo.getPath());
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
                params.put("images\"; filename=\"" + file.getName(), requestBody);
            }
        }

        mModel.uploadImages(params).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onUploadImagesSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onUploadImagesError(e);
            }
        });
    }

    @Override
    public void pay(Map<String, String> params) {
        mModel.pay(params).subscribe(new BaseObserver<PayResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<PayResult> entity) {
                if (entity.isSuccess()) {
                    mView.onWxPaySuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onWxPayError(e);
            }
        });
    }
}
