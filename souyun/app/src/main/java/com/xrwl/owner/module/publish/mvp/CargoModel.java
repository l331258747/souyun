package com.xrwl.owner.module.publish.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.module.publish.bean.CargoBean;
import com.xrwl.owner.retrofit.OtherRetrofitFactory;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/19 下午1:33.
 */
public class CargoModel implements CargoContract.IModel {

    @Override
    public Observable<BaseEntity<List<CargoBean>>> getList(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://app.16souyun.com/").onetypeall(params).compose(RxSchedulers
                .<BaseEntity<List<CargoBean>>>compose());
    }

    @Override
    public Observable<BaseEntity<List<CargoBean>>> getList2(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://app.16souyun.com/").twotypeall(params).compose(RxSchedulers
                .<BaseEntity<List<CargoBean>>>compose());
    }
}
