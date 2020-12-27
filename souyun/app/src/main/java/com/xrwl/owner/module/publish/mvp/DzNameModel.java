package com.xrwl.owner.module.publish.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.module.publish.bean.DzNameManageBean;
import com.xrwl.owner.retrofit.RetrofitFactory;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class DzNameModel implements DzNameContract.IModel{
    @Override
    public Observable<BaseEntity<List<DzNameManageBean>>> getData(Map<String, String> params) {
        return RetrofitFactory.getInstance().getDzName(params).compose(RxSchedulers.<BaseEntity<List<DzNameManageBean>>>compose());
    }

    @Override
    public Observable<BaseEntity> addData(Map<String, String> params) {
        return RetrofitFactory.getInstance().addDzName(params).compose(RxSchedulers.<BaseEntity>compose());
    }
}
