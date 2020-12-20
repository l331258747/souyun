package com.xrwl.owner.module.order.driver.mvp;

import com.ldw.library.bean.BaseEntity;
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
public class DriverOrderDetailModel implements DriverOrderContract.IDetailModel {
    @Override
    public Observable<BaseEntity<OrderDetail>> getOrderDetail(Map<String, String> params) {
        return RetrofitFactory.getInstance().getDriverOrderDetail(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> nav(Map<String, String> params) {
        return RetrofitFactory.getInstance().nav(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> cancelOrder(Map<String, String> params) {
        return RetrofitFactory.getInstance().cancelDriverOrder(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> confirmOrder(Map<String, String> params) {
        return RetrofitFactory.getInstance().confirmDriverOrder(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> trans(Map<String, String> params) {
        return RetrofitFactory.getInstance().trans(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> grapOrder(Map<String, String> params) {
        return RetrofitFactory.getInstance().grapOrder(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> uploadImages(Map<String, RequestBody> params) {
        return RetrofitFactory.getInstance().uploadDriverImages(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<PayResult>> pay(Map<String, String> params) {
        return RetrofitFactory.getInstance().pay(params);
    }
}
