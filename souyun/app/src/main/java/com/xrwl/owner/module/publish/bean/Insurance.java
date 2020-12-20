package com.xrwl.owner.module.publish.bean;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/4/14 下午9:16.
 */
public class Insurance {
    public String title;
    public String des;
    public String price;

    public List<Insurance> data;

    public static Insurance parseJson(JSONObject json) {
        return new Gson().fromJson(json.toString(), Insurance.class);
    }
}
