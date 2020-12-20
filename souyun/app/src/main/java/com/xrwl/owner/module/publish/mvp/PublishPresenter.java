package com.xrwl.owner.module.publish.mvp;

import android.content.Context;
import android.text.TextUtils;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Distance;
import com.xrwl.owner.bean.GongAnAuth;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.retrofit.BaseObserver;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/23 上午9:12.
 */
public class PublishPresenter extends PublishContract.APresenter {

    private PublishContract.IModel mModel;

    public PublishPresenter(Context context) {
        super(context);

        mModel = new PublishModel();
    }



    @Override
    public void calculateDistancecount(Map<String, String> params) {
           mModel.calculateDistancecount(params).subscribe(new BaseObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<Integer> entity) {
                if (entity.isSuccess()) {
                    mView.onRequestSuccessa(entity);
                } else {
                    mView.onError(entity);

                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onRefreshError(e);
            }
        });
    }



//    @Override
//    public void calculateprice(String token,int type,String time,String distance,int car_type,String ton,String square,String piece) {
//        Map<String, String> params = new HashMap<>();
//        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
//        params.put("type", "0");
//        params.put("time", "1");
//        params.put("distance", "11");
//        params.put("car_type","0");
//        params.put("ton", "1");
//        params.put("square","1");
//        params.put("piece","1");
//        mModel.calculateprice(params).subscribe(new BaseObserver<Distance>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                addDisposable(d);
//            }
//
//            @Override
//            protected void onHandleSuccess(BaseEntity<Distance> entity) {
//                if (entity.isSuccess()) {
//                    mView.onRefreshSuccess(entity);
//                } else {
//                    mView.onError(entity);
//                }
//            }
//
//            @Override
//            protected void onHandleError(Throwable e) {
//                mView.onRefreshError(e);
//            }
//        });
//    }
//    @Override
//    public void calculateDistancecount(Map<String, String> params) {
//        Map<String, RequestBody> images = new HashMap<>();
//        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
//        params.put("type", "0");
//        params.put("time", "1");
//        params.put("distance", "11");
//        params.put("car_type","0");
//        params.put("ton", "1");
//        params.put("square","1");
//        params.put("piece","1");
//
//        mModel.calculateDistancecount(params).subscribe(new BaseObserver<Distance>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                addDisposable(d);
//            }
//
//            @Override
//            protected void onHandleSuccess(BaseEntity<Distance> entity) {
//                if (entity.isSuccess()) {
//                    mView.onRefreshSuccess(entity);
//                } else {
//                    mView.onError(entity);
//                }
//            }
//
//            @Override
//            protected void onHandleError(Throwable e) {
//                mView.onRefreshError(e);
//            }
//        });
//    }

//    @Override
//    public void calculateDistancecount() {
//
//        mModel.calculateDistancecount(params).subscribe(new BaseObserver<Distance>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                addDisposable(d);
//            }
//
//            @Override
//            protected void onHandleSuccess(BaseEntity<Distance> entity) {
//                if (entity.isSuccess()) {
//                    mView.onRefreshSuccess(entity);
//                } else {
//                    mView.onError(entity);
//                }
//            }
//
//            @Override
//            protected void onHandleError(Throwable e) {
//                mView.onRefreshError(e);
//            }
//        });
//    }

    @Override
    public void calculateDistanceWithLonLat(double startLon, double startLat, double endLon, double endLat) {
        Map<String, String> params = new HashMap<>();
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
        params.put("type", "0");
        params.put("startx", startLon + "");
        params.put("starty", startLat + "");
        params.put("endx", endLon + "");
        params.put("endy", endLat + "");
        mModel.calculateDistance(params).subscribe(new BaseObserver<Distance>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<Distance> entity) {
                if (entity.isSuccess()) {
                    mView.onRefreshSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onRefreshError(e);
            }
        });
    }

    @Override
    public void calculateDistanceWithCityName(String startCity, String endCity, String startPro, String endPro) {
        Map<String, String> params = new HashMap<>();
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
//        params.put("type", "1");
        params.put("startadd", startCity);
        params.put("endadd", endCity);
        params.put("pstart", startPro);
        params.put("pend", endPro);
        mModel.calculateLongDistance(params).subscribe(new BaseObserver<Distance>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<Distance> entity) {
                if (entity.isSuccess()) {
                    mView.onRefreshSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onRefreshError(e);
            }
        });
    }

    @Override
    public void requestCityLonLat(String startCity, final String endCity) {
        Map<String, String> params = new HashMap<>();
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
        params.put("startadd", startCity);
        params.put("endadd", endCity);
        mModel.requestCityLonLat(params).subscribe(new BaseObserver<Distance>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<Distance> entity) {
                if (entity.isSuccess()) {
                    mView.onRequestCityLatLonSuccess(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {

            }
        });
    }


}
