package com.xrwl.owner.module.publish.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Distance;
import com.xrwl.owner.retrofit.OtherRetrofitFactory;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/23 上午9:10.
 */
public class PublishModel implements PublishContract.IModel {

    @Override
    public Observable<BaseEntity<Integer>> calculateDistancecount(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://jiekou.16souyun.com/").calculateDistancecount(params).compose(RxSchedulers
                .<BaseEntity<Integer>>compose());
    }


    @Override
    ///public/admin/map
    public Observable<BaseEntity<Distance>> calculateDistance(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://distance.16souyun.com/").calculateDistance(params).compose(RxSchedulers
                .<BaseEntity<Distance>>compose());
    }

    @Override
    //public/admin/map/compre
    public Observable<BaseEntity<Distance>> calculateLongDistance(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://distance.16souyun.com/").calculateLongDistance(params).compose(RxSchedulers
                .<BaseEntity<Distance>>compose());
    }

    @Override
    //public/admin/map
    public Observable<BaseEntity<Distance>> requestCityLonLat(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://distance.16souyun.com/").requestCityLonLat(params).compose(RxSchedulers
                .<BaseEntity<Distance>>compose());
    }
}
