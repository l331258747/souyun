package com.xrwl.owner.Frame.retrofitapi;


import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Cljbxx;
import com.xrwl.owner.bean.DriverpositioningBeen;
import com.xrwl.owner.bean.FacerecognitionBeen;
import com.xrwl.owner.bean.Update;
import com.xrwl.owner.bean.updateBeen;
import com.xrwl.owner.bean.wxhdBeen;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * retrofit请求参数封装与rxjava相结合
 * author:zbb
 * 2018/8/30
 **/
public interface NetService {


    /**
     * M每个注解都是想对象的 请参考以下接口
     */


    /**
     * 司机定位接口
     */
    @FormUrlEncoded
    @POST("Order/Coordinates")
    Observable<DriverpositioningBeen> Driverpositioning(@FieldMap Map<String, String> params);


    /**
     * 无车车辆基本信息
     */
    @FormUrlEncoded
    @POST("WLHY_CL1001")
    Observable<Cljbxx> Cljbxx(@FieldMap Map<String, String> params);

    /**
     * 微信支付
     */
    @FormUrlEncoded
    @POST("payinterface.php")
    Observable<wxhdBeen> wxhd(@FieldMap Map<String, String> params);

    /** 检查更新 */
    @FormUrlEncoded
    @POST("Other/Update")
    Observable<updateBeen> updateBeen(@FieldMap Map<String, String> params);

}
