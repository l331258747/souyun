package com.xrwl.owner.module.find.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Order;
import com.xrwl.owner.retrofit.RetrofitFactory;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/5/3 下午10:13.
 */
public class FindModel implements FindContract.IModel {
    @Override
    public Observable<BaseEntity<List<Order>>> getData(Map<String, String> params) {
        return RetrofitFactory.getInstance().getFindList(params).compose(RxSchedulers.<BaseEntity<List<Order>>>compose());
    }

}
