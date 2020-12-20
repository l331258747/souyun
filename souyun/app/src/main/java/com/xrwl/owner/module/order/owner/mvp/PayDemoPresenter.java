package com.xrwl.owner.module.order.owner.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.OrderDetail;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.retrofit.BaseObserver;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/4/28 上午10:42.
 */
public class PayDemoPresenter extends OwnerOrderContract.ADetailPresenter {

    private OwnerOrderContract.IDetailModel mModel;

    public PayDemoPresenter(Context context) {
        super(context);
        mModel = new PayDemoModel();
      
    }

    @Override
    public void nav(String id) {

    }

    @Override
    public void getOrderDetail(String id) {

        mModel.getOrderDetail(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
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

    public Map<String, String> getParams(String id) {
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
    public void cancelpayOrder(String id) {
        mModel.cancelpayOrder(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onCancelpayOrderSuccess(entity);
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
                mView.onCancelOrderError(e);
            }
        });
    }

    @Override
    public void confirmtixingOrder(String id) {

    }

    @Override
    public void confirmOwnerkaishiyunshu(String id) {

    }

    @Override
    public void location(String id, String driverId) {
        Map<String, String> params = getParams(id);
        params.put("userid", driverId);//接口定的太辣鸡
        mModel.location(params).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onLocationSuccess(entity);
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
    public void wxPay(Map<String, String> params) {
        mModel.pay(params).subscribe(new BaseObserver<PayResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<PayResult> entity) {
                if (entity.isSuccess()) {
                    mView.onPaySuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onPayError(e);
            }
        });
    }

    @Override
    public void wxonlinePay(Map<String, String> params) {
        mModel.wxonlinePay(params).subscribe(new BaseObserver<PayResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<PayResult> entity) {
                if (entity.isSuccess()) {
                    mView.wxonOnlinePaySuccess(entity);
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
    public void wxonlinedaishouPay(Map<String, String> params) {

    }

    @Override
    public void cancelDriverkaishiyunshu(String id) {

    }

    @Override
    public void getTotalPriceBalance() {

    }

    @Override
    public void tixian(String price, String yinhangka, String orderid, String zftype) {

    }

    @Override
    public void yuepay(String price, String yinhangka, String orderid, String zftype) {

    }

    @Override
    public void yuepayrefund(String orderid) {

    }

    @Override
    public void dianping(String orderId, String driverid, String userid, String first, String two, String three, String four, String addtime, String content) {

    }

    @Override
    public void getlistpingjias(String orderid) {

    }

    @Override
    public void xrwlwxpay(Map<String, String> params) {

    }

    @Override
    public void wx_refund(Map<String, String> params) {

    }

    @Override
    public void results(Map<String, String> params) {
        mModel.results(params).subscribe(new BaseObserver<PayResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<PayResult> entity) {
                if (entity.isSuccess()) {
                    mView.resultsSuccess(entity);
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
