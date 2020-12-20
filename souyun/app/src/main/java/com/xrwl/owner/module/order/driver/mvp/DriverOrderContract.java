package com.xrwl.owner.module.order.driver.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.bean.Order;
import com.xrwl.owner.bean.OrderDetail;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.module.publish.bean.Photo;
import com.xrwl.owner.mvp.MyLoadPresenter;
import com.xrwl.owner.mvp.MyPresenter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/21 下午1:14.
 */
public interface DriverOrderContract {
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

        void showLoading();

        void onWxPaySuccess(BaseEntity<PayResult> entity);
        void onWxPayError(Throwable e);

        void onNavSuccess(BaseEntity<OrderDetail> entity);
        void onNavError(Throwable e);

        void onCancelOrderSuccess(BaseEntity<OrderDetail> entity);
        void onCancelOrderError(Throwable e);

        void onConfirmOrderSuccess(BaseEntity<OrderDetail> entity);
        void onConfirmOrderError(Throwable e);

        void onLocationDriverSuccess(BaseEntity<OrderDetail> entity);
        void onLocationDriverError(Throwable e);

        void onTransSuccess(BaseEntity<OrderDetail> entity);
        void onTransError(Throwable e);

        void onGrapOrderSuccess(BaseEntity<OrderDetail> entity);
        void onGrapOrderError(Throwable e);

        void onUploadImagesSuccess(BaseEntity<OrderDetail> entity);
        void onUploadImagesError(Throwable e);
    }

    abstract class ADetailPresenter extends MyPresenter<IDetailView> {

        public ADetailPresenter(Context context) {
            super(context);
        }

        public abstract void getOrderDetail(String id);

        /** 导航 */
        public abstract void nav(String id);
        /** 取消订单 */
        public abstract void cancelOrder(String id);
        /** 确认收货 */
        public abstract void confirmOrder(String id);
        /** 中转 */
        public abstract void trans(String id);
        /** 抢单 */
        public abstract void grapOrder(String id);

        /** 上传图片 */
        public abstract void uploadImages(String id, List<Photo> images);

        public abstract void pay(Map<String, String> params);
    }

    interface IDetailModel {
        Observable<BaseEntity<OrderDetail>> getOrderDetail(Map<String, String> params);

        Observable<BaseEntity<OrderDetail>> nav(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> cancelOrder(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> confirmOrder(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> trans(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> grapOrder(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> uploadImages(Map<String, RequestBody> params);

        Observable<BaseEntity<PayResult>> pay(Map<String, String> params);
    }
}
