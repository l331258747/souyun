package com.xrwl.owner.module.home.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.HistoryOrder;
import com.xrwl.owner.retrofit.RetrofitFactory;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/27 下午9:05.
 */
public class HistoryModel implements HistoryContract.IModel {
    @Override
    public Observable<BaseEntity<HistoryOrder>> getData(Map<String, String> params) {
        return RetrofitFactory.getInstance().getHistoryList(params).compose(RxSchedulers.<BaseEntity<HistoryOrder>>compose());
    }

}
