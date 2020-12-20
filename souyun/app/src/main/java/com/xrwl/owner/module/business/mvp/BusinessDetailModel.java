package com.xrwl.owner.module.business.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.retrofit.RetrofitFactory;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/8/16 下午8:33.
 */
public class BusinessDetailModel implements BusinessDetailContract.IModel {
    @Override
    public Observable<BaseEntity> requestData(Map<String, String> params) {
        return RetrofitFactory.getInstance().markBusinessRead(params).compose(RxSchedulers.<BaseEntity>compose());
    }
}
