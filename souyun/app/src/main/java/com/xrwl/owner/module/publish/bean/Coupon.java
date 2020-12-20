package com.xrwl.owner.module.publish.bean;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * 优惠券
 * Created by www.longdw.com on 2018/4/14 下午10:27.
 */
public class Coupon {
    public String full;
    public String reduce;

    public List<Coupon> data;

    public static Coupon parseJson(JSONObject json) {
        return new Gson().fromJson(json.toString(), Coupon.class);
    }
}
