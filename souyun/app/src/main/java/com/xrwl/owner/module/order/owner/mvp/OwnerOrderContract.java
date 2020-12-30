package com.xrwl.owner.module.order.owner.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.bean.HistoryOrder;
import com.xrwl.owner.bean.Order;
import com.xrwl.owner.bean.OrderDetail;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.mvp.MyLoadPresenter;
import com.xrwl.owner.mvp.MyPresenter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/21 下午1:14.
 */
public interface OwnerOrderContract {
    interface IView extends BaseMVP.IBaseLoadView<List<Order>> {
    }

    abstract class APresenter extends MyLoadPresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void getOrderList(String type);

        public abstract void getMoreOrderList(String type);

    }

    interface IModel {
        Observable<BaseEntity<List<Order>>> getOrderList(Map<String, String> params);
    }

    interface IDetailView extends BaseMVP.IBaseView<OrderDetail> {


        void resultsSuccess(BaseEntity<PayResult> entity);


        void onNavSuccess(BaseEntity<OrderDetail> entity);
        void onNavError(Throwable e);


        void onCancelOrderSuccess(BaseEntity<OrderDetail> entity);
        void onCancelOrderError(Throwable e);

        void onCancelpayOrderSuccess(BaseEntity<OrderDetail> entity);
        void onCancelpayOrderError(Throwable e);

        void onConfirmOrderSuccess(BaseEntity<OrderDetail> entity);
        void onConfirmOrderError(Throwable e);

        void onConfirmtixingOrderSuccess(BaseEntity<OrderDetail> entity);
        void onConfirmtixingOrderError(Throwable e);


        void onConfirmOwnerkaishiyunshuSuccess(BaseEntity<OrderDetail> entity);
        void onConfirmOwnerkaishiyunshuError(Throwable e);

        void onLocationSuccess(BaseEntity<OrderDetail> entity);
        void onLocationError(Throwable e);

        void onPaySuccess(BaseEntity<PayResult> entity);
        void onPayError(Throwable e);

        void wxonOnlinePaySuccess(BaseEntity<PayResult> entity);
        void wxonOnlinePayError(Throwable e);

        void wxonOnlinedaishouPaySuccess(BaseEntity<PayResult> entity);
        void wxonOnlinedaishouPayError(Throwable e);

        void onCancelDriverkaishiyunshuSuccess(BaseEntity<OrderDetail> entity);
        void onCancelDriverkaishiyunshuError(Throwable e);


        void onTotalPriceSuccess(String price);

        void onTixianSuccess();
        void onTixianError(BaseEntity entity);
        void onTixianException(Throwable e);


        void onTixiantuikuanSuccess();
        void onTixiantuikuanError(BaseEntity entity);
        void onTixiantuikuanException(Throwable e);


        void onPingJiaSuccess();
        void onPingJiaError(BaseEntity entity);
        void onPingJiaException(Throwable e);

        void ongetlistpingjiaSuccess(BaseEntity<String> entity);
        void ongetlistpingjiaError(BaseEntity entity);


        void ongetlistpingjiaException(Throwable e);
        void onetlistpingjiasSuccess(String duqu, String username, String dianhua, String chexing, String pingfen, String chehao);


        void onWx_refundSuccess(BaseEntity<PayResult> entity);
        void onWx_refundError(Throwable e);


        void updateOrderdunSuccess(BaseEntity entity);
        void updateOrderdunError(BaseEntity e);


    }

    abstract class ADetailPresenter extends MyPresenter<IDetailView> {

        public ADetailPresenter(Context context) {
            super(context);
        }

        public abstract void nav(String id);

        public abstract void getOrderDetail(String id);

        public abstract void updateOrderdun(String id,String weight);

        public abstract void cancelOrder(String id);

        public abstract void cancelpayOrder(String id);

        public abstract void confirmOrder(String id);

        public abstract void confirmtixingOrder(String id);

        public abstract void confirmOwnerkaishiyunshu(String id);

        public abstract void location(String id, String driverId);

        public abstract void wxPay(Map<String, String> params);

        public abstract void wxonlinePay(Map<String, String> params);
        public abstract void wxonlinedaishouPay(Map<String, String> params);

        /** 确认开始运输货主确认 */
        public abstract void cancelDriverkaishiyunshu(String id);
        public abstract void getTotalPriceBalance();
        public abstract void tixian(String price,String yinhangka,String orderid,String zftype);
        public abstract void yuepay(String price,String yinhangka,String orderid,String zftype);
        public abstract void yuepayrefund(String orderid);
        public abstract void dianping(String orderId, String driverid, String userid,String first, String two, String three, String four, String addtime, String content);

        public abstract void getlistpingjias(String orderid);

        public abstract void xrwlwxpay(Map<String, String> params);
        public abstract void wx_refund(Map<String, String> params);

        public abstract void results(Map<String, String> params);
    }

    interface IDetailModel {
        Observable<BaseEntity<OrderDetail>> getOrderDetail(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> nav(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> cancelOrder(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> cancelpayOrder(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> confirmOrder(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> confirmtixingOrder(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> confirmOwnerkaishiyunshu(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> location(Map<String, String> params);
        Observable<BaseEntity<PayResult>> pay(Map<String, String> params);
        Observable<BaseEntity<PayResult>> wxonlinePay(Map<String, String> params);
        Observable<BaseEntity<PayResult>> wxonlinedaishouPay(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> cancelDriverkaishiyunshu(Map<String, String> params);
        Observable<BaseEntity<HistoryOrder>> getTotalPriceBalance(Map<String, String> params);
        Observable<BaseEntity> tixian(Map<String, String> params);
        Observable<BaseEntity> yuepay(Map<String, String> params);
        Observable<BaseEntity> yuepayrefund(Map<String, String> params);
        Observable<BaseEntity> updateOrderdun(Map<String, String> params);
       /**在线点评*/
        Observable<BaseEntity> dianping(Map<String, String> params);

        Observable<BaseEntity<OrderDetail>> getlistpingjias(Map<String, String> params);

        Observable<BaseEntity<PayResult>> xrwlwxpay(Map<String, String> params);

        Observable<BaseEntity<PayResult>> wx_refund(Map<String, String> params);
        Observable<BaseEntity<PayResult>> results(Map<String, String> params);
    }
}
