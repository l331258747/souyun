package com.xrwl.owner.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by www.longdw.com on 2018/4/25 上午10:06.
 */
public class Distance {
    public String distance;
    public String duration;//用的总时间
    public String start;//开始的县或者区
    public String end;//结束的县或者区
    public String ton;//每公里每吨的价格
    public String square;//每公里每方的价格
//    public String startarea;//算代理商的代理订单
//    public String endarea;
    @SerializedName("start_x")
    public String startX;
    @SerializedName("start_y")
    public String endX;
    @SerializedName("end_x")
    public String startY;
    @SerializedName("end_y")
    public String endY;



    //史浩价格接口返回数据
    //token
    public String token;
    //运输类型
    public int types;
    //时间 (小时)
    public String time;
    //距离 (公里)
    public String distances;
    //车型(对应type,车型只有整车有)
    public int car_type;
   // 吨
    public String tons;
    //方
    public String squares;
    //件
    public String piece;

    //fanhuizongjia
    public String datapay;

    //fanhuizongjia
    public String qinyanwei;

    public String nidaye;
}
