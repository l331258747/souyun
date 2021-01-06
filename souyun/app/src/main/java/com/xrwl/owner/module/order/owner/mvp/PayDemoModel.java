package com.xrwl.owner.module.order.owner.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.HistoryOrder;
import com.xrwl.owner.bean.OrderDetail;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.retrofit.RetrofitFactory;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/28 上午10:39.
 */
public class PayDemoModel implements OwnerOrderContract.IDetailModel {
    @Override
    public Observable<BaseEntity<OrderDetail>> getOrderDetail(Map<String, String> params) {
        return RetrofitFactory.getInstance().getOwnerOrderDetail(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> nav(Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> cancelOrder(Map<String, String> params) {
        return RetrofitFactory.getInstance().cancelOwnerOrder(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> cancelpayOrder(Map<String, String> params) {
        return RetrofitFactory.getInstance().cancelpayOwnerOrder(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> confirmOrder(Map<String, String> params) {
        return RetrofitFactory.getInstance().confirmOwnerOrder(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> confirmtixingOrder(Map<String, String> params) {
        return RetrofitFactory.getInstance().confirmtixingOwnerOrder(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> confirmOwnerkaishiyunshu(Map<String, String> params) {
        return RetrofitFactory.getInstance().confirmOwnerkaishiyunshuOrder(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> location(Map<String, String> params) {
        return RetrofitFactory.getInstance().locationDriver(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<PayResult>> pay(Map<String, String> params) {
        return RetrofitFactory.getInstance().pay(params).compose(RxSchedulers.<BaseEntity<PayResult>>compose());
    }

    @Override
    public Observable<BaseEntity<PayResult>> wxonlinePay(Map<String, String> params) {
        return RetrofitFactory.getInstance().wxonlinePay(params).compose(RxSchedulers.<BaseEntity<PayResult>>compose());
    }

    @Override
    public Observable<BaseEntity<PayResult>> wxonlinedaishouPay(Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> cancelDriverkaishiyunshu(Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<BaseEntity<HistoryOrder>> getTotalPriceBalance(Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<BaseEntity> tixian(Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<BaseEntity> yuepay(Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<BaseEntity> yuepayrefund(Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<BaseEntity> updateOrderdun(Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<BaseEntity> dianping(Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> getlistpingjias(Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<BaseEntity<PayResult>> xrwlwxpay(Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<BaseEntity<PayResult>> wx_refund(Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<BaseEntity<PayResult>> results(Map<String, String> params) {
        return RetrofitFactory.getInstance().results(params).compose(RxSchedulers.<BaseEntity<PayResult>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> uploadImages(Map<String, RequestBody> params) {
        return null;
    }


}
