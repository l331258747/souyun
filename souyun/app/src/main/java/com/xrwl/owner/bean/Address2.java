package com.xrwl.owner.bean;

import android.text.TextUtils;

/**
 * 收发货地址 没办法 跟Address类重名了
 * Created by www.longdw.com on 2018/7/11 下午7:51.
 */
public class Address2 {
    public String id;
    public String lat;
    public String lng;
    public String des;
    public String city;
    public String province;
    public String tel;
    public String username;

    public String getId() {
        return id;
    }

    public String getName(){
        String str = "";
        if(!TextUtils.isEmpty(username))
            str = str + username + " ";
        if(!TextUtils.isEmpty(tel))
            str = str + "tel:" +  tel;
        return str;
    }

    public String getTel() {
        return tel;
    }

    public String getUserName() {
        return username;
    }

    public String getShengshixian(){
        String str = "";
        if(!TextUtils.isEmpty(province))
            str = str + province;
        if(!TextUtils.isEmpty(city))
            str = str + city;
        return str;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getDes() {
        return des;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }
}
