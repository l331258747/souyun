package com.xrwl.owner.module.publish.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.module.publish.bean.CarManageBean;
import com.xrwl.owner.module.publish.bean.CarManageSearchBean;
import com.xrwl.owner.retrofit.RetrofitFactory;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class CarModel implements CarContract.IModel{
    @Override
    public Observable<BaseEntity<List<CarManageBean>>> getData(Map<String, String> params) {
        return RetrofitFactory.getInstance().getCar(params).compose(RxSchedulers.<BaseEntity<List<CarManageBean>>>compose());
    }

    @Override
    public Observable<BaseEntity> addData(Map<String, String> params) {
        return RetrofitFactory.getInstance().addCar(params).compose(RxSchedulers.<BaseEntity>compose());
    }

    @Override
    public Observable<BaseEntity<List<CarManageSearchBean>>> searchCar(Map<String, String> params) {
        return RetrofitFactory.getInstance().searchCar(params).compose(RxSchedulers.<BaseEntity<List<CarManageSearchBean>>>compose());
    }
}
