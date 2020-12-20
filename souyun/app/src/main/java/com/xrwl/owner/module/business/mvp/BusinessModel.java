package com.xrwl.owner.module.business.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Business;
import com.xrwl.owner.retrofit.RetrofitFactory;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/8/16 下午7:08.
 */
public class BusinessModel implements BusinessContract.IModel {
    @Override
    public Observable<BaseEntity<List<Business>>> getData(Map<String, String> params) {
        return RetrofitFactory.getInstance().getBusinessList(params).compose(RxSchedulers.<BaseEntity<List<Business>>>compose());
    }
//    @Override
//    public Observable<BaseEntity<List<Business>>> getDataSystem(Map<String, String> params) {
//        return RetrofitFactory.getInstance().getSystemList(params).compose(RxSchedulers.<BaseEntity<List<Business>>>compose());
//    }
}
