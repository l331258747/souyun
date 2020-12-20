package com.xrwl.owner.module.home.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Auth;
import com.xrwl.owner.retrofit.RetrofitFactory;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/29 下午9:11.
 */
public class DriverAuthModel implements DriverAuthContract.IModel {
    @Override
    public Observable<BaseEntity<Auth>> getData(Map<String, String> params) {
        return RetrofitFactory.getInstance().getAuthInfo(params).compose(RxSchedulers.<BaseEntity<Auth>>compose());
    }

    @Override
    public Observable<BaseEntity> postData(Map<String, RequestBody> params) {
        return RetrofitFactory.getInstance().postDriverAuthInfo(params).compose(RxSchedulers.<BaseEntity>compose());
    }
}
