package com.xrwl.owner.module.order.owner.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.HistoryOrder;
import com.xrwl.owner.bean.OrderDetail;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.retrofit.BaseObserver;
import com.xrwl.owner.retrofit.BaseSimpleObserver;
import com.xrwl.owner.utils.AccountUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/4/28 上午10:42.
 */
public class OwnerOrderDetailPresenter extends OwnerOrderContract.ADetailPresenter {
    private Account mAccount;
    private OwnerOrderContract.IDetailModel mModel;

    public OwnerOrderDetailPresenter(Context context) {
        super(context);

        mModel = new OwnerOrderDetailModel();
        mAccount = AccountUtil.getAccount(context);
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

    @Override
    public void updateOrderdun(String id, String weight) {
        Map<String, String> params = new HashMap<>();
        params.put("userid", getAccount().getId());
        params.put("id", id);
        params.put("weight", weight);

        mModel.updateOrderdun(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
                if (entity.isSuccess()) {

                    mView.updateOrderdunSuccess(entity);
                } else {
                    mView.updateOrderdunError(entity);
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
    public void wx_refund(Map<String, String> params) {

        mModel.wx_refund(params).subscribe(new BaseObserver<PayResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<PayResult> entity) {
                if (entity.isSuccess()) {

                    mView.onWx_refundSuccess(entity);
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
        mModel.confirmtixingOrder(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onConfirmtixingOrderSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onConfirmtixingOrderError(e);
            }
        });
    }
    @Override
    public void confirmOwnerkaishiyunshu(String id) {
        mModel.confirmOwnerkaishiyunshu(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onConfirmOwnerkaishiyunshuSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onConfirmOwnerkaishiyunshuError(e);
            }
        });
    }

    @Override
    public void cancelDriverkaishiyunshu(String id) {
        mModel.cancelDriverkaishiyunshu(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onCancelDriverkaishiyunshuSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onCancelDriverkaishiyunshuError(e);
            }
        });
    }


    @Override
    public void location(String id, String driverId) {
        Map<String, String> params = getParams(id);
        params.put("userid", driverId);
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



    @Override
    public void wxonlinedaishouPay(Map<String, String> params) {
        mModel.wxonlinedaishouPay(params).subscribe(new BaseObserver<PayResult>() {
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
                mView.onRefreshError(e);
            }
        });
    }

    @Override
    public void tixian(String price,String yinhangka,String orderid,String zftype)
    {
        Map<String, String> params = new HashMap<>();
        params.put("userid", mAccount.getId());
        params.put("jine", price);
        params.put("yinhangka", yinhangka);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(new Date());
        params.put("addtime", date);
        params.put("type", "0");
        params.put("orderid", orderid);
        params.put("zftype", zftype);
        mModel.tixian(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
                if (entity.isSuccess()) {
                    mView.onTixianSuccess();
                } else {
                    mView.onTixianError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onTixianException(e);
            }
        });
    }



    @Override
    public void yuepay(String price,String yinhangka,String orderid,String zftype)
    {
        Map<String, String> params = new HashMap<>();
        params.put("userid", mAccount.getId());
        params.put("jine", price);
        params.put("yinhangka", yinhangka);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(new Date());
        params.put("addtime", date);
        params.put("type", "0");
        params.put("orderid", orderid);
        params.put("zftype", zftype);
        mModel.yuepay(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
                if (entity.isSuccess()) {
                    mView.onTixianSuccess();
                } else {
                    mView.onTixianError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onTixianException(e);
            }
        });
    }




    @Override
    public void yuepayrefund(String orderid)
    {
        Map<String, String> params = new HashMap<>();
        params.put("userid", mAccount.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(new Date());
        params.put("addtime", date);
        params.put("orderid", orderid);

        mModel.yuepayrefund(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
                if (entity.isSuccess()) {
                    mView.onTixiantuikuanSuccess();
                } else {
                    mView.onTixiantuikuanError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onTixiantuikuanException(e);
            }
        });
    }



    @Override
    public void dianping(String orderId, String driverid, String userid, String first, String two, String three, String four, String addtime, String content) {
        Map<String, String> params = new HashMap<>();

        params.put("orderId",orderId);
        params.put("driverid",driverid);
        params.put("userid", mAccount.getId());
        params.put("first", first);
        params.put("two", two);
        params.put("three", three);
        params.put("four", four);
        params.put("addtime", addtime);
        params.put("content", content);

        mModel.dianping(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
                if (entity.isSuccess()) {
                    mView.onPingJiaSuccess();
                } else {
                    mView.onPingJiaError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onPingJiaException(e);
            }
        });
    }


    @Override
    public void getTotalPriceBalance() {
        Map<String, String> params = new HashMap<>();
        params.put("userid", mAccount.getId());
        params.put("pages", "1");
        params.put("type", "0");
        mModel.getTotalPriceBalance(params).subscribe(new BaseObserver<HistoryOrder>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<HistoryOrder> entity) {
                if (entity.isSuccess()) {
                    HistoryOrder ho = entity.getData();
                    mView.onTotalPriceSuccess(ho.getPrice());
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

    @Override
    public void getlistpingjias(String orderid) {
        Map<String, String> params = new HashMap<>();

        params.put("orderid",orderid);
        mModel.getlistpingjias(params).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    OrderDetail ho = entity.getData();
                    mView.onetlistpingjiasSuccess(ho.getDuqu(),ho.getUsername(),ho.getDianhua(),ho.getChexing(),ho.getPingfen(),ho.getChehao());
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
    public void xrwlwxpay(Map<String, String> params) {

        mModel.xrwlwxpay(params).subscribe(new BaseObserver<PayResult>() {
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


}
