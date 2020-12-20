package com.xrwl.owner.bean;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/3/21 下午10:32.
 */

public class HomeItem {
    public String icon;
    public String title;
    public String url;
    public List<HomeItem> data1;
    public List<HomeItem> data2;
    public List<HomeItem> ad;
    public List<HomeItem> ad2;
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<HomeItem> getData1() {
        return data1;
    }

    public void setData1(List<HomeItem> data1) {
        this.data1 = data1;
    }

    public List<HomeItem> getData2() {
        return data2;
    }

    public void setData2(List<HomeItem> data2) {
        this.data2 = data2;
    }

    public List<HomeItem> getAd() {
        return ad;
    }

    public void setAd(List<HomeItem> ad) {
        this.ad = ad;
    }


    public List<HomeItem> getAd2() {
        return ad2;
    }

    public void setAd2(List<HomeItem> ad2) {
        this.ad2 = ad2;
    }


    public static HomeItem parseJson(JSONObject json) {
        return new Gson().fromJson(json.toString(), HomeItem.class);
    }
}
