package com.xrwl.owner.module.publish.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.bean.Distance;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/23 上午9:03.
 */
public interface PublishContract {
    interface IView extends BaseMVP.IBaseView<Distance> {
        void onRequestCityLatLonSuccess(BaseEntity<Distance> entity);
        void onRequestSuccessa(BaseEntity<Integer> entity);
    }

    abstract class APresenter extends BaseMVP.BasePresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }
        public abstract void calculateDistancecount(Map<String, String> params);
        public abstract void calculateDistanceWithLonLat(double startLon, double startLat, double endLon, double endLat);
        public abstract void calculateDistanceWithCityName(String startCity, String endCity, String startPro, String endPro);
        public abstract void requestCityLonLat(String startCity, String endCity);

    }

    interface IModel {

//        Observable<BaseEntity<Distance>> calculateprice(Map<String, String> params);
        Observable<BaseEntity<Integer>> calculateDistancecount(Map<String, String> params);
        Observable<BaseEntity<Distance>> calculateDistance(Map<String, String> params);
        Observable<BaseEntity<Distance>> calculateLongDistance(Map<String, String> params);
        Observable<BaseEntity<Distance>> requestCityLonLat(Map<String, String> params);

    }
}
