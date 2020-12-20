package com.xrwl.owner.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by www.longdw.com on 2018/8/16 下午6:47.
 */
public class Business {
    public String id;
    public String type;
    public String title;
    @SerializedName("addtime")
    public String time;
    public String pic;
    public String product_name;
    public String date;
    public String total_price;
    public String start_province;
    public String start_city;
    public String end_province;
    public String end_city;
    public String content;
    public String get_person;
    public String get_phone;
    public String start_area;
    public String end_area;
    public String start_desc;
    public String end_desc;
    public String systemcanshu;
    public String orderid;
    public String contents;
    public String classify;
    public String publish_person;
    public String publish_phone;
}
