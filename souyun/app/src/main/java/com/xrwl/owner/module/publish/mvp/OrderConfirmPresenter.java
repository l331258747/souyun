package com.xrwl.owner.module.publish.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.HistoryOrder;
import com.xrwl.owner.bean.MsgCode;
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
 * Created by www.longdw.com on 2018/4/23 下午1:26.
 */
public class OrderConfirmPresenter extends OrderConfirmContract.APresenter {

    private OrderConfirmModel mModel;
    private  Account mAccount;
    public OrderConfirmPresenter(Context context) {
        super(context);

        mModel = new OrderConfirmModel();
        mAccount = AccountUtil.getAccount(context);
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
    public void czwxPay(Map<String, String> params) {
        mModel.czpay(params).subscribe(new BaseObserver<PayResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<PayResult> entity) {
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
    public void onlinePay(Map<String, String> params) {
        mModel.onlinePay(params).subscribe(new BaseObserver<PayResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<PayResult> entity) {
                if (entity.isSuccess()) {
                    mView.onOnlinePaySuccess(entity);
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
    public void hongbao(String price, String userid) {
        Map<String, String> params = new HashMap<>();
        params.put("userid", mAccount.getId());
        params.put("price", price);
        mModel.hongbao(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
                if (entity.isSuccess()) {
                    mView.onHongbaoSuccess();
                } else {
                    mView.onHongbaoError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onHongbaoException(e);
            }
        });
    }

    @Override
    public void getCodeButton(String mobile,String type,String start_city,String end_city,String order_sn) {
        Map<String, String> params = new HashMap<>();
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
        params.put("mobile", mobile);
        params.put("type","1");
        params.put("start_city",start_city);
        params.put("end_city",end_city);
        params.put("order_sn",order_sn);
        mModel.getCodeButton(params).subscribe(new BaseObserver<MsgCode>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<MsgCode> entity) {
                try {
                    mView.onGetCodeSuccess(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.onGetCodeError(e);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onGetCodeError(e);
            }
        });
    }
    @Override
    public void getCodeButtontwo(String mobile,String type,String start_city,String end_city,String name,String phone) {
        Map<String, String> params = new HashMap<>();
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
        params.put("mobile", mobile);
        params.put("type","2");
        params.put("start_city",start_city);
        params.put("end_city",end_city);
        params.put("name",name);
        params.put("phone",phone);
        mModel.getCodeButtontwo(params).subscribe(new BaseObserver<MsgCode>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<MsgCode> entity) {
                try {
                    mView.onGetCodeSuccess(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.onGetCodeError(e);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onGetCodeError(e);
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
